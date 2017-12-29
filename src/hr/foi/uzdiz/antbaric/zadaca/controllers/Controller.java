/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.views.View;
import java.util.Scanner;

/**
 *
 * @author javert
 */
public abstract class Controller<T extends View, E> {

    protected T view;
    protected E model;

    public Controller(T view, E model) {
        this.view = view;
        this.model = model;
    }

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    public E getModel() {
        return model;
    }

    public void setModel(E model) {
        this.model = model;
    }

    public void init() {
        Logger.getInstance().setController(this);
    }

    public void update() {
        this.view.printContent();
        this.view.printCommands();
    }
    
    public void prompt() {
        this.view.printContent();
        this.view.printCommandsWithPrompt();

        String command;
        
        // TODO uncomment before prod
        /*do {
            command = new Scanner(System.in).nextLine();
            ANSIHelper.move(25, Router.getConfig().getRows());
        } while (!command.equals("n") && !command.equals("N"));*/
        
        Logger.getInstance().emptyBuffer();
    }
}
