/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca.views.IndexView;

/**
 *
 * @author javert
 */
public class IndexController extends Controller<IndexView, Actuator> {

    public IndexController(IndexView view, Actuator model) {
        super(view, model);
    }

    @Override
    public void init() {
        super.init();
    }
}
