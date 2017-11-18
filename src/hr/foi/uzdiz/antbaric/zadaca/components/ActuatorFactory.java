/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.components;

import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import java.util.List;

/**
 *
 * @author javert
 */
public class ActuatorFactory extends DeviceFactory {

    @Override
    public Device createToF(List<String> values) throws Exception {
        if (values.get(0).equals("")
                || values.get(0) == null
                || Integer.parseInt(values.get(1)) < 0
                || Integer.parseInt(values.get(1)) > 2
                || Integer.parseInt(values.get(2)) < 0
                || Integer.parseInt(values.get(2)) > 3) {

            throw new Exception();
        }

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
