/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

import hr.foi.uzdiz.antbaric.zadaca.components.Generator;
import hr.foi.uzdiz.antbaric.zadaca.components.Logger;

/**
 *
 * @author javert
 */
public class Sensor extends Device {

    private Boolean changed = false;

    public Sensor(String name, Integer category, Integer unitType, Double min, Double max, String comment) {
        super(name, category, unitType, min, max, comment);
    }

    @Override
    public void activate() {
        this.changed = true;

        Generator generator = Generator.getInstance();

        switch (this.getUnitType()) {
            case 1:
                Logger.getInstance().log("Sensor '" + this.getName() + "'", true);
                Logger.getInstance().log("    MIN: " + generator.parseDecimal1(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimal1(this.getMax()) + this.getComment(), true);
                break;
            case 2:
                Logger.getInstance().log("Sensor '" + this.getName() + "'", true);
                Logger.getInstance().log("    MIN: " + generator.parseDecimal5(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimal5(this.getMax()) + this.getComment(), true);
                break;
            default:
                Logger.getInstance().log("Sensor '" + this.getName() + "'", true);
                Logger.getInstance().log("    MIN: " + generator.parseDecimalRound(this.getMin()) + this.getComment() + ", MAX: " + generator.parseDecimalRound(this.getMax()) + this.getComment(), true);
                break;
        }
    }

    @Override
    public Device prototype() {
        Device sensor = new Sensor(this.getName(), this.getType(), this.getUnitType(), this.getMin(), this.getMax(), this.getComment());
        sensor.setId();

        return sensor;
    }

    @Override
    public void setId() {
        if (this.id == null) {
            this.id = Generator.getInstance().getUniqueIdentifier(DeviceEnum.SENSOR.toString());
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
