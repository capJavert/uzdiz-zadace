/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.Worker;
import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.views.WorkerView;
import java.util.Scanner;

/**
 *
 * @author javert
 */
public class WorkerController extends Controller<WorkerView, Integer> {

    public WorkerController(WorkerView view, Integer model) {
        super(view, model);
    }

    @Override
    public void init() {
        Logger.getInstance().setController(this);
        
        final Worker worker = Worker.getInstance(this.model);
        worker.run();
    }

    @Override
    public void update() {
        this.view.printContent();
        this.view.printCommands();
    }

    @Override
    public void prompt() {
        this.view.printContent();
        this.view.printCommandsWithPrompt();

        String command;
        
        do {
            command = new Scanner(System.in).nextLine();
            ANSIHelper.move(25, Router.getConfig().getRows());
        } while (!command.equals("n") && !command.equals("N"));
        
        Logger.getInstance().emptyBuffer();
    }

}
