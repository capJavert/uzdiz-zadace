/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.components;

import hr.foi.uzdiz.antbaric.zadaca_1.models.Device;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Place;
import hr.foi.uzdiz.antbaric.zadaca_1.models.DeviceEnum;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            CSVHelper.parseLine(reader); // TODO add header handling
            List<String> values = CSVHelper.parseLine(reader);
            while (values != null) {
                collection.add(values);
                values = CSVHelper.parseLine(reader);
            }

        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getInstance().add("CSV file path '"+file.getAbsolutePath()+"' not valid", true);
        } catch (Exception ex) {
           Logger.getInstance().add(ex.getMessage(), true);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                Logger.getInstance().add(ex.getMessage(), true);
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
    public List<Map.Entry<String, Place>> getPlaces() {
        List<Map.Entry<String, Place>> places = new ArrayList<>();

        List<List<String>> collection = this.readCsv(this.placesFile);
        for (List<String> values : collection) {
            if (values.get(2) == null) {
                values.set(2, "0");
            }
            
            if (values.get(3) == null) {
                values.set(3, "0");
            }
            
            try {
                Place place = new Place(
                        values.get(0),
                        Integer.parseInt(values.get(1)),
                        Integer.parseInt(values.get(2)),
                        Integer.parseInt(values.get(3))
                );
                
                places.add(new AbstractMap.SimpleEntry<>(place.getName(), place));
            } catch (NumberFormatException ex) {
                Logger.getInstance().add("Line is not valid. Skipping...", true);
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
            } catch (NumberFormatException ex) {
                Logger.getInstance().add("Line is not valid. Skipping...", true);
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
            } catch (NumberFormatException ex) {
                Logger.getInstance().add("Line is not valid. Skipping...", true);
            }
        }

        return actuators;
    }
}
