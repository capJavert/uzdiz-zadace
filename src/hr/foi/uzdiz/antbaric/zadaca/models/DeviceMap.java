/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

/**
 *
 * @author javert
 */
public class DeviceMap {

    private MapEnum type;
    private Integer pk;
    private Integer fk;

    public DeviceMap(MapEnum type, Integer pk, Integer fk) {
        this.type = type;
        this.pk = pk;
        this.fk = fk;
    }

    public MapEnum getType() {
        return type;
    }

    public Integer getPk() {
        return pk;
    }

    public Integer getFk() {
        return fk;
    }

}
