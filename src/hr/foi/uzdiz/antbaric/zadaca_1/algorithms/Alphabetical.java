/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.algorithms;

import hr.foi.uzdiz.antbaric.zadaca_1.models.Place;
import java.util.List;
import java.util.Map;

/**
 *
 * @author javert
 */
public class Alphabetical implements Algorithm {

    @Override
    public List<Map.Entry<String, Place>> order(List<Map.Entry<String, Place>> places) {
        places.sort((Map.Entry<String, Place> x, Map.Entry<String, Place> y) -> (x.getKey()).compareTo(y.getKey()));

        return places;
    }
}
