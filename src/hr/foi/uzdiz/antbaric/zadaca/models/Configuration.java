/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

/**
 *
 * @author javert
 */
public class Configuration {
    private Long seed;
    private String placesFilePath;
    private String sensorsFilePath;
    private String actuatorsFielPath;
    private String algoritham;
    private Integer interval;
    private Integer executionLimit;
    private String outFilePath; 

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public String getPlacesFilePath() {
        return placesFilePath;
    }

    public void setPlacesFilePath(String placesFilePath) {
        this.placesFilePath = placesFilePath;
    }

    public String getSensorsFilePath() {
        return sensorsFilePath;
    }

    public void setSensorsFilePath(String sensorsFilePath) {
        this.sensorsFilePath = sensorsFilePath;
    }

    public String getActuatorsFielPath() {
        return actuatorsFielPath;
    }

    public void setActuatorsFielPath(String actuatorsFielPath) {
        this.actuatorsFielPath = actuatorsFielPath;
    }

    public String getAlgoritham() {
        return algoritham;
    }

    public void setAlgoritham(String algoritham) {
        this.algoritham = algoritham;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getExecutionLimit() {
        return executionLimit;
    }

    public void setExecutionLimit(Integer executionLimit) {
        this.executionLimit = executionLimit;
    }

    public String getOutFilePath() {
        return outFilePath;
    }

    public void setOutFilePath(String outFilePath) {
        this.outFilePath = outFilePath;
    }
    
    
}
