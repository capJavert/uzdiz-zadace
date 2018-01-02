/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.maze;

import hr.foi.uzdiz.antbaric.zadaca.controllers.GameController;

/**
 *
 * @author javert
 */
public abstract class Movement {

    public static String UP = "U";
    public static String RIGHT = "R";
    public static String DOWN = "D";
    public static String LEFT = "L";

    protected String direction;

    protected Movement next;
    protected Movement root;

    public Movement setNext(Movement controller) {
        this.next = controller;

        return this.next;
    }

    public void tryMovement(GameController controller, String moveDirection) {
        if (moveDirection == null ? this.direction != null : !moveDirection.equals(this.direction)) {
            if (this.next != null) {
                this.next.tryMovement(controller, moveDirection);
            }
        } else {
            this.move(controller);
        }
    }

    abstract protected void move(GameController controller);
}
