package hr.foi.uzdiz.antbaric.zadaca.views;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author javert
 */
public class IndexView {

    public void print() {  
        System.out.println("Hello world");
               
        for (int i = 1, j = 0; i < Router.getConfig().getRows(); i++, j++) {
            ANSIHelper.write("#", i, j);
        }
    }
}
