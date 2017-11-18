/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.components;

import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;

/**
 *
 * @author javert
 */
public interface ConfigurationBuilder {
    Configuration build();

    ConfigurationBuilder setSeed(String seed);
    
    ConfigurationBuilder setPlacesFilePath(String placesFilePath);
    
    ConfigurationBuilder setSensorsFilePath(String sensorsFilePath);
    
    ConfigurationBuilder setActuatorsFielPath(String actuatorsFielPath);
    
    ConfigurationBuilder setAlgoritham(String algoritham);
    
    ConfigurationBuilder setInterval(String interval);

    ConfigurationBuilder setExecutionLimit(String executionLimit);

    ConfigurationBuilder setOutFilePath(String outFilePath);

}
