/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.models;

/**
 *
 * @author javert
 */
public class Sensor extends Device {

    public Sensor(String name, Integer Category, Integer Type, Double min, Double max, String comment) {
        super(name, Category, Type, min, max, comment);
    }

    @Override
    public void activate() {
        System.out.println("Sensor activated!");
    }

    public Device prototype() {
        Device sensor = new Sensor(this.getName(), this.getCategory(), this.getType(), this.getMin(), this.getMax(), this.getComment());
        
        return sensor;
    }
}
