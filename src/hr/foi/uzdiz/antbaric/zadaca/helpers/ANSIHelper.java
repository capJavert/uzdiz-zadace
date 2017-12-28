/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.helpers;

import hr.foi.uzdiz.antbaric.zadaca.models.ANSIColor;

/**
 *
 * @author javert
 */
public class ANSIHelper {

    public static final String ESC = "\033[";

    public static void print(String s) {
        System.out.print(s);
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void cls() {
        ANSIHelper.print(ESC + "2J");
    }
    
    public static void cleol() {
        ANSIHelper.print(ESC + "2K");
    }

    public static void write(String s) {
        ANSIHelper.print(s);
    }

    public static void write(String s, int x, int y) {
        ANSIHelper.move(x, y);
        ANSIHelper.print(s);
    }

    public static void move(int x, int y) {
        ANSIHelper.print(ESC + y + ";" + x + "H");
    }

    public static void clear(int x, int y) {
        ANSIHelper.move(x, y);
        ANSIHelper.save();
        ANSIHelper.setBg(ANSIColor.BLACK);
        ANSIHelper.setFg(ANSIColor.WHITE);
        ANSIHelper.print(" ");
        ANSIHelper.load();
    }

    public static void save() {
        ANSIHelper.print(ESC + "7");
    }

    public static void load() {
        ANSIHelper.print(ESC + "8");
    }

    public static void setFg(ANSIColor color) {
        ANSIHelper.print(ESC + color.getCode());
    }

    public static void setBg(ANSIColor color) {
        ANSIHelper.print(ESC + color.getCode());
    }
    
    public static void moveUp() {
        ANSIHelper.println(ESC + "1A");
    }
    
    public static void moveDown() {
        ANSIHelper.println(ESC + "1B");
    }
}
