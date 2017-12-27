/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.components;

import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.models.LWarning;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CliConfigurationBuilder implements ConfigurationBuilder {

    private final Configuration configuration;

    public CliConfigurationBuilder() {
        this.configuration = new Configuration();
    }

    @Override
    public Boolean isValid() {
        return this.configuration.getPlacesFilePath() != null
                && this.configuration.getSensorsFilePath() != null
                && this.configuration.getActuatorsFielPath() != null
                && this.configuration.getScheduleFilePath() != null;
    }

    /**
     *
     * @return @throws Exception
     */
    @Override
    public Configuration build() throws IllegalArgumentException {
        if (!this.isValid()) {
            throw new IllegalArgumentException("You must supply -m, -s, -a, -r arguments!");
        }

        if (this.configuration.getSeed() == null) {
            Integer argValue = Generator.getInstance().fromInterval(100, 65535);
            Logger.getInstance().log(new LWarning("-g argument not set, Setting: " + argValue), Boolean.TRUE);
            this.setSeed("-g " + String.valueOf(argValue));
        }
        
        if (this.configuration.getRows() == null) {
            this.setSeed("-br " + String.valueOf(24));
        }
        
        if (this.configuration.getCols() == null) {
            this.setSeed("-bs " + String.valueOf(80));
        }
        
        if (this.configuration.getRowsForCommands() == null) {
            this.setSeed("-brk " + String.valueOf(2));
        }
        
        if (this.configuration.getDevicePerishability() == null) {
            this.setSeed("-pi " + String.valueOf(50));
        }

        if (this.configuration.getInterval() == null) {
            Integer argValue = Generator.getInstance().fromInterval(1, 17);
            Logger.getInstance().log(new LWarning("-tcd argument not set, Setting: " + argValue), Boolean.TRUE);
            this.setInterval("-tcd " + String.valueOf(argValue));
        }

        if (this.configuration.getOutFilePath() == null) {
            Logger.getInstance().log(new LWarning("-i argument not set"), Boolean.TRUE);
            this.setOutFilePath("-i antbaric" + String.valueOf(new SimpleDateFormat("_yyyyMMdd_HHmmss").format(new Date()) + ".txt"));
        }

        return this.configuration;
    }

    @Override
    public ConfigurationBuilder setSeed(String seed) {
        seed = CliConfigurationBuilder.toValue(seed);

        this.configuration.setSeed(Long.parseLong(seed));

        return this;
    }

    @Override
    public ConfigurationBuilder setPlacesFilePath(String placesFilePath) {
        placesFilePath = CliConfigurationBuilder.toValue(placesFilePath);

        this.configuration.setPlacesFilePath(absolutePath(placesFilePath));

        return this;
    }

    @Override
    public ConfigurationBuilder setSensorsFilePath(String sensorsFilePath) {
        sensorsFilePath = CliConfigurationBuilder.toValue(sensorsFilePath);

        this.configuration.setSensorsFilePath(absolutePath(sensorsFilePath));

        return this;
    }

    @Override
    public ConfigurationBuilder setActuatorsFielPath(String actuatorsFielPath) {
        actuatorsFielPath = CliConfigurationBuilder.toValue(actuatorsFielPath);

        this.configuration.setActuatorsFielPath(absolutePath(actuatorsFielPath));

        return this;
    }

    @Override
    public ConfigurationBuilder setInterval(String interval) {
        interval = CliConfigurationBuilder.toValue(interval);

        this.configuration.setInterval(Integer.parseInt(interval) * 1000);

        return this;
    }

    @Override
    public ConfigurationBuilder setOutFilePath(String outFilePath) {
        outFilePath = CliConfigurationBuilder.toValue(outFilePath);

        this.configuration.setOutFilePath(absolutePath(outFilePath));

        return this;
    }

    private static String absolutePath(String path) {
        Path p = Paths.get(path);

        if (!p.isAbsolute()) {
            path = System.getProperty("user.dir") + File.separator + path;
        }

        return path;
    }

    private static String toValue(String arg) {
        return arg.split(" ")[1];
    }

    @Override
    public ConfigurationBuilder setRows(String rows) {
        rows = CliConfigurationBuilder.toValue(rows);

        this.configuration.setRows(Integer.parseInt(rows));

        return this;
    }

    @Override
    public ConfigurationBuilder setCols(String cols) {
        cols = CliConfigurationBuilder.toValue(cols);

        this.configuration.setRows(Integer.parseInt(cols));

        return this;
    }

    @Override
    public ConfigurationBuilder setRowsForCommands(String rowsForCommands) {
        rowsForCommands = CliConfigurationBuilder.toValue(rowsForCommands);

        this.configuration.setRows(Integer.parseInt(rowsForCommands));

        return this;
    }

    @Override
    public ConfigurationBuilder setDevicePerishability(String devicePerishability) {
        devicePerishability = CliConfigurationBuilder.toValue(devicePerishability);

        this.configuration.setRows(Integer.parseInt(devicePerishability));

        return this;
    }

}
