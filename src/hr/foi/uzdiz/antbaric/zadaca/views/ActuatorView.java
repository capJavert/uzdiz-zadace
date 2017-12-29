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
                    header_part = this.formatHeader(field.getName(), 3) + " | ";
                } else {
                    header_part = "| " + this.formatHeader(field.getName(), 5) + " | ";
                }

                if (!row.isEmpty()) {
                    row_part = this.formatCell(field.get(actuator), 3) + " | ";
                } else {
                    row_part = "| " + this.formatCell(field.get(actuator), 5) + " | ";
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
            if (value.toString().length() < Router.getConfig().getCols() / 8 - offset) {
                stringValue = value.toString().substring(0, value.toString().length());
            } else {
                stringValue = value.toString().substring(0, Router.getConfig().getCols() / 8 - offset);
            }

        }

        String padding = String.join("", Collections.nCopies(Router.getConfig().getCols() / 8 - offset - stringValue.length(), " "));

        try {
            Float.parseFloat(value.toString());

            return padding + stringValue;
        } catch (NumberFormatException ex) {
            return stringValue + padding;
        }
    }

    private String formatHeader(Object value, Integer offset) {
        String padding = String.join("", Collections.nCopies(Router.getConfig().getCols() / 8 - String.valueOf(value).length() - offset, " "));

        return value.toString() + padding;
    }
}
