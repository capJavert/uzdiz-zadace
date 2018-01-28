/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.extensions;

import hr.foi.uzdiz.antbaric.zadaca.Router;

/**
 *
 * @author javert
 */
public abstract class LogElement {

    private final String text;

    public LogElement(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        if (Router.getConfig() == null) {
            return text;
        } else {
            if (text == null) {
                return "Unknown Error";
            } else {
                if (text.length() < Router.getConfig().getCols()) {
                    return text.substring(0, text.length());
                } else {
                    return text.substring(0, Router.getConfig().getCols());
                }
            }

        }

    }

    public abstract void accept(LogElementVisitor visitor);
}
