/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

import hr.foi.uzdiz.antbaric.zadaca.Worker;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import hr.foi.uzdiz.antbaric.zadaca.views.ActuatorView;
import hr.foi.uzdiz.antbaric.zadaca.views.PlaceView;
import hr.foi.uzdiz.antbaric.zadaca.views.SensorView;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author javert
 */
public class PlaceController extends Controller<PlaceView, Integer> {

    private SensorView sensorView;

    private ActuatorView actuatorView;

    public PlaceController(PlaceView view, Integer model) {
        super(view, model);
    }

    public void setSensorView(SensorView sensorView) {
        this.sensorView = sensorView;
    }

    public void setActuatorView(ActuatorView actuatorView) {
        this.actuatorView = actuatorView;
    }

    @Override
    public void init() {
        super.init();

        Boolean exists = false;

        for (Map.Entry<Integer, Place> entry : Worker.getInstance().PLACES.entrySet()) {
            if (Objects.equals(entry.getValue().id, this.model)) {
                this.view.prepareTable(entry.getValue());

                if (this.actuatorView != null) {
                    if (entry.getValue().getActuators().entrySet().size() > 0) {
                        Logger.getInstance().log(new LMessage("Actuators for " + entry.getValue().getNameAndId() + ": "), true);
                    }

                    for (Map.Entry<Integer, Device> device : entry.getValue().getActuators().entrySet()) {
                        this.actuatorView.prepareTable((Actuator) device.getValue());
                    }
                }

                if (this.sensorView != null) {
                    if (entry.getValue().getSensors().entrySet().size() > 0) {
                        Logger.getInstance().log(new LMessage("Sensors for " + entry.getValue().getNameAndId() + ": "), true);
                    }

                    for (Map.Entry<Integer, Device> device : entry.getValue().getSensors().entrySet()) {
                        this.sensorView.prepareTable((Sensor) device.getValue());
                    }
                }

                exists = true;
                break;
            }

        }

        if (!exists) {
            Logger.getInstance().log(new LError("Place with that ID does not exist!"), true);
        }
    }

}
