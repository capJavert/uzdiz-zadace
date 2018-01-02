/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.components;

import hr.foi.uzdiz.antbaric.zadaca.helpers.CSVHelper;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.DeviceEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
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
public class CSVParser extends CSVHelper {

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
            Logger.getInstance().log(new LError("CSV file path '" + file.getAbsolutePath() + "' not valid"), true);
        } catch (Exception ex) {
            Logger.getInstance().log(new LError(ex.getMessage()), true);
        } finally {
            try {
                fileInputStream.close();
            } catch (Exception ex) {
                Logger.getInstance().log(new LError(ex.getMessage()), true);
            }
        }

        return collection;
    }

    public CSVParser(String placesFilePath, String actuatorsFilePath, String sensorsFilePath) {
        this.placesFile = new File(placesFilePath);
        this.actuatorsFile = new File(actuatorsFilePath);
        this.sensorsFile = new File(sensorsFilePath);
    }

    public List<Place> getPlaces() {
        List<Place> places = new ArrayList<>();

        List<List<String>> collection = this.readCsv(this.placesFile);
        for (List<String> values : collection) {
            try {
                if (values.get(1).equals("")
                        || values.get(1) == null
                        || Integer.parseInt(values.get(2)) < 0
                        || Integer.parseInt(values.get(2)) > 1) {
                }

                if (values.get(3) == null) {
                    values.set(3, "0");
                }

                if (values.get(4) == null) {
                    values.set(4, "0");
                }

                Place place = new Place(
                        Integer.parseInt(values.get(0)),
                        values.get(1),
                        Integer.parseInt(values.get(2)),
                        Integer.parseInt(values.get(3)),
                        Integer.parseInt(values.get(4))
                );

                Boolean duplicate = false;

                for (Place p : places) {
                    if (p.getName().equals(place.getName())) {
                        duplicate = true;
                        break;
                    }
                }

                if (duplicate) {
                    Logger.getInstance().log(new LError("Duplicate '" + place.getName() + "'! Skipping Place..."), true);
                } else {
                    if (Generator.USED_IDENTIFIERS_PLACES < place.id) {
                        Generator.USED_IDENTIFIERS_PLACES = place.id;
                    }

                    places.add(place);
                }
            } catch (NumberFormatException ex) {
                Logger.getInstance().log(new LError("Line is not valid. Skipping Place..."), true);
            }
        }

        return places;
    }

    public List<Device> getSensors() {
        List<Device> sensors = new ArrayList<>();
        DeviceFactory factory = DeviceFactory.getFactory(DeviceEnum.SENSOR);

        List<List<String>> collection = this.readCsv(this.sensorsFile);
        for (List<String> values : collection) {
            try {
                sensors.add(factory.createToF(values));
            } catch (Exception ex) {
                Logger.getInstance().log(new LError("Line is not valid. Skipping Sensor..."), true);
            }

            if (Generator.USED_IDENTIFIERS_SENSORS < Integer.parseInt(values.get(0))) {
                Generator.USED_IDENTIFIERS_SENSORS = Integer.parseInt(values.get(0));
            }
        }

        return sensors;
    }

    public List<Device> getActuators() {
        List<Device> actuators = new ArrayList<>();
        DeviceFactory factory = DeviceFactory.getFactory(DeviceEnum.ACTUATOR);

        List<List<String>> collection = this.readCsv(this.actuatorsFile);
        for (List<String> values : collection) {
            try {
                actuators.add(factory.createToF(values));
            } catch (Exception ex) {
                Logger.getInstance().log(new LError("Line is not valid. Skipping Actuator..."), true);
            }

            if (Generator.USED_IDENTIFIERS_ACTUATORS < Integer.parseInt(values.get(0))) {
                Generator.USED_IDENTIFIERS_ACTUATORS = Integer.parseInt(values.get(0));
            }
        }

        return actuators;
    }
    
    public void assignDevices() {
        // TODO implement
    }
}
