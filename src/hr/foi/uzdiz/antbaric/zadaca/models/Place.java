/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
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
    private final ConcurrentHashMap<Integer, Device> actuators;
    private final ConcurrentHashMap<Integer, Device> sensors;

    public final List<Device> SENSORS_TRASH = new ArrayList<>();
    public final List<Device> ACTUATORS_TRASH = new ArrayList<>();

    public Place(String name, Integer category, Integer sensorsNum, Integer actuatorsNum) {
        this.name = name;
        this.category = category;
        this.sensorsNum = sensorsNum;
        this.actuatorsNum = actuatorsNum;
        this.actuators = new ConcurrentHashMap<>();
        this.sensors = new ConcurrentHashMap<>();
    }

    public Place(Integer id, String name, Integer category, Integer sensorsNum, Integer actuatorsNum) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.sensorsNum = sensorsNum;
        this.actuatorsNum = actuatorsNum;
        this.actuators = new ConcurrentHashMap<>();
        this.sensors = new ConcurrentHashMap<>();
    }

    public String getId() {
        return String.valueOf(id);
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

    public ConcurrentHashMap<Integer, Device> getActuators() {
        return actuators;
    }

    public ConcurrentHashMap<Integer, Device> getSensors() {
        return sensors;
    }

    public void addSensor(Device sensor) throws Exception {
        if (sensor.getType().equals(this.category) || sensor.getType().equals(2)) {
            if (this.sensors.size() < this.sensorsNum) {
                this.sensors.put(sensor.id, sensor);
            } else {
                throw new Exception("Place #" + this.getId() + " can hold " + this.sensorsNum + " sensors! Can't add device #" + sensor.id);
            }
        } else {
            throw new Exception("Device #" + sensor.id + " type doesn't match Place #" + this.getId() + " type!");
        }

    }

    public void addActuator(Device actuator) throws Exception {
        if (actuator.getType().equals(this.category) || actuator.getType().equals(2)) {
            if (this.actuators.size() < this.actuatorsNum) {
                this.actuators.put(actuator.id, actuator);
            } else {
                throw new Exception("Place #" + this.getId() + " can hold " + this.actuatorsNum + " actuators! Can't add device #" + actuator.id);
            }
        } else {
            throw new Exception("Device #" + actuator.id + " type doesn't match Place #" + this.getId() + " type!");
        }
    }

}
