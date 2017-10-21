/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.models;

import hr.foi.uzdiz.antbaric.zadaca_1.components.Generator;

/**
 *
 * @author javert
 */
public class Actuator extends Device {

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
                System.out.println("Actuator '" + this.getName() + "'");
                System.out.println("    MIN: " + generator.parseDecimalRound(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimalRound(this.getMax()) + this.getComment());

                number = generator.fromInterval(this.getMin(), this.getMax()).doubleValue();
                System.out.println("    Value: " + generator.parseDecimalRound(this.getValue()) + this.getComment() + ", Movement " + generator.parseDecimalRound(number) + this.getComment() + ":");
                direction = 1;
                while (number > 0) {
                    Double oldValue = this.getValue();
                    this.setValue(this.getValue() + number * direction);
                    System.out.println("    " + generator.parseDecimalRound(oldValue) + this.getComment() + " -> " + generator.parseDecimalRound(this.getValue()) + this.getComment());

                    if (oldValue + number * direction > this.getMax()) {
                        number = (oldValue.longValue() + number * direction) % this.getMax().longValue();
                        direction = -1;
                    } else {
                        number -= number;
                    }

                }
                break;
            case 1:
                System.out.println("Actuator '" + this.getName() + "'");
                System.out.println("    MIN: " + generator.parseDecimal1(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimal1(this.getMax()) + this.getComment());

                number = generator.fromIntervalPrecision1(this.getMin(), this.getMax());
                System.out.println("    Value: " + generator.parseDecimal1(this.getValue()) + this.getComment() + ", Movement " + generator.parseDecimal1(number) + this.getComment() + ":");
                direction = 1;
                while (number > 0) {
                    Double oldValue = this.getValue();
                    this.setValue(this.getValue() + number * direction);
                    System.out.println("    " + generator.parseDecimal1(oldValue) + this.getComment() + " -> " + generator.parseDecimal1(this.getValue()) + this.getComment());

                    if (oldValue + number * direction > this.getMax()) {
                        number = (oldValue + number * direction) % this.getMax();
                        direction = -1;
                    } else {
                        number -= number;
                    }

                }
                break;
            case 2:
                System.out.println("Actuator '" + this.getName() + "'");
                System.out.println("    MIN: " + generator.parseDecimal5(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimal5(this.getMax()) + this.getComment());
                
                number = generator.fromIntervalPrecision5(this.getMin(), this.getMax());
                System.out.println("    Value: " + generator.parseDecimal5(this.getValue()) + this.getComment() + ", Movement " + generator.parseDecimal5(number) + this.getComment() + ":");
                direction = 1;
                while (number > 0) {
                    Double oldValue = this.getValue();
                    this.setValue(this.getValue() + number * direction);
                    System.out.println("    " + generator.parseDecimal5(oldValue) + this.getComment() + " -> " + generator.parseDecimal5(this.getValue()) + this.getComment());

                    if (oldValue + number * direction > this.getMax()) {
                        number = (oldValue + number * direction) % this.getMax();
                        direction = -1;
                    } else {
                        number -= number;
                    }

                }
                break;
            case 3:
                this.setValue(this.getValue() * -1);
                System.out.println("    Value: " + generator.parseDecimalRound(this.getValue() * -1) + ", New Value " + generator.parseDecimalRound(this.getValue()) + this.getComment());
                break;
        }
    }

    public Device prototype() {
        Device actuator = new Actuator(this.getName(), this.getType(), this.getUnitType(), this.getMin(), this.getMax(), this.getComment());

        return actuator;
    }

}
