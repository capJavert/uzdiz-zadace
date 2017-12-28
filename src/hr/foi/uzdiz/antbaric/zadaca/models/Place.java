/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javert
 */
public class Place {

    private Integer id = null;
    private final String name;
    private final Integer category;
    private final Integer actuatorsNum;
    private final Integer sensorsNum;
    private final List<Device> actuators;
    private final List<Device> sensors;

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
