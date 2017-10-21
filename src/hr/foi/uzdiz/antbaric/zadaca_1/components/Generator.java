/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.components;

import hr.foi.uzdiz.antbaric.zadaca_1.models.Device;
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

    static {
        INSTANCE = new Generator();
    }

    private Generator() {
    }

    public static Generator getInstance() {
        return INSTANCE;
    }

    public Integer getStatus() {
        return this.nextInt(10) + 1 > 1 ? 1 : 0;
    }

    public Double getDouble() {
        return this.nextLong()+this.nextDouble();
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
