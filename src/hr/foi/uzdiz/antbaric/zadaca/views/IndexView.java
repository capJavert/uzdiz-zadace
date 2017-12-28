package hr.foi.uzdiz.antbaric.zadaca.views;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.extensions.LogElement;
import hr.foi.uzdiz.antbaric.zadaca.extensions.PimpMyLogVisitor;
import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author javert
 */
public class IndexView extends View {
    private final PimpMyLogVisitor visitor = new PimpMyLogVisitor();
    
    @Override
    public void printContent() {
        super.printContent();

        Integer x = 0;
        Integer y = 0;
        
        for (LogElement log : Logger.getLOG(this.getRowsDiff()-2)) {
            ANSIHelper.move(x, y++);
            log.accept(this.visitor);
        }
        
        this.printSeparator();
    }
    
    @Override
    public void printCommands() {
        Integer x = 0;
        Integer y = this.getRowsDiff();
        
        for (String command : Logger.getCOMMANDS(Router.getConfig().getRowsForCommands())) {
            ANSIHelper.write(command, x, y++);
        }
        
        ANSIHelper.write("_", x, y);
        ANSIHelper.move(x+1, y);
    }
}
