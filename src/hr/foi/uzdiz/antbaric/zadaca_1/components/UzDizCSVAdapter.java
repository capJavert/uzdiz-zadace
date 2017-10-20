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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author javert
 */
public class UzDizCSVAdapter extends CSVHelper implements CSVAdapter {

    private final File placesFile;
    private final File actuatorsFile;
    private final File sensorsFile;

    private List<List<String>> readCsv(File file) throws FileNotFoundException {
        List<List<String>> collection = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(file);

        try {
            Reader reader = new InputStreamReader(fileInputStream, "UTF-8");
            CSVHelper.parseLine(reader); // TODO add header handling
            List<String> values = CSVHelper.parseLine(reader);
            while (values != null) {
                collection.add(values);
                values = CSVHelper.parseLine(reader);
            }

        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(UzDizCSVAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UzDizCSVAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(UzDizCSVAdapter.class.getName()).log(Level.SEVERE, null, ex);
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
    public HashMap<String, Place> getPlaces() {
        HashMap<String, Place> places = new HashMap<>();

        try {
            List<List<String>> collection = this.readCsv(this.placesFile);

            System.out.println(collection);
            
            for (List<String> values : collection) {
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

                places.put(place.getName(), place);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UzDizCSVAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return places;
    }

    @Override
    public List<Device> getSensors() {
        List<Device> sensors = new ArrayList<>();
        DeviceFactory factory = DeviceFactory.getFactory(DeviceEnum.SENSOR);

        try {
            List<List<String>> collection = this.readCsv(this.sensorsFile);

            for (List<String> values : collection) {
                sensors.add(factory.createToF(values));
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(UzDizCSVAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sensors;
    }

    @Override
    public List<Device> getActuators() {
        List<Device> actuators = new ArrayList<>();
        DeviceFactory factory = DeviceFactory.getFactory(DeviceEnum.ACTUATOR);

        try {
            List<List<String>> collection = this.readCsv(this.actuatorsFile);

            for (List<String> values : collection) {
                actuators.add(factory.createToF(values));
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(UzDizCSVAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return actuators;
    }
}
