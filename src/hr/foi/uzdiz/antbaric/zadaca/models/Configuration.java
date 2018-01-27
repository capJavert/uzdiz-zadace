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
    private String scheduleFilePath;
    private Integer interval;
    private Integer loggerBufferSize;
    private Integer rows;
    private Integer cols;
    private Integer rowsForCommands;
    private Integer devicePerishability;
    private Integer kMax;
    private Integer kMin;
    private Integer kPov;

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

    public String getScheduleFilePath() {
        return scheduleFilePath;
    }

    public void setScheduleFilePath(String scheduleFilePath) {
        this.scheduleFilePath = scheduleFilePath;
    }

    public Integer getInterval() {
        return interval;
    }

    public Long getPreciseInterval() {
        return interval.longValue() * 1000000;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getLoggerBufferSize() {
        return loggerBufferSize;
    }

    public void setLoggerBufferSize(Integer loggerBufferSize) {
        this.loggerBufferSize = loggerBufferSize;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public Integer getRowsForCommands() {
        return rowsForCommands;
    }

    public void setRowsForCommands(Integer rowsForCommands) {
        this.rowsForCommands = rowsForCommands;
    }

    public Integer getDevicePerishability() {
        return devicePerishability;
    }

    public void setDevicePerishability(Integer devicePerishability) {
        this.devicePerishability = devicePerishability;
    }

    public Integer getkMax() {
        return kMax;
    }

    public void setkMax(Integer kMax) {
        this.kMax = kMax;
    }

    public Integer getkMin() {
        return kMin;
    }

    public void setkMin(Integer kMin) {
        this.kMin = kMin;
    }

    public Integer getkPov() {
        return kPov;
    }

    public void setkPov(Integer kPov) {
        this.kPov = kPov;
    }

    
    
}
