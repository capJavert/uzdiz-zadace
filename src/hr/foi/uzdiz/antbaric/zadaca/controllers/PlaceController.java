/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

import hr.foi.uzdiz.antbaric.zadaca.Worker;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.iterators.UEntry;
import hr.foi.uzdiz.antbaric.zadaca.iterators.UIterator;
import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca.models.AlgorithmEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import hr.foi.uzdiz.antbaric.zadaca.views.ActuatorView;
import hr.foi.uzdiz.antbaric.zadaca.views.PlaceView;
import hr.foi.uzdiz.antbaric.zadaca.views.SensorView;
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

        for (final UIterator<UEntry<String, Place>> iterator = Worker.PLACES.getIterator(AlgorithmEnum.INDEX); iterator.hasNext();) {
            final UEntry<String, Place> entry = iterator.next();

            if (Objects.equals(entry.getValue().getId(), this.model)) {
                this.view.prepareTable(entry.getValue());

                if (this.actuatorView != null) {
                    Logger.getInstance().log(new LMessage("Actuators for " + entry.getValue().getNameAndId() + ": "), true);

                    for (Device device : entry.getValue().getActuators()) {
                        this.actuatorView.prepareTable((Actuator) device);
                    }

                    for (Device device : entry.getValue().ACTUATORS_TRASH) {
                        this.actuatorView.prepareTable((Actuator) device);
                    }
                }

                if (this.sensorView != null) {
                    Logger.getInstance().log(new LMessage("Sensors for " + entry.getValue().getNameAndId() + ": "), true);

                    for (Device device : entry.getValue().getSensors()) {
                        this.sensorView.prepareTable((Sensor) device);
                    }

                    for (Device device : entry.getValue().SENSORS_TRASH) {
                        this.sensorView.prepareTable((Sensor) device);
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
