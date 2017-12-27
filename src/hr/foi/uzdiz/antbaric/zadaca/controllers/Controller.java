/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

/**
 *
 * @author javert
 */
public abstract class Controller<T, E> {
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
    
    abstract void init();

    abstract void update();
}
