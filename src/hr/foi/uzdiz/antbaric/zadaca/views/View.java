/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.views;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.extensions.PimpMyLogVisitor;
import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.ANSIColor;

/**
 *
 * @author javert
 */
public abstract class View {

    protected final PimpMyLogVisitor visitor = new PimpMyLogVisitor();

    public void printContent() {
        ANSIHelper.cls();
    }

    public void printCommands() {
        Integer y = this.getRowsDiff();
        ANSIHelper.move(0, y);
        ANSIHelper.cleol();

        for (String command : Logger.getCOMMANDS(Router.getConfig().getRowsForCommands())) {
            ANSIHelper.write(command, 0, y++);
        }

        ANSIHelper.move(0, y);
    }

    public void printCommandsWithPrompt() {
        Integer y = this.getRowsDiff();

        for (String command : Logger.getCOMMANDS(Router.getConfig().getRowsForCommands())) {
            ANSIHelper.write(command, 0, y++);
        }

        ANSIHelper.write("Press n/N to continue...", 0, y);
    }

    protected Integer getRowsDiff() {
        return Router.getConfig().getRows() - Router.getConfig().getRowsForCommands();
    }

    protected void printSeparator() {
        for (int i = 0, j = this.getRowsDiff() - 1; i < Router.getConfig().getCols(); i++) {
            ANSIHelper.setBg(ANSIColor.WHITE);
            ANSIHelper.write(" ", i, j);
            ANSIHelper.setBg(ANSIColor.BLACK);
        }
    }
}
