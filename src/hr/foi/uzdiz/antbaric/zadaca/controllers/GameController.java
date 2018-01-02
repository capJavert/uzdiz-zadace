/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.maze.Movement;
import hr.foi.uzdiz.antbaric.zadaca.maze.MovementDown;
import hr.foi.uzdiz.antbaric.zadaca.maze.MovementLeft;
import hr.foi.uzdiz.antbaric.zadaca.maze.MovementRight;
import hr.foi.uzdiz.antbaric.zadaca.maze.MovementUp;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LNotification;
import hr.foi.uzdiz.antbaric.zadaca.views.MazeView;
import java.util.Scanner;

/**
 *
 * @author javert
 */
public class GameController extends Controller<MazeView, Integer> {

    public GameController(MazeView view, Integer model) {
        super(view, model);
    }

    private final Integer[][] maze = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 1, 1},
        {1, 0, 1, 1, 1, 0, 1, 0, 1, 1},
        {1, 0, 1, 1, 1, 0, 1, 0, 1, 1},
        {1, 0, 0, 0, 0, 0, 1, 0, 1, 1},
        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1},
        {1, 0, 1, 1, 0, 0, 0, 0, 1, 1},
        {1, 0, 1, 1, 0, 1, 1, 1, 1, 1},
        {1, 0, 1, 0, 0, 1, 1, 1, 1, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 1, 1, 1, 0, 1, 1},
        {1, 0, 1, 1, 1, 1, 1, 0, 1, 1},
        {1, 0, 0, 0, 0, 1, 1, 0, 0, 1},
        {1, 1, 1, 1, 0, 1, 1, 1, 0, 1},
        {1, 1, 1, 1, 0, 0, 0, 1, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},};

    public Integer x = 1;
    public Integer y = 1;

    @Override
    public void init() {
        super.init();

        String command;

        do {
            Movement root = null;
            Movement movement = null;
            Logger.getInstance().emptyBuffer();
            this.view.prepareMaze(maze, x, y);

            command = new Scanner(System.in).nextLine();
            ANSIHelper.cleol();

            if (command.toUpperCase().equals("Q")) {
                Logger.getInstance().log(new LError("PUNY COWARD!!!"), Boolean.TRUE);
                break;
            }

            if (y - 1 < maze.length && maze[y - 1][x] != 1) {
                root = movement = new MovementUp();
            }

            if (x + 1 < maze[0].length && maze[y][x + 1] != 1) {
                if (root == null || movement == null) {
                    root = movement = new MovementRight();
                } else {
                    movement = movement.setNext(new MovementRight());
                }
            }

            if (y + 1 < maze.length && maze[y + 1][x] != 1) {
                if (root == null || movement == null) {
                    root = movement = new MovementDown();
                } else {
                    movement = movement.setNext(new MovementDown());
                }
            }

            if (x - 1 <= maze[0].length && maze[y][x - 1] != 1) {
                if (root == null || movement == null) {
                    root = new MovementLeft();
                } else {
                    movement.setNext(new MovementLeft());
                }
            }

            if (root != null) {
                root.tryMovement(this, command.toUpperCase());
            }

            if (x == 8 && y == 14) {
                this.view.prepareMaze(maze, x, y);
                Logger.getInstance().log(new LNotification("You WIN!"), Boolean.TRUE);
                break;
            }
        } while (true);
    }
}
