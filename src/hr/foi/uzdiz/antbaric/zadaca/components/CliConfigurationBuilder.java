/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.components;

import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CliConfigurationBuilder implements ConfigurationBuilder {

    private final Configuration configuration;

    public CliConfigurationBuilder() {
        this.configuration = new Configuration();
    }

    @Override
    public Configuration build() {
        return this.configuration;
    }

    @Override
    public ConfigurationBuilder setSeed(String seed) {
        this.configuration.setSeed(Long.parseLong(seed));

        return this;
    }

    @Override
    public ConfigurationBuilder setPlacesFilePath(String placesFilePath) {
        this.configuration.setPlacesFilePath(absolutePath(placesFilePath));

        return this;
    }

    @Override
    public ConfigurationBuilder setSensorsFilePath(String sensorsFilePath) {
        this.configuration.setSensorsFilePath(absolutePath(sensorsFilePath));

        return this;
    }

    @Override
    public ConfigurationBuilder setActuatorsFielPath(String actuatorsFielPath) {
        this.configuration.setActuatorsFielPath(absolutePath(actuatorsFielPath));

        return this;
    }

    @Override
    public ConfigurationBuilder setAlgoritham(String algoritham) {
        this.configuration.setAlgoritham(algoritham);

        return this;
    }

    @Override
    public ConfigurationBuilder setInterval(String interval) {
        this.configuration.setInterval(Integer.parseInt(interval) * 1000);

        return this;
    }

    @Override
    public ConfigurationBuilder setExecutionLimit(String executionLimit) {
        this.configuration.setExecutionLimit(Integer.parseInt(executionLimit));

        return this;
    }

    @Override
    public ConfigurationBuilder setOutFilePath(String outFilePath) {
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

}
