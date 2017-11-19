/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

import hr.foi.uzdiz.antbaric.zadaca.components.Generator;
import hr.foi.uzdiz.antbaric.zadaca.components.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javert
 */
public class Actuator extends Device {

    public final List<Sensor> sensors = new ArrayList<>();
    
    public Actuator(String name, Integer category, Integer unitType, Double min, Double max, String comment) {
        super(name, category, unitType, min, max, comment);
    }

    @Override
    public void activate() {
        Double number;
        Integer direction;
        Generator generator = Generator.getInstance();

        switch (this.getUnitType()) {
            case 0:
                Logger.getInstance().log("Actuator '" + this.getName() + "'", true);
                Logger.getInstance().log("    MIN: " + generator.parseDecimalRound(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimalRound(this.getMax()) + this.getComment(), true);

                number = generator.fromInterval(this.getMin(), this.getMax()).doubleValue();
                Logger.getInstance().log("    Value: " + generator.parseDecimalRound(this.getValue()) + this.getComment() + ", Movement " + generator.parseDecimalRound(number) + this.getComment() + ":", true);
                direction = 1;
                while (number > 0) {
                    Double oldValue = this.getValue();
                    this.setValue(this.getValue() + number * direction);
                    Logger.getInstance().log("    " + generator.parseDecimalRound(oldValue) + this.getComment() + " -> " + generator.parseDecimalRound(this.getValue()) + this.getComment(), true);

                    if (oldValue + number * direction > this.getMax()) {
                        number = (oldValue.longValue() + number * direction) % this.getMax().longValue();
                        direction = -1;
                    } else {
                        number -= number;
                    }

                }
                break;
            case 1:
                Logger.getInstance().log("Actuator '" + this.getName() + "'", true);
                Logger.getInstance().log("    MIN: " + generator.parseDecimal1(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimal1(this.getMax()) + this.getComment(), true);

                number = generator.fromIntervalPrecision1(this.getMin(), this.getMax());
                Logger.getInstance().log("    Value: " + generator.parseDecimal1(this.getValue()) + this.getComment() + ", Movement " + generator.parseDecimal1(number) + this.getComment() + ":", true);
                direction = 1;
                while (number > 0) {
                    Double oldValue = this.getValue();
                    this.setValue(this.getValue() + number * direction);
                    Logger.getInstance().log("    " + generator.parseDecimal1(oldValue) + this.getComment() + " -> " + generator.parseDecimal1(this.getValue()) + this.getComment(), true);

                    if (oldValue + number * direction > this.getMax()) {
                        number = (oldValue + number * direction) % this.getMax();
                        direction = -1;
                    } else {
                        number -= number;
                    }

                }
                break;
            case 2:
                Logger.getInstance().log("Actuator '" + this.getName() + "'", true);
                Logger.getInstance().log("    MIN: " + generator.parseDecimal5(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimal5(this.getMax()) + this.getComment(), true);
                
                number = generator.fromIntervalPrecision5(this.getMin(), this.getMax());
                Logger.getInstance().log("    Value: " + generator.parseDecimal5(this.getValue()) + this.getComment() + ", Movement " + generator.parseDecimal5(number) + this.getComment() + ":", true);
                direction = 1;
                while (number > 0) {
                    Double oldValue = this.getValue();
                    this.setValue(this.getValue() + number * direction);
                    Logger.getInstance().log("    " + generator.parseDecimal5(oldValue) + this.getComment() + " -> " + generator.parseDecimal5(this.getValue()) + this.getComment(), true);

                    if (oldValue + number * direction > this.getMax()) {
                        number = (oldValue + number * direction) % this.getMax();
                        direction = -1;
                    } else {
                        number -= number;
                    }

                }
                break;
            case 3:
                Logger.getInstance().log("Actuator '" + this.getName() + "'", true);
                this.setValue((this.getValue() == 1 ? new Double(0) : new Double(1)));
                Logger.getInstance().log("    Value: " + (this.getValue() == 1 ? 0 : 1) + ", New Value " + generator.parseDecimalRound(this.getValue()) + this.getComment(), true);
                break;
        }
    }

    @Override
    public Device prototype() {
        Device actuator = new Actuator(this.getName(), this.getType(), this.getUnitType(), this.getMin(), this.getMax(), this.getComment());
        actuator.setId();

        return actuator;
    }

    @Override
    public void setId() {
        if (this.id == null) {
            this.id = Generator.getInstance().getUniqueIdentifier(DeviceEnum.ACTUATOR.toString());
        }
    }
    
    public Boolean isSensorChanged() {
        for(Sensor sensor : this.sensors) {
            if(sensor.isChanged()) {
                return true;
            }
        }
        
        return false;
    }

}
