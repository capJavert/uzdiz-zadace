/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.components;

import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.DeviceEnum;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javert
 */
public class UzDizCSVAdapter extends CSVHelper implements CSVAdapter {

    private final File placesFile;
    private final File actuatorsFile;
    private final File sensorsFile;

    private List<List<String>> readCsv(File file) {
        List<List<String>> collection = new ArrayList<>();
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(fileInputStream, "UTF-8");
            CSVHelper.parseLine(reader); // TODO log header handling
            List<String> values = CSVHelper.parseLine(reader);
            while (values != null) {
                collection.add(values);
                values = CSVHelper.parseLine(reader);
            }

        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getInstance().log("CSV file path '" + file.getAbsolutePath() + "' not valid", true);
        } catch (Exception ex) {
            Logger.getInstance().log(ex.getMessage(), true);
        } finally {
            try {
                fileInputStream.close();
            } catch (Exception ex) {
                Logger.getInstance().log(ex.getMessage(), true);
            }
        }

        return collection;
    }

    public UzDizCSVAdapter(String placesFilePath, String actuatorsFilePath, String sensorsFilePath) {
        this.placesFile = new File(placesFilePath);
        this.actuatorsFile = new File(actuatorsFilePath);
        this.sensorsFile = new File(sensorsFilePath);
    }

    @Override
    public List<Place> getPlaces() {
        List<Place> places = new ArrayList<>();

        List<List<String>> collection = this.readCsv(this.placesFile);
        for (List<String> values : collection) {
            try {
                if(values.get(0).equals("") 
                        || values.get(0) == null
                        || Integer.parseInt(values.get(1)) < 0 
                        || Integer.parseInt(values.get(1)) > 1) {
                    
                    Logger.getInstance().log("Kurac", true);
                }
                
                if (values.get(2) == null) {
                    values.set(2, "0");
                }

                if (values.get(3) == null) {
                    values.set(3, "0");
                }
                
                Place place = new Place(
                        values.get(0),
                        Integer.parseInt(values.get(1)),
                        Integer.parseInt(values.get(2)),
                        Integer.parseInt(values.get(3))
                );

                places.add(place);
            } catch (NumberFormatException ex) {
                Logger.getInstance().log(ex.getMessage(), true);
                Logger.getInstance().log("Line is not valid. Skipping Place...", true);
            }
        }

        return places;
    }

    @Override
    public List<Device> getSensors() {
        List<Device> sensors = new ArrayList<>();
        DeviceFactory factory = DeviceFactory.getFactory(DeviceEnum.SENSOR);

        List<List<String>> collection = this.readCsv(this.sensorsFile);
        for (List<String> values : collection) {
            try {
                sensors.add(factory.createToF(values));
            } catch (Exception ex) {
                Logger.getInstance().log("Line is not valid. Skipping Sensor...", true);
            }
        }

        return sensors;
    }

    @Override
    public List<Device> getActuators() {
        List<Device> actuators = new ArrayList<>();
        DeviceFactory factory = DeviceFactory.getFactory(DeviceEnum.ACTUATOR);

        List<List<String>> collection = this.readCsv(this.actuatorsFile);
        for (List<String> values : collection) {
            try {
                actuators.add(factory.createToF(values));
            } catch (Exception ex) {
                Logger.getInstance().log("Line is not valid. Skipping Actuator...", true);
            }
        }

        return actuators;
    }
}
