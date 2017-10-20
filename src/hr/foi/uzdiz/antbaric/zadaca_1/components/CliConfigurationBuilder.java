/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.components;

import hr.foi.uzdiz.antbaric.zadaca_1.models.Configuration;


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
        this.configuration.setSeed(Integer.parseInt(seed));
        
        return this;
    }
    
    @Override
    public ConfigurationBuilder setPlacesFilePath(String placesFilePath) {
        this.configuration.setPlacesFilePath(placesFilePath);
        
        return this;
    }

    @Override
    public ConfigurationBuilder setSensorsFilePath(String sensorsFilePath) {
        this.configuration.setSensorsFilePath(sensorsFilePath);
        
        return this;
    }

    @Override
    public ConfigurationBuilder setActuatorsFielPath(String actuatorsFielPath) {
        this.configuration.setActuatorsFielPath(actuatorsFielPath);
        
        return this;
    }

    @Override
    public ConfigurationBuilder setAlgoritham(String algoritham) {
        this.configuration.setAlgoritham(algoritham);
        
        return this;
    }

    @Override
    public ConfigurationBuilder setInterval(String interval) {
        this.configuration.setInterval(Integer.parseInt(interval)*1000);
        
        return this;
    }

    @Override
    public ConfigurationBuilder setExecutionLimit(String executionLimit) {
        this.configuration.setExecutionLimit(Integer.parseInt(executionLimit));
        
        return this;
    }

    @Override
    public ConfigurationBuilder setOutFilePath(String outFilePath) {
        this.configuration.setOutFilePath(outFilePath);
        
        return this;
    }
    
}
