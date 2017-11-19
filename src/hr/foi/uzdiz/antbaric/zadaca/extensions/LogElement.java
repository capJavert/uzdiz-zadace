/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.extensions;

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
        return text;
    }
    
    public abstract void accept(LogElementVisitor visitor);
}
