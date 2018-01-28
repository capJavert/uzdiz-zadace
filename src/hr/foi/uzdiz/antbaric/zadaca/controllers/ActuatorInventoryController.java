/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.Worker;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.DeviceInventoryItem;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.views.ActuatorInventoryView;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author javert
 */
public class ActuatorInventoryController extends Controller<ActuatorInventoryView, Integer> {

    public ActuatorInventoryController(ActuatorInventoryView view, Integer model) {
        super(view, model);
    }

    @Override
    public void init() {
        super.init();

        Boolean exists = false;

        for (Map.Entry<Integer, DeviceInventoryItem> entry : Worker.getInstance().ACTUATOR_INVENTORY.entrySet()) {
            if (Objects.equals(entry.getKey(), this.model)) {
                Logger.getInstance().log(new LMessage("\n"), true);

                Device device;
                if ((device = Worker.getInstance().ACTUATORS.get(this.model)) != null) {
                    Logger.getInstance().log(new LMessage(device.getNameAndId()), true);
                    
                    Logger.getInstance().log(new LMessage("  Trenutno dostupno: " + entry.getValue().availableCount + " / " + Router.getConfig().getkMax()), true);
                    Logger.getInstance().log(new LMessage("  Trenutni broj: " + entry.getValue().count), true);
                    Logger.getInstance().log(new LMessage("  Broj zamjena: " + entry.getValue().getReplaceCount()), true);
                    Logger.getInstance().log(new LMessage("  Broj pokvarenih: " + entry.getValue().failedCount.size()), true);
                    Logger.getInstance().log(new LMessage("  Broj nabavljanja: " + entry.getValue().getOrders()), true);
                    
                    exists = true;
                }

            }

            break;
        }

        if (!exists) {
            Logger.getInstance().log(new LError("Actuator model with that ID does not exist!"), true);
        }
    }

}
