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
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import java.lang.reflect.Field;
import java.util.Collections;

/**
 *
 * @author javert
 */
public class SensorView extends View {

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

    public void prepareTable(Sensor sensor) {
        String header = "";
        String row = "";

        for (Field field : Device.class.getFields()) {
            try {
                if (!header.isEmpty()) {
                    header += this.formatCell(field.getName(), 3) + " | ";
                } else {
                    header += "| " + this.formatCell(field.getName(), 5) + " | ";
                }

                if (!row.isEmpty()) {
                    row += this.formatCell(field.get(sensor), 3) + " | ";
                } else {
                    row += "| " + this.formatCell(field.get(sensor), 5) + " | ";
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                // error probs wont happend
            }
        }

        Logger.getInstance().log(new LMessage(String.join("", Collections.nCopies(header.length() - 1, "-"))), Boolean.TRUE);
        Logger.getInstance().log(new LMessage(header), Boolean.TRUE);
        Logger.getInstance().log(new LMessage(String.join("", Collections.nCopies(header.length() - 1, "-"))), Boolean.TRUE);
        Logger.getInstance().log(new LMessage(row), Boolean.TRUE);
        Logger.getInstance().log(new LMessage(String.join("", Collections.nCopies(header.length() - 1, "-"))), Boolean.TRUE);
    }
    
    private String formatCell(Object value, Integer offset) {
        String padding = String.join("", Collections.nCopies(Router.getConfig().getCols() / Device.class.getFields().length - String.valueOf(value).length() - offset, " "));
        
        try {
            Float.parseFloat(value.toString());
            
            return padding + value.toString();
        } catch (NumberFormatException ex) {
            return value.toString() + padding;
        } 
    }
}
