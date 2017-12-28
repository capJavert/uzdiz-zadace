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
public enum ANSIColor {
    BLACK("30m"),
    RED("31m"),
    GREEN("32m"),
    YELLOW("33m"),
    BLUE("34m"),
    MAGENTA("35m"),
    CYAN("36m"),
    WHITE("37m");

    private final String code;

    ANSIColor(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
