/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca;

import hr.foi.uzdiz.antbaric.zadaca.controllers.IndexController;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LInfo;
import hr.foi.uzdiz.antbaric.zadaca.models.LNotification;
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
        String[] meta = viewId.split(" ");
        
        switch (meta[0]) {
            case "SP":
                break;
            case "VP":
                break;
            case "VF":
                break;
            case "PI":
                try {
                    if (Integer.parseInt(meta[1]) > 100 || Integer.parseInt(meta[1]) < 0) {
                        throw new NumberFormatException();
                    }
                    
                    Router.CONFIG.setDevicePerishability(Integer.parseInt(meta[1])); 
                    Logger.getInstance().log(new LNotification("PI % is set to " + meta[1]), Boolean.TRUE);
                } catch(NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    Logger.getInstance().log(new LError("PI % must be a number (0-100)"), Boolean.TRUE);
                }
                
                break;
            case "M":
                break;
            case "S":
                if (meta.length > 1) {
                    // sensor x table
                } else {
                    Logger.getInstance().log(new LInfo("You won't find anything here, carry on..."), Boolean.TRUE);
                }
                break;
            case "A":
                break;
            case "C":
                break;
            default:
                viewId = "";
                IndexView view = new IndexView();
                IndexController controller = new IndexController(view, null);
                controller.init();
        }
        
        IndexView view = new IndexView();
        IndexController controller = new IndexController(view, null);
        controller.init();

        if (!viewId.equals("")) {
            this.current = viewId;
        }
    }
}
