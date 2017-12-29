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
import hr.foi.uzdiz.antbaric.zadaca.models.AlgorithmEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import hr.foi.uzdiz.antbaric.zadaca.views.ActuatorView;
import hr.foi.uzdiz.antbaric.zadaca.views.SensorView;
import java.util.Objects;

/**
 *
 * @author javert
 */
public class ActuatorController extends Controller<ActuatorView, Integer> {

    private SensorView sensorView;

    public ActuatorController(ActuatorView view, Integer model) {
        super(view, model);
    }

    public void setSensorView(SensorView sensorView) {
        this.sensorView = sensorView;
    }

    @Override
    public void init() {
        super.init();

        Boolean exists = false;

        for (final UIterator<UEntry<String, Place>> iterator = Worker.PLACES.getIterator(AlgorithmEnum.INDEX); iterator.hasNext();) {
            final UEntry<String, Place> entry = iterator.next();

            for (Device device : entry.getValue().getActuators()) {
                if (Objects.equals(device.getId(), this.model)) {
                    this.view.prepareTable((Actuator) device);

                    if (this.sensorView != null) {
                        if (((Actuator) device).sensors.size() > 0) {
                            Logger.getInstance().log(new LMessage("Sensors for " + device.getNameAndId() + ": "), true);
                        }

                        for (Sensor sensor : ((Actuator) device).sensors) {
                            this.sensorView.prepareTable(sensor);
                        }
                    }

                    exists = true;
                    break;
                }
            }

            for (Device device : entry.getValue().ACTUATORS_TRASH) {
                if (Objects.equals(device.getId(), this.model)) {
                    this.view.prepareTable((Actuator) device);

                    if (this.sensorView != null) {
                        if (((Actuator) device).sensors.size() > 0) {
                            Logger.getInstance().log(new LMessage("Sensors for " + device.getNameAndId() + ": "), true);
                        }

                        for (Sensor sensor : ((Actuator) device).sensors) {
                            this.sensorView.prepareTable(sensor);
                        }
                    }

                    exists = true;
                    break;
                }
            }
        }

        if (!exists) {
            Logger.getInstance().log(new LError("Actuator with that ID does not exist!"), true);
        }
    }

}
