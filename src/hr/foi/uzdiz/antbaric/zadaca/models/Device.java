/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import java.io.Serializable;

/**
 *
 * @author javert
 */
public abstract class Device implements Serializable {

    public Integer id;
    public final String name;
    public final Integer type;
    public final Integer unitType;
    public final Double min;
    public final Double max;
    public final String comment;
    public Double value;

    public Device(String name, Integer cateogry, Integer Type, Double min, Double max, String comment) {
        this.id = null;
        this.name = name;
        this.type = cateogry;
        this.unitType = Type;
        this.min = min;
        this.max = max;
        this.comment = comment;

        if (this.type == 3) {
            this.value = new Double(0);
        } else {
            this.value = this.min;
        }
    }

    public Integer getId() {
        return id;
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
        if (value > this.max) {
            this.value = this.max;
        } else if (value < this.min) {
            this.value = this.min;
        } else {
            this.value = value;
        }
    }

    public Integer getStatus() {
        return Generator.getInstance().getStatus();
    }

    public String getNameAndId() {
        return this.name + " ID: " + this.id;
    }
    
    public abstract Device prototype();

    public abstract void activate();
    
    public abstract void setId();

}
