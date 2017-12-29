/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.views;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.extensions.LogElement;
import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import java.lang.reflect.Field;
import java.util.Collections;

/**
 *
 * @author javert
 */
public class ActuatorView extends View {

    private static final Integer NUMBER_OF_COLS = 8;
    private static final Integer FIRST_COL_OFFSET = 3;
    private static final Integer COL_OFFSET = 5;

    @Override
    public void printContent() {
        super.printContent();

        Integer y = 1;

        for (LogElement log : Logger.getLOG(0)) {
            ANSIHelper.move(0, y++);
            log.accept(this.visitor);
        }

        this.printSeparator();
    }

    public void prepareTable(Actuator actuator) {
        String header = "";
        String row = "";

        for (Field field : Actuator.class.getFields()) {
            try {
                String header_part;
                String row_part;

                if (!header.isEmpty()) {
                    header_part = this.formatHeader(field.getName(), FIRST_COL_OFFSET) + " | ";
                } else {
                    header_part = "| " + this.formatHeader(field.getName(), COL_OFFSET) + " | ";
                }

                if (!row.isEmpty()) {
                    row_part = this.formatCell(field.get(actuator), FIRST_COL_OFFSET) + " | ";
                } else {
                    row_part = "| " + this.formatCell(field.get(actuator), COL_OFFSET) + " | ";
                }

                header += header_part;
                row += row_part;
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                // 
            }
        }

        Logger.getInstance().log(new LMessage("Actuator " + actuator.getNameAndId()), Boolean.TRUE);
        Logger.getInstance().log(new LMessage(String.join("", Collections.nCopies(header.length() - 1, "-"))), Boolean.TRUE);
        Logger.getInstance().log(new LMessage(header), Boolean.TRUE);
        Logger.getInstance().log(new LMessage(String.join("", Collections.nCopies(header.length() - 1, "-"))), Boolean.TRUE);
        Logger.getInstance().log(new LMessage(row), Boolean.TRUE);
        Logger.getInstance().log(new LMessage(String.join("", Collections.nCopies(header.length() - 1, "-"))), Boolean.TRUE);
    }

    private String formatCell(Object value, Integer offset) {
        String stringValue;

        if (Router.getConfig() == null) {
            stringValue = value.toString();
        } else {
            if (value.toString().length() < Router.getConfig().getCols() / NUMBER_OF_COLS - offset) {
                stringValue = value.toString().substring(0, value.toString().length());
            } else {
                stringValue = value.toString().substring(0, Router.getConfig().getCols() / NUMBER_OF_COLS - offset);
            }

        }

        String padding = String.join("", Collections.nCopies(Router.getConfig().getCols() / NUMBER_OF_COLS - offset - stringValue.length(), " "));

        try {
            Float.parseFloat(value.toString());

            return padding + stringValue;
        } catch (NumberFormatException ex) {
            return stringValue + padding;
        }
    }

    private String formatHeader(Object value, Integer offset) {
        String padding = String.join("", Collections.nCopies(Router.getConfig().getCols() / NUMBER_OF_COLS - String.valueOf(value).length() - offset, " "));

        return value.toString() + padding;
    }
}
