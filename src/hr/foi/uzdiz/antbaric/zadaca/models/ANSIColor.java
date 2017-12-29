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
    BLACK("30m", "40m"),
    RED("31m", "41m"),
    GREEN("32m", "42m"),
    YELLOW("33m", "43m"),
    BLUE("34m", "44m"),
    MAGENTA("35m", "45m"),
    CYAN("36m", "46m"),
    WHITE("37m", "47m");

    private final String fgCode;
    private final String bgCode;

    ANSIColor(String fgCode, String bgCode) {
        this.fgCode = fgCode;
        this.bgCode = bgCode;
    }

    public String getFgCode() {
        return this.fgCode;
    }
    
    public String getBgCode() {
        return this.bgCode;
    }
}
