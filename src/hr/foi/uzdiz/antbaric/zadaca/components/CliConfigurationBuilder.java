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
            Logger.getInstance().log(new LWarning("-g argument not set, Setting: " + argValue), Boolean.FALSE);
            this.setSeed("-g " + String.valueOf(argValue));
        }

        if (this.configuration.getRows() == null) {
            Logger.getInstance().log(new LWarning("-br argument not set, Setting: 24"), Boolean.FALSE);
            this.setRows("-br " + String.valueOf(24));
        }

        if (this.configuration.getCols() == null) {
            Logger.getInstance().log(new LWarning("-bs argument not set, Setting: 80"), Boolean.FALSE);
            this.setCols("-bs " + String.valueOf(80));
        }

        if (this.configuration.getRowsForCommands() == null) {
            Logger.getInstance().log(new LWarning("-brk argument not set, Setting: 2"), Boolean.FALSE);
            this.setRowsForCommands("-brk " + String.valueOf(2));
        }

        if (this.configuration.getDevicePerishability() == null) {
            Logger.getInstance().log(new LWarning("-pi argument not set, Setting: 50"), Boolean.FALSE);
            this.setDevicePerishability("-pi " + String.valueOf(50));
        }

        if (this.configuration.getInterval() == null) {
            Integer argValue = Generator.getInstance().fromInterval(1, 17);
            Logger.getInstance().log(new LWarning("-tcd argument not set, Setting: " + argValue), Boolean.FALSE);
            this.setInterval("-tcd " + String.valueOf(argValue));
        }

        if (this.configuration.getkMax() == null) {
            Logger.getInstance().log(new LWarning("-kmax argument not set, Setting: " + 100), Boolean.FALSE);
            this.setKMax("-kmax " + String.valueOf(100));
        }

        if (this.configuration.getkMin() == null) {
            Logger.getInstance().log(new LWarning("-kmin argument not set, Setting: " + 5), Boolean.FALSE);
            this.setKMin("-kmin " + String.valueOf(5));
        }

        if (this.configuration.getkPov() == null) {
            Logger.getInstance().log(new LWarning("-kpov argument not set, Setting: " + 5), Boolean.FALSE);
            this.setKPov("-kpov " + String.valueOf(5));
        }

        if (this.configuration.getkMin() > this.configuration.getkMax()) {
            this.configuration.setkMin(this.configuration.getkMax());
        }

        if (this.configuration.getkPov() > this.configuration.getkMax()) {
            this.configuration.setkPov(this.configuration.getkMax());
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
    public ConfigurationBuilder setActuatorsFilePath(String actuatorsFielPath) {
        actuatorsFielPath = CliConfigurationBuilder.toValue(actuatorsFielPath);

        this.configuration.setActuatorsFielPath(absolutePath(actuatorsFielPath));

        return this;
    }

    @Override
    public ConfigurationBuilder setScheduleFilePath(String scheduleFilePath) {
        scheduleFilePath = CliConfigurationBuilder.toValue(scheduleFilePath);

        this.configuration.setScheduleFilePath(absolutePath(scheduleFilePath));

        return this;
    }

    @Override
    public ConfigurationBuilder setInterval(String interval) {
        interval = CliConfigurationBuilder.toValue(interval);

        this.configuration.setInterval(Integer.parseInt(interval) * 1000);

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

        this.configuration.setCols(Integer.parseInt(cols));

        return this;
    }

    @Override
    public ConfigurationBuilder setRowsForCommands(String rowsForCommands) {
        rowsForCommands = CliConfigurationBuilder.toValue(rowsForCommands);

        this.configuration.setRowsForCommands(Integer.parseInt(rowsForCommands));

        return this;
    }

    @Override
    public ConfigurationBuilder setDevicePerishability(String devicePerishability) {
        devicePerishability = CliConfigurationBuilder.toValue(devicePerishability);

        this.configuration.setDevicePerishability(Integer.parseInt(devicePerishability));

        return this;
    }

    @Override
    public ConfigurationBuilder setKMax(String kMax) {
        kMax = CliConfigurationBuilder.toValue(kMax);

        this.configuration.setkMax(Integer.parseInt(kMax));

        return this;
    }

    @Override
    public ConfigurationBuilder setKMin(String kMin) {
        kMin = CliConfigurationBuilder.toValue(kMin);

        this.configuration.setkMin(Integer.parseInt(kMin));

        return this;
    }

    @Override
    public ConfigurationBuilder setKPov(String kPov) {
        kPov = CliConfigurationBuilder.toValue(kPov);

        this.configuration.setkPov(Integer.parseInt(kPov));

        return this;
    }

}
