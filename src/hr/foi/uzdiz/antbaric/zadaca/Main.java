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

        if(Main.needHelp(args)) {
            Main.help();
            
            return;
        }
        
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
    
    private static void help() {
        Logger.getInstance().add(Main.help, true);
    }
    
    private static Boolean needHelp(String[] args) {
        return args.length == 1 && args[0].equals("--help");
    }
    
    private static String help = "-g sjeme za generator slučajnog broja (u intervalu 100 - 65535). Ako nije upisana opcija, uzima se broj milisekundi u trenutnom vremenu na bazi njegovog broja sekundi i broja milisekundi.\n" +
        "\n" +
        "-m naziv datoteke mjesta\n" +
        "\n" +
        "-s naziv datoteke senzora\n" +
        "\n" +
        "-a naziv datoteke aktuatora\n" +
        "\n" +
        "-alg puni naziv klase algoritma provjere koja se dinamički učitava\n" +
        "\n" +
        "-tcd trajanje ciklusa dretve u sek. Ako nije upisana opcija, uzima se slučajni broj u intervalu 1 - 17.\n" +
        "\n" +
        "-bcd broj ciklusa dretve. Ako nije upisana opcija, uzima se slučajni broj u intervalu 1 - 23.\n" +
        "\n" +
        "-i naziv datoteke u koju se sprema izlaz programa. Ako nije upisana opcija, uzima se vlastito korisničko ime kojem se dodaje trenutni podaci vremena po formatu _ggggmmdd_hhmmss.txt npr. dkermek_20171105_203128.txt\n" +
        "\n" +
        "-brl broj linija u spremniku za upis u datoteku za izlaz. Ako nije upisana opcija, uzima se slučajni broj u intervalu 100 - 999.\n" +
        "\n" +
        "--help pomoć za korištenje opcija u programu.";

}
