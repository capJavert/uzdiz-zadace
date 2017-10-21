/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1;

import hr.foi.uzdiz.antbaric.zadaca_1.algorithms.Algorithm;
import hr.foi.uzdiz.antbaric.zadaca_1.algorithms.Alphabetical;
import hr.foi.uzdiz.antbaric.zadaca_1.algorithms.Random;
import hr.foi.uzdiz.antbaric.zadaca_1.algorithms.Sequential;
import hr.foi.uzdiz.antbaric.zadaca_1.components.CliConfigurationBuilder;
import hr.foi.uzdiz.antbaric.zadaca_1.components.ConfigurationBuilder;
import hr.foi.uzdiz.antbaric.zadaca_1.components.Generator;
import hr.foi.uzdiz.antbaric.zadaca_1.components.Logger;
import hr.foi.uzdiz.antbaric.zadaca_1.components.SyntaxValidator;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Configuration;

/**
 *
 * @author javert
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Logger.getInstance().setUsePrintDelay(false); //uncomment for CLI debugging
        final Configuration config;

        if (SyntaxValidator.validateArguments(args)) {
            Logger.getInstance().add("Reading arguments..", true);

            ConfigurationBuilder builder = new CliConfigurationBuilder();
            config = builder.setSeed(args[0])
                    .setPlacesFilePath(args[1])
                    .setSensorsFilePath(args[2])
                    .setActuatorsFielPath(args[3])
                    .setAlgoritham(args[4])
                    .setInterval(args[5])
                    .setExecutionLimit(args[6])
                    .setOutFilePath(args[7])
                    .build();

            Generator generator = Generator.getInstance();
            generator.setSeed(config.getSeed());

            Algorithm algorithm = null;

            switch (config.getAlgoritham()) {
                case "Sequential":
                    algorithm = new Sequential();
                    break;
                case "Alphabetical":
                    algorithm = new Alphabetical();
                    break;
                case "Random":
                    algorithm = new Random();
                    break;
            }

            Logger.getInstance().add("Loaded inspector algorithm '" + config.getAlgoritham() + "'", true);

            Worker.setConfig(config);

            Logger.getInstance().add("Loaded thread configuration: Execution limit: " + config.getExecutionLimit() + "x, Interval: " + config.getInterval() + " sec", true);

            final Worker worker = Worker.getInstance(algorithm);
            worker.start();
        } else {
            Logger.getInstance().add("Error: Please check your arguments", true);
        }
    }

}
