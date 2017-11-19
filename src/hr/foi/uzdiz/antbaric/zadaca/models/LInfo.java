/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

import hr.foi.uzdiz.antbaric.zadaca.extensions.LogElement;
import hr.foi.uzdiz.antbaric.zadaca.extensions.LogElementVisitor;

/**
 *
 * @author javert
 */
public class LInfo extends LogElement {

    public LInfo(String text) {
        super(text);
    }

    @Override
    public void accept(LogElementVisitor visitor) {
        visitor.visit(this);
    }
}