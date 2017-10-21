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
public class Sensor extends Device {

    public Sensor(String name, Integer category, Integer unitType, Double min, Double max, String comment) {
        super(name, category, unitType, min, max, comment);
    }

    @Override
    public void activate() {
        Generator generator = Generator.getInstance();

        switch (this.getUnitType()) {
            case 1:
                System.out.println("Sensor '" + this.getName() + "'");
                System.out.println("    MIN: " + generator.parseDecimal1(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimal1(this.getMax()) + this.getComment());
                break;
            case 2:
                System.out.println("Sensor '" + this.getName() + "'");
                System.out.println("    MIN: " + generator.parseDecimal5(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimal5(this.getMax()) + this.getComment());
                break;
            default:
                System.out.println("Sensor '" + this.getName() + "'");
                System.out.println("    MIN: " + generator.parseDecimalRound(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimalRound(this.getMax()) + this.getComment());
                break;
        }
    }

    public Device prototype() {
        Device sensor = new Sensor(this.getName(), this.getType(), this.getUnitType(), this.getMin(), this.getMax(), this.getComment());

        return sensor;
    }
}
