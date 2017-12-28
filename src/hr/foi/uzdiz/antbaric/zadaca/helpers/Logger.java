/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.helpers;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.controllers.Controller;
import hr.foi.uzdiz.antbaric.zadaca.extensions.LogElement;
import hr.foi.uzdiz.antbaric.zadaca.extensions.LogElementVisitor;
import hr.foi.uzdiz.antbaric.zadaca.extensions.PimpMyLogVisitor;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LWarning;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

/**
 *
 * @author javert
 */
public class Logger {

    private static volatile Logger INSTANCE;
    private static List<LogElement> LOG = new ArrayList<>();
    private static List<String> COMMANDS = new ArrayList<>();
    private static Boolean USE_PRINT_DELAY = true;
    private static final Boolean PRINT_TO_CONSOLE = false;
    private static Integer BUFFER_SIZE = 0;
    private String filePath = "";
    private final LogElementVisitor pimpMyLogVisitor = new PimpMyLogVisitor();

    private Controller controller;

    static {
        INSTANCE = new Logger();
    }

    private Logger() {
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setUsePrintDelay(Boolean usePrintDelay) {
        USE_PRINT_DELAY = usePrintDelay;
    }

    public void setBufferSize(Integer bufferSize) {
        Logger.BUFFER_SIZE = bufferSize;
    }

    public void setFilePath(String filePath) {
        INSTANCE.filePath = filePath;
    }

    public static List<LogElement> getLOG(Integer limit) {
        if (limit > LOG.size() || limit == 0) {
            return LOG;
        } else {
            return LOG.subList(LOG.size() - limit, LOG.size());
        }
    }

    public static List<String> getCOMMANDS(Integer limit) {
        if (limit > COMMANDS.size()) {
            List<String> inset = new ArrayList<>(Collections.nCopies(limit - COMMANDS.size(), ""));
            inset.addAll(COMMANDS);

            return inset;
        } else {
            return COMMANDS.subList(COMMANDS.size() - limit, COMMANDS.size());
        }
    }

    public void log(LogElement log, Boolean uiPrint) {
        if (log == null) {
            return;
        }

        if (!uiPrint) {
            log.accept(this.pimpMyLogVisitor);
            System.out.println();

            if (Logger.USE_PRINT_DELAY) {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            if (INSTANCE.isBufferFull()) {
                controller.prompt();
                LOG.add(log);
            } else {
                LOG.add(log);
                controller.update();
            }
        }
    }

    public void logCommand(String command) {
        if (command == null) {
            return;
        }

        COMMANDS.add(command);
    }

    public Boolean isBufferFull() {
        return Logger.LOG.size() >= Logger.BUFFER_SIZE && Logger.BUFFER_SIZE != 0;
    }

    public void writeToFile() {
        List<String> stringLog = new ArrayList<>();

        for (LogElement log : Logger.LOG) {
            stringLog.add(log.toString());
        }

        if (!this.filePath.equals("")) {
            try {
                Files.write(Paths.get(INSTANCE.filePath), stringLog, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException ex) {
                new LError("Error: Output file path '" + INSTANCE.filePath + "' not valid").accept(this.pimpMyLogVisitor);
            }

            INSTANCE.emptyBuffer();
        } else {
            this.log(new LWarning("Can't write to file, output file path is not set"), true);
        }
    }

    public void emptyBuffer() {
        Logger.LOG = new ArrayList<>();
        // Logger.COMMANDS = new ArrayList<>();
    }

    public void clearFile() {
        try {
            try (BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filePath))) {
                outputWriter.write("");
                outputWriter.flush();
            }
        } catch (IOException ex) {
            new LError("Error: Output file path '" + INSTANCE.filePath + "' not valid").accept(this.pimpMyLogVisitor);
        }
    }
}
