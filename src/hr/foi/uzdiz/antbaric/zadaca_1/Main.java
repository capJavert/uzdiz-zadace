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
        final Configuration config;

        //Matcher m = SyntaxValidator.validateArguments(args);
        if (args.length == 8) {
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
            
            Worker.setConfig(config);
            final Worker worker = Worker.getInstance(algorithm);
            worker.start();
        } else {
            System.out.println("Nos ti posran!");
        }
    }

}
