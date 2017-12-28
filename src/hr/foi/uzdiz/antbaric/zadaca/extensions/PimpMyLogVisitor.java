/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.extensions;

import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;
import hr.foi.uzdiz.antbaric.zadaca.models.ANSIColor;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LInfo;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.LNotification;
import hr.foi.uzdiz.antbaric.zadaca.models.LWarning;

/**
 *
 * @author javert
 */
public class PimpMyLogVisitor implements LogElementVisitor {

    @Override
    public void visit(LError error) {
        ANSIHelper.setFg(ANSIColor.RED);
        ANSIHelper.write(error.toString());
        this.reset();
    }

    @Override
    public void visit(LWarning warning) {
        ANSIHelper.setFg(ANSIColor.YELLOW);
        ANSIHelper.write(warning.toString());
        ANSIHelper.setFg(ANSIColor.WHITE);
    }

    @Override
    public void visit(LMessage message) {
        ANSIHelper.write(message.toString());
    }

    @Override
    public void visit(LInfo info) {
        ANSIHelper.setFg(ANSIColor.MAGENTA);
        ANSIHelper.write(info.toString());
        this.reset();
    }

    @Override
    public void visit(LNotification notification) {
        ANSIHelper.setFg(ANSIColor.GREEN);
        ANSIHelper.write(notification.toString());
        this.reset();
    }

    private void reset() {
        ANSIHelper.setFg(ANSIColor.WHITE);
    }

}
