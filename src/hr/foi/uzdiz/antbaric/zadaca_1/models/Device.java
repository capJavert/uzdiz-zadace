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
public abstract class Device {

    private final String name;
    private final Integer type;
    private final Integer unitType;
    private final Double min;
    private final Double max;
    private final String comment;
    private Double value;

    public Device(String name, Integer cateogry, Integer Type, Double min, Double max, String comment) {
        this.name = name;
        this.type = cateogry;
        this.unitType = Type;
        this.min = min;
        this.max = max;
        this.comment = comment;
        
        if(this.type == 3) {
            this.value = new Double(0);
        } else {
            this.value = this.min;
        }
    }

    public String getName() {
        return name;
    }

    public Integer getType() {
        return type;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public String getComment() {
        return comment;
    }

    public Double getValue() {
        return value;
    }
    
    public void setValue(Double value) {
        if(value > this.max) {
            this.value = this.max;
        } else if(value < this.min) {
            this.value = this.min;
        } else {
            this.value = value;
        }
    }

    public Integer getStatus() {
        return Generator.getInstance().getStatus();
    }

    public abstract Device prototype();
    
    public abstract void activate();

}
