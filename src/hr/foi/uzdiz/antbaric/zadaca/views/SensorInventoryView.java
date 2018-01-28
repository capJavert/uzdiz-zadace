/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.views;

import hr.foi.uzdiz.antbaric.zadaca.extensions.LogElement;
import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;

/**
 *
 * @author javert
 */
public class SensorInventoryView extends View {
    
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
    
}
