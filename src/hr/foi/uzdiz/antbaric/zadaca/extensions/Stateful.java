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
abstract public class Stateful {

    abstract public Object save();

    abstract public void restore(Object state);
}
