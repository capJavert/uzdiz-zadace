/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.components;

import hr.foi.uzdiz.antbaric.zadaca_1.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Device;
import java.util.List;

/**
 *
 * @author javert
 */
public class ActuatorFactory extends DeviceFactory {

    @Override
    public Device createToF(List<String> values) {
        if (values.get(5) == null) {
            values.set(5, "");
        }
        
        return new Actuator(
                values.get(0),
                Integer.parseInt(values.get(1)),
                Integer.parseInt(values.get(2)),
                Double.parseDouble(values.get(3)),
                Double.parseDouble(values.get(4)),
                values.get(5)
        );
    }

}
