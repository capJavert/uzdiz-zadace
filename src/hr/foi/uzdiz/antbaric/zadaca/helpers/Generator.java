/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.helpers;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 *
 * @author javert
 */
public class Generator extends Random {

    private static volatile Generator INSTANCE;
    private static final DecimalFormat DECIMAL_FORMATER_ROUND = new DecimalFormat("#0");
    private static final DecimalFormat DECIMAL_FORMATER_1 = new DecimalFormat("#0.0");
    private static final DecimalFormat DECIMAL_FORMATER_5 = new DecimalFormat("#0.00000");
    public static Integer USED_IDENTIFIERS_PLACES = 0;
    public static Integer USED_IDENTIFIERS_SENSORS = 0;
    public static Integer USED_IDENTIFIERS_ACTUATORS = 0;

    static {
        INSTANCE = new Generator();
    }

    private Generator() {
    }

    public static Generator getInstance() {
        return INSTANCE;
    }

    private Integer getUsedIdentifiers(String type) {
        switch (type) {
            case "SENSOR":
                return ++USED_IDENTIFIERS_SENSORS;
            case "ACTUATOR":
                return ++USED_IDENTIFIERS_ACTUATORS;
            default:
                return ++USED_IDENTIFIERS_PLACES;
        }
    }

    public Integer getStatus() {
        return this.nextInt(100) + 1 > Router.getConfig().getDevicePerishability() ? 1 : 0;
    }

    public Double getDouble() {
        return this.nextLong() + this.nextDouble();
    }

    public Integer getUniqueIdentifier(String type) {
        return this.getUsedIdentifiers(type);
    }

    public int fromInterval(int min, int max) {
        int number = this.nextInt(max - min) + min;

        return number;
    }

    public float fromInterval(float min, float max) {
        float number = min + (this.nextFloat() * (max - min));

        return number;
    }

    public Long fromInterval(Double min, Double max) {
        Double number = min + (this.nextDouble() * (max - min));

        return number.longValue();
    }

    public Double fromIntervalPrecision1(Double min, Double max) {
        return min + (this.nextDouble() * (max - min));
    }

    public Double fromIntervalPrecision5(Double min, Double max) {
        return min + (this.nextDouble() * (max - min));
    }

    public String parseDecimalRound(Double number) {
        return Generator.DECIMAL_FORMATER_ROUND.format(number);
    }

    public String parseDecimal1(Double number) {
        return Generator.DECIMAL_FORMATER_1.format(number);
    }

    public String parseDecimal5(Double number) {
        return Generator.DECIMAL_FORMATER_5.format(number);
    }

    public Integer selectFrom(List<Device> list) {
        return this.nextInt(list.size());
    }
}
