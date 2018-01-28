/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca;

import hr.foi.uzdiz.antbaric.zadaca.components.StateManager;
import hr.foi.uzdiz.antbaric.zadaca.controllers.ActuatorController;
import hr.foi.uzdiz.antbaric.zadaca.controllers.ActuatorInventoryController;
import hr.foi.uzdiz.antbaric.zadaca.controllers.GameController;
import hr.foi.uzdiz.antbaric.zadaca.controllers.IndexController;
import hr.foi.uzdiz.antbaric.zadaca.controllers.PlaceController;
import hr.foi.uzdiz.antbaric.zadaca.controllers.PlaceStructureController;
import hr.foi.uzdiz.antbaric.zadaca.controllers.SensorController;
import hr.foi.uzdiz.antbaric.zadaca.controllers.SensorInventoryController;
import hr.foi.uzdiz.antbaric.zadaca.controllers.WorkerController;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LInfo;
import hr.foi.uzdiz.antbaric.zadaca.models.LNotification;
import hr.foi.uzdiz.antbaric.zadaca.views.ActuatorInventoryView;
import hr.foi.uzdiz.antbaric.zadaca.views.ActuatorView;
import hr.foi.uzdiz.antbaric.zadaca.views.IndexView;
import hr.foi.uzdiz.antbaric.zadaca.views.MazeView;
import hr.foi.uzdiz.antbaric.zadaca.views.PlaceStructureView;
import hr.foi.uzdiz.antbaric.zadaca.views.PlaceView;
import hr.foi.uzdiz.antbaric.zadaca.views.SensorInventoryView;
import hr.foi.uzdiz.antbaric.zadaca.views.SensorView;
import hr.foi.uzdiz.antbaric.zadaca.views.WorkerView;

/**
 *
 * @author javert
 */
public class Router extends StateManager {

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
        INSTANCE.indexController.init();
    }

    public void goTo(String viewId) {
        String[] meta = viewId.split(" ");

        switch (meta[0]) {
            case "SP":
                viewId = "";
                this.addState(Worker.getInstance().save());
                Logger.getInstance().log(new LNotification("State saved successfully"), Boolean.TRUE);
                break;
            case "VP":
                viewId = "";
                if (this.hasSave()) {
                    Worker.getInstance().restore(this.getState());
                    Logger.getInstance().log(new LNotification("State restored successfully"), Boolean.TRUE);
                } else {
                    Logger.getInstance().log(new LError("There is no saved state to restore!"), Boolean.TRUE);
                }
                break;
            case "VF":
                MazeView mazeView = new MazeView();
                GameController gameController = new GameController(mazeView, 0);
                gameController.init();
                break;
            case "PI":
                viewId = "";

                try {
                    if (Integer.parseInt(meta[1]) > 100 || Integer.parseInt(meta[1]) < 0) {
                        throw new NumberFormatException();
                    }

                    Router.CONFIG.setDevicePerishability(Integer.parseInt(meta[1]));
                    Logger.getInstance().log(new LNotification("PI % is set to " + Router.CONFIG.getDevicePerishability()), Boolean.TRUE);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    Logger.getInstance().log(new LError("PI % must be a number (0-100)"), Boolean.TRUE);
                }
                break;
            case "M":
                if (meta.length > 1) {
                    try {
                        PlaceView view = new PlaceView();
                        PlaceController controller = new PlaceController(view, Integer.parseInt(meta[1]));
                        controller.setSensorView(new SensorView());
                        controller.setActuatorView(new ActuatorView());
                        controller.init();
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                        Logger.getInstance().log(new LError("Actuator ID must be a number"), Boolean.TRUE);
                    }
                } else {
                    viewId = "";
                }
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
                if (meta.length > 1) {
                    try {
                        ActuatorView view = new ActuatorView();
                        ActuatorController controller = new ActuatorController(view, Integer.parseInt(meta[1]));
                        controller.setSensorView(new SensorView());
                        controller.init();
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                        Logger.getInstance().log(new LError("Actuator ID must be a number"), Boolean.TRUE);
                    }
                } else {
                    viewId = "";
                }
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
            case "H":
                viewId = "";

                for (String line : this.help.split("\\r?\\n")) {
                    Logger.getInstance().log(new LInfo(line), Boolean.TRUE);
                }
                break;
            case "SM":
                if (meta.length > 1) {
                    PlaceStructureView view = new PlaceStructureView();
                    PlaceStructureController controller = new PlaceStructureController(view, Integer.parseInt(meta[1]));
                    controller.init();
                } else {
                    viewId = "";
                }
                break;
            case "TS":
                if (meta.length > 1) {
                    SensorInventoryView view = new SensorInventoryView();
                    SensorInventoryController controller = new SensorInventoryController(view, Integer.parseInt(meta[1]));
                    controller.init();
                } else {
                    viewId = "";
                }
                break;
            case "TA":
                if (meta.length > 1) {
                    ActuatorInventoryView view = new ActuatorInventoryView();
                    ActuatorInventoryController controller = new ActuatorInventoryController(view, Integer.parseInt(meta[1]));
                    controller.init();
                } else {
                    viewId = "";
                }
                break;
            case "CP":
                try {
                    if (Integer.parseInt(meta[1]) > 99 || Integer.parseInt(meta[1]) < 1) {
                        throw new NumberFormatException();
                    }

                    Router.CONFIG.setDeviceFixInterval(Integer.parseInt(meta[1]));
                    Logger.getInstance().log(new LNotification("CP is set to " + Router.CONFIG.getDeviceFixInterval()), Boolean.TRUE);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    Logger.getInstance().log(new LError("CP n must be a number (1-99)"), Boolean.TRUE);
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

    private final String help = "M x - ispis podataka mjesta x\n"
            + "\n"
            + "S x - ispis podataka senzora x\n"
            + "\n"
            + "A x - ispis podataka aktuatora x\n"
            + "\n"
            + "S - ispis statistike\n"
            + "\n"
            + "SP - spremi podatke (mjesta, uređaja)\n"
            + "\n"
            + "VP - vrati spremljene podatke (mjesta, uređaja)\n"
            + "\n"
            + "C n - izvršavanje n ciklusa dretve (1-100)\n"
            + "\n"
            + "VF [argumenti] - izvršavanje vlastite funkcionalnosti, po potrebni mogući su argumenti\n"
            + "\n"
            + "PI n - prosječni % ispravnosti uređaja (0-100)\n"
            + "\n"
            + "H - pomoć, ispis dopuštenih komandi i njihov opis\n"
            + "\n"
            + "I - izlaz.";
}
