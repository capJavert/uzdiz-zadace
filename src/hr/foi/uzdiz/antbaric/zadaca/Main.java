/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca;

import hr.foi.uzdiz.antbaric.zadaca.components.CliConfigurationBuilder;
import hr.foi.uzdiz.antbaric.zadaca.components.ConfigurationBuilder;
import hr.foi.uzdiz.antbaric.zadaca.components.Generator;
import hr.foi.uzdiz.antbaric.zadaca.components.Logger;
import hr.foi.uzdiz.antbaric.zadaca.components.SyntaxValidator;
import hr.foi.uzdiz.antbaric.zadaca.models.AlgorithmEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;

/**
 *
 * @author javert
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger.getInstance().setUsePrintDelay(false); //uncomment for CLI debugging
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

            AlgorithmEnum algorithm = null;

            switch (config.getAlgoritham()) {
                case "Sequential":
                    algorithm = AlgorithmEnum.SEQUENTIAL;
                    break;
                case "Random":
                    algorithm = AlgorithmEnum.RANDOM;
                    break;
                case "Index":
                    algorithm = AlgorithmEnum.INDEX;
                    break;
            }

            Logger.getInstance().add("Loaded inspector algorithm '" + config.getAlgoritham() + "'", true);

            Worker.setConfig(config);

            Logger.getInstance().add("Loaded thread configuration: Execution limit: " + config.getExecutionLimit() + "x, Interval: " + (config.getInterval()/1000) + " sec", true);

            final Worker worker = Worker.getInstance(algorithm);
            worker.start();
        } else {
            Logger.getInstance().add("Error: Please check your arguments", true);
        }
    }

}
