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

    public Integer id = 0;
    private final Integer modelId;
    public final String name;
    public final Integer type;
    public final Integer unitType;
    public final Double min;
    public final Double max;
    public final String comment;
    public Double value;
    private Boolean available = true;
    private Boolean valid = true;

    public Device(Integer modelId, String name, Integer cateogry, Integer Type, Double min, Double max, String comment) {
        this.modelId = modelId;
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

    public Device(Integer id, Integer modelId, String name, Integer cateogry, Integer Type, Double min, Double max, String comment) {
        this.id = id;
        this.modelId = modelId;
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

    public Integer getModelId() {
        return modelId;
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
        return this.name + " ID: " + this.getId();
    }

    public String getId() {
        return String.valueOf(id);
    }

    public abstract Device prototype(Integer id);

    public abstract void activate();

    public abstract void setId(Integer id);

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        if (this.valid) {
            this.available = available;
        }
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
        
        if (!this.available) {
            this.available = this.valid;   
        }
    }

}
