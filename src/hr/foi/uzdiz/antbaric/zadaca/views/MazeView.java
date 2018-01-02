/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.views;

import hr.foi.uzdiz.antbaric.zadaca.extensions.LogElement;
import hr.foi.uzdiz.antbaric.zadaca.helpers.ANSIHelper;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;

/**
 *
 * @author javert
 */
public class MazeView extends View {

    @Override
    public void printContent() {
        super.printContent();

        Integer y = 1;

        for (LogElement log : Logger.getLOG(0)) {
            ANSIHelper.move(0, y++);
            log.accept(this.visitor);
        }

        this.printSeparator();
    }

    public void prepareMaze(Integer[][] maze, Integer x, Integer y) {
        Logger.getInstance().log(new LMessage(String.valueOf(maze.length)), Boolean.TRUE);
        Logger.getInstance().log(new LMessage(String.valueOf(maze[0].length)), Boolean.TRUE);
        
        for (int i = 0; i < maze.length; i++) {
            String line = "";
            
            for (int j = 0; j < maze[0].length; j++) {
                if (i == y && j == x) {
                    line += "O ";
                }
                else if (maze[i][j] == 1) {
                    line += "# ";
                } else {
                    line += ". ";
                }
            }
            
            Logger.getInstance().log(new LMessage(line), Boolean.TRUE);
        }
    }
}
