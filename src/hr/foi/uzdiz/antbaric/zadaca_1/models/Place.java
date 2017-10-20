/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javert
 */
public class Place {
    private final String name;
    private final Integer category;
    private final Integer actuatorsNum;
    private final Integer sensorsNum;
    private final List<Actuator> actuators;
    private final List<Sensor> sensors;
    
    public Place(String name, Integer category, Integer sensorsNum, Integer actuatorsNum) {
        this.name = name;
        this.category = category;
        this.sensorsNum = sensorsNum;
        this.actuatorsNum = actuatorsNum;
        this.actuators = new ArrayList();
        this.sensors = new ArrayList();
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

    public List<Actuator> getActuators() {
        return actuators;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }
    
    public void addSensor(Sensor sensor) {
        this.sensors.add(sensor);
    }
    
    public void addActuator(Actuator actuator) {
        this.actuators.add(actuator);
    }
    
}
