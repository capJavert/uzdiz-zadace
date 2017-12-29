/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca;

import hr.foi.uzdiz.antbaric.zadaca.controllers.IndexController;
import hr.foi.uzdiz.antbaric.zadaca.controllers.SensorController;
import hr.foi.uzdiz.antbaric.zadaca.controllers.WorkerController;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LInfo;
import hr.foi.uzdiz.antbaric.zadaca.models.LNotification;
import hr.foi.uzdiz.antbaric.zadaca.views.IndexView;
import hr.foi.uzdiz.antbaric.zadaca.views.SensorView;
import hr.foi.uzdiz.antbaric.zadaca.views.WorkerView;

/**
 *
 * @author javert
 */
public class Router {

    private static volatile Router INSTANCE;
    private static Configuration CONFIG = null;
    private IndexController indexController;

    static {
        INSTANCE = new Router();
    }

    private Router() {
    }

    public static Router getInstance() {
        return INSTANCE;
    }

    public void setIndexController(IndexController indexController) {
        this.indexController = indexController;
    }

    public static Configuration getConfig() {
        return CONFIG;
    }

    public static void setConfig(Configuration config) {
        Router.CONFIG = config;

        IndexView view = new IndexView();
        IndexController controller = new IndexController(view, null);
        controller.init();
        INSTANCE.indexController = controller;
    }

    public void goTo(String viewId) {
        String[] meta = viewId.split(" ");

        switch (meta[0]) {
            case "SP":
                break;
            case "VP":
                break;
            case "VF":
                viewId = "";
                for (int i = 0; i < 1000; i++) {
                    Logger.getInstance().log(new LError("Output test #" + String.valueOf(i)), Boolean.TRUE);
                }
                break;
            case "PI":
                viewId = "";

                try {
                    if (Integer.parseInt(meta[1]) > 100 || Integer.parseInt(meta[1]) < 0) {
                        throw new NumberFormatException();
                    }

                    Router.CONFIG.setDevicePerishability(Integer.parseInt(meta[1]));
                    Logger.getInstance().log(new LNotification("PI % is set to " + meta[1]), Boolean.TRUE);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    Logger.getInstance().log(new LError("PI % must be a number (0-100)"), Boolean.TRUE);
                }
                break;
            case "M":
                break;
            case "S":
                if (meta.length > 1) {
                    try {
                        SensorView view = new SensorView();
                        SensorController controller = new SensorController(view, Integer.parseInt(meta[1]));
                        controller.init();
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                        Logger.getInstance().log(new LError("Sensor ID must be a number"), Boolean.TRUE);
                    }

                } else {
                    viewId = "";
                    Logger.getInstance().log(new LInfo("You won't find anything here, carry on..."), Boolean.TRUE);
                }
                break;
            case "A":
                break;
            case "C":
                try {
                    if (Integer.parseInt(meta[1]) > 100 || Integer.parseInt(meta[1]) < 1) {
                        throw new NumberFormatException();
                    }

                    WorkerView view = new WorkerView();
                    WorkerController controller = new WorkerController(view, Integer.parseInt(meta[1]));
                    controller.init();
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    Logger.getInstance().log(new LError("C n must be a number (1-100)"), Boolean.TRUE);
                }
                break;
            default:
                viewId = "";
        }

        if (viewId.equals("")) {
            this.indexController.init();
            this.indexController.update();
        }
    }
}
