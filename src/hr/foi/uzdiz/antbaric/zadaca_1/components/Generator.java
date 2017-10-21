/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.components;

import hr.foi.uzdiz.antbaric.zadaca_1.models.Device;
import java.util.List;
import java.util.Random;

/**
 *
 * @author javert
 */
public class Generator extends Random {

    private static volatile Generator INSTANCE;

    static {
        INSTANCE = new Generator();
    }

    private Generator() {
    }

    public static Generator getInstance() {
        return INSTANCE;
    }

    public Integer getStatus() {
        return INSTANCE.nextInt(10) + 1 > 1 ? 1 : 0;
    }

    public Integer selectFrom(List<Device> list) {
        return INSTANCE.nextInt(list.size());
    }

}
