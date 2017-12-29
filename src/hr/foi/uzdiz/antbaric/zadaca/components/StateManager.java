/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.components;

/**
 *
 * @author javert
 */
public class StateManager {
    private Object savedState;

    public void addState(Object state) {
        this.savedState = state;
    }

    public Object getState() {
        return this.savedState;
    }
    
    public Boolean hasSave() {
        return this.savedState != null;
    }
}
