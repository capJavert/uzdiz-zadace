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
public class PlaceDeviceMap extends DeviceMap {

    private final Integer modelFk;

    public PlaceDeviceMap(MapEnum type, Integer pk, Integer modelFk, Integer fk) {
        super(type, pk, fk);

        this.modelFk = modelFk;
    }

    public Integer getModelFk() {
        return modelFk;
    }

}
