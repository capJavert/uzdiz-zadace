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
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LInfo;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import java.util.List;
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
        Logger.getInstance().setUsePrintDelay(false); // set to true for CLI debugging

        if (Main.needHelp(args)) {
            Main.help();

            return;
        }

        List<Matcher> matcher = SyntaxValidator.validateArguments(args);

        if (matcher != null) {
            Logger.getInstance().log(new LMessage("Reading arguments.."), true);

            try {
                Configuration config = Main.buildConfig(matcher);

                Generator generator = Generator.getInstance();
                generator.setSeed(config.getSeed());
                generator.setDevicePerishability(config.getDevicePerishability());

                Worker.setConfig(config);
                Router.setConfig(config);
                
                Router.getInstance().goTo("");

                //final Worker worker = Worker.getInstance(algorithm);
                //worker.start();
            } catch (IllegalArgumentException ex) {
                Logger.getInstance().log(new LError(ex.getMessage()), true);
            }
        } else {
            Logger.getInstance().log(new LError("Error: Please check your arguments!"), true);
        }
    }

    private static Configuration buildConfig(List<Matcher> matchers) throws IllegalArgumentException {
        ConfigurationBuilder builder = new CliConfigurationBuilder();

        for (Matcher matcher : matchers) {
            if (matcher.group().contains("-g")) {
                builder.setSeed(matcher.group());
            } else if (matcher.group().contains("-br")) {
                builder.setRows(matcher.group());
            } else if (matcher.group().contains("-bs")) {
                builder.setCols(matcher.group());
            } else if (matcher.group().contains("-brk")) {
                builder.setRowsForCommands(matcher.group());
            } else if (matcher.group().contains("-pi")) {
                builder.setDevicePerishability(matcher.group());
            } else if (matcher.group().contains("-m")) {
                builder.setPlacesFilePath(matcher.group());
            } else if (matcher.group().contains("-s")) {
                builder.setSensorsFilePath(matcher.group());
            } else if (matcher.group().contains("-a")) {
                builder.setActuatorsFilePath(matcher.group());
            } else if (matcher.group().contains("-r")) {
                builder.setScheduleFilePath(matcher.group());
            } else if (matcher.group().contains("-tcd")) {
                builder.setInterval(matcher.group());
            }
        }

        return builder.build();
    }

    private static void help() {
        Logger.getInstance().log(new LInfo(Main.HELP), true);
    }

    private static Boolean needHelp(String[] args) {
        return args.length == 1 && args[0].equals("--help");
    }

    private static final String HELP = "-br broj redaka na ekranu (24-40). Ako nije upisana opcija, uzima se 24.\n"
            + "\n"
            + "-bs broj stupaca na ekranu (80-160). Ako nije upisana opcija, uzima se 80.\n"
            + "\n"
            + "-brk broj redaka na ekranu za unos komandi (2-5). Ako nije upisana opcija, uzima se 2.\n"
            + "\n"
            + "-pi prosječni % ispravnosti uređaja (0-100). Ako nije upisana opcija, uzima se 50.\n"
            + "\n"
            + "-g sjeme za generator slučajnog broja (u intervalu 100 - 65535). Ako nije upisana opcija, uzima se broj milisekundi u trenutnom vremenu na bazi njegovog broja sekundi i broja milisekundi.\n"
            + "\n"
            + "-m naziv datoteke mjesta\n"
            + "\n"
            + "-s naziv datoteke senzora\n"
            + "\n"
            + "-a naziv datoteke aktuatora\n"
            + "\n"
            + "-r naziv datoteke rasporeda\n"
            + "\n"
            + "-tcd trajanje ciklusa dretve u sek. Ako nije upisana opcija, uzima se slučajni broj u intervalu 1 - 17.\n"
            + "\n"
            + "--help pomoć za korištenje opcija u programu.";

}
