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
        if (values.get(1).equals("")
                || values.get(1) == null
                || Integer.parseInt(values.get(2)) < 0
                || Integer.parseInt(values.get(2)) > 2
                || Integer.parseInt(values.get(3)) < 0
                || Integer.parseInt(values.get(3)) > 3) {

            throw new Exception();
        }

        if (values.get(6) == null) {
            values.set(6, "");
        }

        return new Actuator(
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
