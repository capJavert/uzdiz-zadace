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
    Configuration build() throws IllegalArgumentException;

    ConfigurationBuilder setSeed(String seed);
    
    ConfigurationBuilder setPlacesFilePath(String placesFilePath);
    
    ConfigurationBuilder setSensorsFilePath(String sensorsFilePath);
    
    ConfigurationBuilder setActuatorsFilePath(String actuatorsFielPath);
    
    ConfigurationBuilder setScheduleFilePath(String actuatorsFielPath);
    
    ConfigurationBuilder setInterval(String interval);
    
    ConfigurationBuilder setRows(String rows);
    
    ConfigurationBuilder setCols(String cols);
    
    ConfigurationBuilder setRowsForCommands(String rowsForCommands);
    
    ConfigurationBuilder setDevicePerishability(String devicePerishability);

    Boolean isValid();

}
