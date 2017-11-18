/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.components;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author javert
 */
public class Logger {

    private static volatile Logger INSTANCE;
    private static final List<String> LOG = new ArrayList<>();
    private static Boolean USE_PRINT_DELAY = true;

    static {
        INSTANCE = new Logger();
    }

    private Logger() {
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

    public void setUsePrintDelay(Boolean usePrintDelay) {
        USE_PRINT_DELAY = usePrintDelay;
    }

    public void add(String log, Boolean printToConsole) {
        if(log == null) {
            return;
        }
        
        LOG.add(log);

        if (printToConsole) {
            System.out.println(log);

            if (Logger.USE_PRINT_DELAY) {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
