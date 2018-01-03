/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

import hr.foi.uzdiz.antbaric.zadaca.Worker;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import hr.foi.uzdiz.antbaric.zadaca.views.SensorView;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author javert
 */
public class SensorController extends Controller<SensorView, Integer> {

    public SensorController(SensorView view, Integer model) {
        super(view, model);
    }

    @Override
    public void init() {
        super.init();

        Boolean exists = false;

        for (Map.Entry<Integer, Place> entry : Worker.getInstance().PLACES.entrySet()) {
            for (Map.Entry<Integer, Device> device : entry.getValue().getSensors().entrySet()) {
                if (Objects.equals(device.getValue().id, this.model)) {
                    this.view.prepareTable((Sensor) device.getValue());
                    exists = true;
                    break;
                }
            }

            for (Device device : entry.getValue().SENSORS_TRASH) {
                if (Objects.equals(device.id, this.model)) {
                    this.view.prepareTable((Sensor) device);
                    exists = true;
                    break;
                }
            }
        }

        if (!exists) {
            Logger.getInstance().log(new LError("Sensor with that ID does not exist!"), true);
        }
    }
}
