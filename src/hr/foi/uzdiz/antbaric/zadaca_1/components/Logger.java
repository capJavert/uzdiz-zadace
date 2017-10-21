/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.components;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javert
 */
public class Logger {

    private static volatile Logger INSTANCE;
    private static final List<String> LOG = new ArrayList<>();

    static {
        INSTANCE = new Logger();
    }

    private Logger() {
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

    public void add(String log, Boolean printToConsole) {
        LOG.add(log);

        if (printToConsole) {
            System.out.println(log);
        }
    }

    public void writeToFile(String filePath) {
        try {
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filePath));
            for (String line : Logger.LOG) {
                outputWriter.write(line);
                outputWriter.newLine();
            }
            outputWriter.flush();
            outputWriter.close();
        } catch (IOException ex) {
            this.add("Error: Output file path '" + filePath + "' not valid", true);
        }
        
        this.add("Done!", true);
    }
}
