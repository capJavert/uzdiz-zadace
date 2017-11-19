/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.extensions;

import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LInfo;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.LWarning;
import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.fusesource.jansi.AnsiConsole;

/**
 *
 * @author javert
 */
public class PimpMyLogVisitor implements LogElementVisitor {

    @Override
    public void visit(LError error) {
        AnsiConsole.out.println(ansi().fg(Ansi.Color.RED).a(error.toString()).reset());
    }

    @Override
    public void visit(LWarning warning) {
        AnsiConsole.out.println(ansi().fg(Ansi.Color.YELLOW).a(warning.toString()).reset());
    }

    @Override
    public void visit(LMessage message) {
        AnsiConsole.out.println(ansi().fg(Ansi.Color.GREEN).a(message.toString()).reset());
    }

    @Override
    public void visit(LInfo info) {
        AnsiConsole.out.println(ansi().fg(Ansi.Color.BLUE).a(info.toString()).reset());
    }

}
