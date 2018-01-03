/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import java.io.Serializable;

/**
 *
 * @author javert
 */
public class Sensor extends Device implements Serializable {

    private Boolean changed = false;

    public Sensor(Integer modelId, String name, Integer category, Integer unitType, Double min, Double max, String comment) {
        super(modelId, name, category, unitType, min, max, comment);
    }

    public Sensor(Integer id, Integer modelId, String name, Integer category, Integer unitType, Double min, Double max, String comment) {
        super(id, modelId, name, category, unitType, min, max, comment);
    }

    @Override
    public void activate() {
        this.changed = true;

        Generator generator = Generator.getInstance();

        switch (this.getUnitType()) {
            case 1:
                Logger.getInstance().log(new LMessage("Sensor '" + this.getName() + "'"), true);
                Logger.getInstance().log(new LNotification("    MIN: " + generator.parseDecimal1(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimal1(this.getMax()) + this.getComment()), true);
                break;
            case 2:
                Logger.getInstance().log(new LMessage("Sensor '" + this.getName() + "'"), true);
                Logger.getInstance().log(new LNotification("    MIN: " + generator.parseDecimal5(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimal5(this.getMax()) + this.getComment()), true);
                break;
            default:
                Logger.getInstance().log(new LMessage("Sensor '" + this.getName() + "'"), true);
                Logger.getInstance().log(new LNotification("    MIN: " + generator.parseDecimalRound(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimalRound(this.getMax()) + this.getComment()), true);
                break;
        }
    }

    @Override
    public Device prototype(Integer id) {
        Device sensor = new Sensor(this.getModelId(), this.getName(), this.getType(), this.getUnitType(), this.getMin(), this.getMax(), this.getComment());
        sensor.setId(id);

        return sensor;
    }

    @Override
    public void setId(Integer id) {
        if (id == 0) {
            this.id = Generator.getInstance().getUniqueIdentifier(DeviceEnum.SENSOR.toString());
        } else {
            this.id = id;
        }
    }

    @Override
    public Integer getStatus() {
        this.changed = false;

        return super.getStatus();
    }

    public Boolean isChanged() {
        return changed;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Sensor)) {
            return false;
        }
        Sensor other = (Sensor) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

}
