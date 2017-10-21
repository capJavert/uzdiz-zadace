/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.components;

import hr.foi.uzdiz.antbaric.zadaca_1.models.Device;
import hr.foi.uzdiz.antbaric.zadaca_1.models.DeviceEnum;
import java.util.List;

/**
 *
 * @author javert
 */
abstract class DeviceFactory {
    public static DeviceFactory getFactory(DeviceEnum tofType) {
        switch(tofType) {
            case SENSOR: 
                return new SensorFactory();
            case ACTUATOR: 
                return new ActuatorFactory();
            default:
                return null;
        }
    }
    
    public abstract Device createToF(List<String> values);
}
