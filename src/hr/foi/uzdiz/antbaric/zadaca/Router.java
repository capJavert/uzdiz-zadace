/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca;

import hr.foi.uzdiz.antbaric.zadaca.controllers.IndexController;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.views.IndexView;

/**
 *
 * @author javert
 */
public class Router {

    private static volatile Router INSTANCE;
    private static Configuration CONFIG = null;
    private String current = "";

    static {
        INSTANCE = new Router();
    }

    private Router() {
    }

    public static Router getInstance() {
        return INSTANCE;
    }

    public static Configuration getConfig() {
        return CONFIG;
    }

    public static void setConfig(Configuration config) {
        Router.CONFIG = config;
    }

    public void goTo(String viewId) {
        switch (viewId) {
            default:
                viewId = "";
                IndexView view = new IndexView();
                IndexController controller = new IndexController(view, null);
                controller.init();
        }
        
        if (!viewId.equals("")) {
            this.current = viewId;
        }
    }
}
