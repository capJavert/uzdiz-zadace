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
public class MovementUp extends Movement {

    public MovementUp() {
        this.direction = Movement.UP;
    }

    @Override
    protected void move(GameController controller) {
       controller.y -= 1;
    }
}
