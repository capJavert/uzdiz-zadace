/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.helpers;

/**
 *
 * @author javert
 */
public class ANSIHelper {
    
    protected enum Color {
        BLACK, WHITE, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN
    }
    
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
        ANSIHelper.setBg(Color.BLACK);
        ANSIHelper.setFg(Color.WHITE);
        ANSIHelper.print(" ");
        ANSIHelper.load();
    }

    public static void save() {
        ANSIHelper.print(ESC + "7");
    }

    public static void load() {
        ANSIHelper.print(ESC + "8");
    }

    public static void setFg(Color color) {
        switch (color) {
            case BLACK:
                ANSIHelper.print(ESC + "30" + "m");
                break;
            case WHITE:
                ANSIHelper.print(ESC + "37" + "m");
                break;
            case RED:
                ANSIHelper.print(ESC + "31" + "m");
                break;
            case GREEN:
                ANSIHelper.print(ESC + "32" + "m");
                break;
            case YELLOW:
                ANSIHelper.print(ESC + "33" + "m");
                break;
            case BLUE:
                ANSIHelper.print(ESC + "34" + "m");
                break;
            case MAGENTA:
                ANSIHelper.print(ESC + "35" + "m");
                break;
            case CYAN:
                ANSIHelper.print(ESC + "36" + "m");
                break;
        }
    }

    public static void setBg(Color color) {
        switch (color) {
            case BLACK:
                ANSIHelper.print(ESC + "40" + "m");
                break;
            case WHITE:
                ANSIHelper.print(ESC + "47" + "m");
                break;
            case RED:
                ANSIHelper.print(ESC + "41" + "m");
                break;
            case GREEN:
                ANSIHelper.print(ESC + "42" + "m");
                break;
            case YELLOW:
                ANSIHelper.print(ESC + "43" + "m");
                break;
            case BLUE:
                ANSIHelper.print(ESC + "44" + "m");
                break;
            case MAGENTA:
                ANSIHelper.print(ESC + "45" + "m");
                break;
            case CYAN:
                ANSIHelper.print(ESC + "46" + "m");
                break;
        }
    }

    public static void hidePointer() {
        ANSIHelper.println(ESC + "?25h");
    }

    public static void showPointer() {
        ANSIHelper.println(ESC + "?25l");
    }
}
