/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.views;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.extensions.PimpMyLogVisitor;
import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;

/**
 *
 * @author javert
 */
public abstract class View {
    private final PimpMyLogVisitor visitor = new PimpMyLogVisitor();
    
    public void printContent() {  
        ANSIHelper.cls();
        ANSIHelper.move(0, 0);
    }
    
    abstract void printCommands();
    
    protected Integer getRowsDiff() {
        return Router.getConfig().getRows()-Router.getConfig().getRowsForCommands();
    }
    
    protected void printSeparator() {
        for (int i=0,j=this.getRowsDiff()-1;i<Router.getConfig().getCols();i++) {
            ANSIHelper.write("-", i, j);
        }
    }
}
