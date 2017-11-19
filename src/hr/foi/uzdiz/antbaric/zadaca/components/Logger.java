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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author javert
 */
public class Logger {

    private static volatile Logger INSTANCE;
    private static List<String> LOG = new ArrayList<>();
    private static Boolean USE_PRINT_DELAY = true;
    private static Integer BUFFER_SIZE = 0;
    private String filePath = "";

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

    public void setBufferSize(Integer bufferSize) {
        Logger.BUFFER_SIZE = bufferSize;
    }

    public void setFilePath(String filePath) {
        INSTANCE.filePath = filePath;
    }

    public void log(String log, Boolean printToConsole) {
        if (log == null) {
            return;
        }

        if (INSTANCE.isBufferFull()) {
            INSTANCE.writeToFile();
            this.log("Buffer full, writing log to output file...", true);
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

    public Boolean isBufferFull() {
        return Logger.LOG.size() >= Logger.BUFFER_SIZE && Logger.BUFFER_SIZE != 0;
    }

    public void writeToFile() {
        try {
            Files.write(Paths.get(INSTANCE.filePath), Logger.LOG, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println("Error: Output file path '" + INSTANCE.filePath + "' not valid");
        }

        INSTANCE.emptyBuffer();
    }

    private void emptyBuffer() {
        Logger.LOG = new ArrayList<>();
    }

    public void clearFile() {
        try {
            try (BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filePath))) {
                outputWriter.write("");
                outputWriter.flush();
            }
        } catch (IOException ex) {
            System.out.println("Error: Output file path '" + filePath + "' not valid");
        }
    }
}
