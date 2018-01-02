/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.components;

import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import java.util.List;

/**
 *
 * @author javert
 */
public class SensorFactory extends DeviceFactory {

    @Override
    public Sensor createToF(List<String> values) throws Exception {
        if(values.get(6) == null) {
            values.set(6, "");
        }
        
        return new Sensor(
                Integer.parseInt(values.get(0)),
                values.get(1), 
                Integer.parseInt(values.get(2)), 
                Integer.parseInt(values.get(3)), 
                Double.parseDouble(values.get(4)), 
                Double.parseDouble(values.get(5)), 
                values.get(6)
        );
    }
    
}
