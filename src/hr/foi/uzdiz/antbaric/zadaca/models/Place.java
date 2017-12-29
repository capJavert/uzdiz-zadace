/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javert
 */
public class Place implements Serializable {

    public Integer id = null;
    public final String name;
    public final Integer category;
    public final Integer actuatorsNum;
    public final Integer sensorsNum;
    private final List<Device> actuators;
    private final List<Device> sensors;

    public final List<Device> SENSORS_TRASH = new ArrayList<>();
    public final List<Device> ACTUATORS_TRASH = new ArrayList<>();

    public Place(String name, Integer category, Integer sensorsNum, Integer actuatorsNum) {
        this.name = name;
        this.category = category;
        this.sensorsNum = sensorsNum;
        this.actuatorsNum = actuatorsNum;
        this.actuators = new ArrayList<>();
        this.sensors = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId() {
        if (this.id == null) {
            this.id = Generator.getInstance().getUniqueIdentifier("PLACE");
        }
    }

    public String getName() {
        return name;
    }

    public String getNameAndId() {
        return this.name + " ID: " + this.id;
    }

    public Integer getCategory() {
        return category;
    }

    public Integer getActuatorsNum() {
        return actuatorsNum;
    }

    public Integer getSensorsNum() {
        return sensorsNum;
    }

    public List<Device> getActuators() {
        return actuators;
    }

    public List<Device> getSensors() {
        return sensors;
    }

    public void addSensor(Device sensor) {
        sensor.setId();

        this.sensors.add(sensor);
    }

    public void addActuator(Device actuator) {
        actuator.setId();

        this.actuators.add(actuator);
    }

}
