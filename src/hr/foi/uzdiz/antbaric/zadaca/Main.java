/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca;

import hr.foi.uzdiz.antbaric.zadaca.components.CliConfigurationBuilder;
import hr.foi.uzdiz.antbaric.zadaca.components.ConfigurationBuilder;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.helpers.SyntaxValidator;
import hr.foi.uzdiz.antbaric.zadaca.models.AlgorithmEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LInfo;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

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

        if (Main.needHelp(args)) {
            Main.help();

            return;
        }

        Matcher matcher = SyntaxValidator.validateArguments(args);

        if (matcher != null) {
            Logger.getInstance().log(new LMessage("Reading arguments.."), true);

            try {
                Configuration config = Main.buildConfig(matcher);

                Logger.getInstance().setBufferSize(config.getLoggerBufferSize());
                Logger.getInstance().setFilePath(config.getOutFilePath());
                Logger.getInstance().clearFile();

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

                Logger.getInstance().log(new LMessage("Loaded inspector algorithm '" + config.getAlgoritham() + "'"), true);

                Worker.setConfig(config);

                Logger.getInstance().log(new LMessage("Loaded thread configuration: Execution limit: " + config.getExecutionLimit() + "x, Interval: " + (config.getInterval() / 1000) + " sec"), true);

                final Worker worker = Worker.getInstance(algorithm);
                worker.start();
            } catch (NullPointerException ex) {
                Logger.getInstance().log(new LError("Error: Invalid arguments"), true);
            }
        } else {
            Logger.getInstance().log(new LError("Error: Please check your arguments"), true);
        }
    }

    private static Configuration buildConfig(Matcher matcher) {
        ConfigurationBuilder builder = new CliConfigurationBuilder();

        if (matcher.group(2) != null) {
            builder.setSeed(matcher.group(2));
        } else {
            builder.setSeed("-g " + String.valueOf(System.currentTimeMillis()));
        }

        builder.setPlacesFilePath(matcher.group(3))
                .setSensorsFilePath(matcher.group(4))
                .setActuatorsFielPath(matcher.group(5))
                .setAlgoritham("-alg " + matcher.group(7));

        if (matcher.group(8) != null) {
            builder.setInterval(matcher.group(8));
        } else {
            builder.setInterval("-tcd " + String.valueOf(Generator.getInstance().fromInterval(1, 17)));
        }

        if (matcher.group(9) != null) {
            builder.setExecutionLimit(matcher.group(9));
        } else {
            builder.setExecutionLimit("-bcd " + String.valueOf(Generator.getInstance().fromInterval(1, 23)));
        }

        if (matcher.group(10) != null) {
            builder.setOutFilePath(matcher.group(10));
        } else {
            builder.setOutFilePath("-i antbaric" + String.valueOf(new SimpleDateFormat("_yyyyMMdd_HHmmss").format(new Date()) + ".txt"));
        }

        if (matcher.group(11) != null) {
            builder.setLoggerBufferSize(matcher.group(11));
        } else {
            builder.setLoggerBufferSize("-brl " + String.valueOf(Generator.getInstance().fromInterval(100, 999)));
        }

        return builder.build();
    }

    private static void help() {
        Logger.getInstance().log(new LInfo(Main.HELP), true);
    }

    private static Boolean needHelp(String[] args) {
        return args.length == 1 && args[0].equals("--help");
    }

    private static final String HELP = "-g sjeme za generator slučajnog broja (u intervalu 100 - 65535). Ako nije upisana opcija, uzima se broj milisekundi u trenutnom vremenu na bazi njegovog broja sekundi i broja milisekundi.\n"
            + "\n"
            + "-m naziv datoteke mjesta\n"
            + "\n"
            + "-s naziv datoteke senzora\n"
            + "\n"
            + "-a naziv datoteke aktuatora\n"
            + "\n"
            + "-alg puni naziv klase algoritma provjere koja se dinamički učitava\n"
            + "\n"
            + "-tcd trajanje ciklusa dretve u sek. Ako nije upisana opcija, uzima se slučajni broj u intervalu 1 - 17.\n"
            + "\n"
            + "-bcd broj ciklusa dretve. Ako nije upisana opcija, uzima se slučajni broj u intervalu 1 - 23.\n"
            + "\n"
            + "-i naziv datoteke u koju se sprema izlaz programa. Ako nije upisana opcija, uzima se vlastito korisničko ime kojem se dodaje trenutni podaci vremena po formatu _ggggmmdd_hhmmss.txt npr. dkermek_20171105_203128.txt\n"
            + "\n"
            + "-brl broj linija u spremniku za upis u datoteku za izlaz. Ako nije upisana opcija, uzima se slučajni broj u intervalu 100 - 999.\n"
            + "\n"
            + "--help pomoć za korištenje opcija u programu.";

}
