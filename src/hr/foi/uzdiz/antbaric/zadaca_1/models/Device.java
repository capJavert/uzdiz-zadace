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
    private final Integer category;
    private final Integer Type;
    private final Double min;
    private final Double max;
    private final String comment;

    public Device(String name, Integer cateogry, Integer Type, Double min, Double max, String comment) {
        this.name = name;
        this.category = cateogry;
        this.Type = Type;
        this.min = min;
        this.max = max;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public Integer getCategory() {
        return category;
    }

    public Integer getType() {
        return Type;
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

    public Integer getStatus() {
        return Generator.getInstance().getStatus();
    }
    
    public abstract void activate();

}
