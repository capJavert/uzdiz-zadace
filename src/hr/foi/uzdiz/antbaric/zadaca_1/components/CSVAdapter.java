/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.components;

import hr.foi.uzdiz.antbaric.zadaca_1.iterators.PlaceIterator;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Device;
import java.util.List;

/**
 *
 * @author javert
 */
public interface CSVAdapter {
    
    PlaceIterator getPlaces();
    
    List<Device> getSensors();
    
    List<Device> getActuators();
    
}
