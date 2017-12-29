/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

import hr.foi.uzdiz.antbaric.zadaca.Worker;
import hr.foi.uzdiz.antbaric.zadaca.views.WorkerView;

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
        super.init();

        final Worker worker = Worker.getInstance(this.model);
        worker.run();
    }
}
