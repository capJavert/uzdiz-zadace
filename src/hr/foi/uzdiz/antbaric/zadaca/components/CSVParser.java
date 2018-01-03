/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.components;

import hr.foi.uzdiz.antbaric.zadaca.Worker;
import hr.foi.uzdiz.antbaric.zadaca.helpers.CSVHelper;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.DeviceEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.DeviceMap;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.MapEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.PlaceDeviceMap;
import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

/**
 *
 * @author javert
 */
public class CSVParser extends CSVHelper {

    private final File placesFile;
    private final File actuatorsFile;
    private final File sensorsFile;
    private final File scheduleFilePath;

    private List<List<String>> readCsv(File file, Integer skipLines) {
        List<List<String>> collection = new ArrayList<>();
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(fileInputStream, "UTF-8");

            for (int i = 0; i < skipLines; i++) {
                CSVHelper.parseLine(reader);
            }

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

    public CSVParser(String placesFilePath, String actuatorsFilePath, String sensorsFilePath, String scheduleFilePath) {
        this.placesFile = new File(placesFilePath);
        this.actuatorsFile = new File(actuatorsFilePath);
        this.sensorsFile = new File(sensorsFilePath);
        this.scheduleFilePath = new File(scheduleFilePath);
    }

    public HashMap<Integer, Place> getPlaces() {
        HashMap<Integer, Place> places = new HashMap<>();

        List<List<String>> collection = this.readCsv(this.placesFile, 1);
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

                if (places.get(place.id) != null) {
                    Logger.getInstance().log(new LError("Duplicate '" + place.getName() + "'! Skipping Place..."), true);
                } else {
                    if (Generator.USED_IDENTIFIERS_PLACES < place.id) {
                        Generator.USED_IDENTIFIERS_PLACES = place.id;
                    }

                    places.put(place.id, place);
                }
            } catch (NumberFormatException ex) {
                Logger.getInstance().log(new LError("Line is not valid. Skipping Place..."), true);
            }
        }

        return places;
    }

    public HashMap<Integer, Device> getSensors() {
        HashMap<Integer, Device> sensors = new HashMap<>();
        DeviceFactory factory = DeviceFactory.getFactory(DeviceEnum.SENSOR);

        List<List<String>> collection = this.readCsv(this.sensorsFile, 1);
        for (List<String> values : collection) {
            try {
                Device device = factory.createToF(values);
                sensors.put(device.getModelId(), factory.createToF(values));
            } catch (Exception ex) {
                Logger.getInstance().log(new LError("Line is not valid. Skipping Sensor..."), true);
            }

            if (Generator.USED_IDENTIFIERS_SENSORS < Integer.parseInt(values.get(0))) {
                Generator.USED_IDENTIFIERS_SENSORS = Integer.parseInt(values.get(0));
            }
        }

        return sensors;
    }

    public HashMap<Integer, Device> getActuators() {
        HashMap<Integer, Device> actuators = new HashMap<>();
        DeviceFactory factory = DeviceFactory.getFactory(DeviceEnum.ACTUATOR);

        List<List<String>> collection = this.readCsv(this.actuatorsFile, 1);
        for (List<String> values : collection) {
            try {
                Device device = factory.createToF(values);
                actuators.put(device.getModelId(), factory.createToF(values));
            } catch (Exception ex) {
                Logger.getInstance().log(new LError("Line is not valid. Skipping Actuator..."), true);
            }

            if (Generator.USED_IDENTIFIERS_ACTUATORS < Integer.parseInt(values.get(0))) {
                Generator.USED_IDENTIFIERS_ACTUATORS = Integer.parseInt(values.get(0));
            }
        }

        return actuators;
    }

    public void mapDevices(List<Integer> lines, Boolean logErrors) {
        List<Integer> tempLines = new ArrayList<>();

        List<List<String>> collection = this.readCsv(this.scheduleFilePath, 3);

        int line = 0;

        for (List<String> values : collection) {
            if (lines.contains(line)) {
                tempLines.add(line);
                line++;
                continue;
            }

            try {
                DeviceMap map;

                switch (Integer.parseInt(values.get(0))) {
                    case 0:
                        switch (Integer.parseInt(values.get(2))) {
                            case 0:
                                map = new PlaceDeviceMap(
                                        MapEnum.PLACE_SENSOR,
                                        Integer.parseInt(values.get(1)),
                                        Integer.parseInt(values.get(3)),
                                        Integer.parseInt(values.get(4))
                                );

                                Device newSensor = Worker.getInstance().SENSORS.get(((PlaceDeviceMap) map).getModelFk());

                                if (newSensor != null) {
                                    newSensor = newSensor.prototype(map.getFk());
                                    Worker.getInstance().SENSORS.put(newSensor.id, newSensor);
                                    Worker.getInstance().PLACES.get(map.getPk()).addSensor(newSensor);
                                } else {
                                    throw new Exception("Device model does not exist! Skipping...");
                                }

                                break;
                            case 1:
                                map = new PlaceDeviceMap(
                                        MapEnum.PLACE_ACTUATOR,
                                        Integer.parseInt(values.get(1)),
                                        Integer.parseInt(values.get(3)),
                                        Integer.parseInt(values.get(4))
                                );

                                Device newActuator = Worker.getInstance().ACTUATORS.get(((PlaceDeviceMap) map).getModelFk());

                                if (newActuator != null) {
                                    newActuator = newActuator.prototype(map.getFk());
                                    Worker.getInstance().ACTUATORS.put(newActuator.id, newActuator);
                                    Worker.getInstance().PLACES.get(map.getPk()).addActuator(newActuator);
                                } else {
                                    throw new Exception("Device model does not exist! Skipping...");
                                }

                                break;
                            default:
                                throw new Exception("Device type must be 0 or 1. Skipping...");
                        }
                        break;
                    case 1:
                        for (String i : values.get(2).split(",")) {
                            map = new DeviceMap(
                                    MapEnum.ACTUATOR_SENSOR,
                                    Integer.parseInt(values.get(1)),
                                    Integer.parseInt(i)
                            );

                            Device actuator = this.findActuator(map.getPk());
                            Device sensor = this.findSensor(map.getFk());

                            if (actuator != null && sensor != null) {
                                ((Actuator) actuator).sensors.add((Sensor) sensor);
                            } else {
                                throw new Exception("Device pair #" + String.valueOf(map.getPk()) + " and #" + String.valueOf(map.getFk()) + " does not exist!");
                            }
                        }
                        break;
                    default:
                        throw new Exception("Line type must be 0 or 1. Skipping...");
                }

                tempLines.add(line);
            } catch (NumberFormatException ex) {
                Logger.getInstance().log(new LError(ex.getMessage()), true);
            } catch (Exception ex) {
                if (logErrors) {
                    Logger.getInstance().log(new LError(ex.getMessage()), true);
                }
            }
            
            line++;
        }

        if (tempLines.size() != lines.size()) {
            this.mapDevices(tempLines, false);
        } else if (!logErrors) {
            this.mapDevices(lines, Boolean.TRUE);
        }
    }

    private Device findSensor(Integer deviceId) {
        Device found = null;

        for (Map.Entry<Integer, Place> entry : Worker.getInstance().PLACES.entrySet()) {
            Place place = entry.getValue();

            for (Device device : place.getSensors()) {
                if (Objects.equals(device.id, deviceId)) {
                    found = device;
                    break;
                }
            }

            if (found != null) {
                return found;
            }
        }

        return null;
    }

    private Device findActuator(Integer deviceId) {
        Device found = null;

        for (Map.Entry<Integer, Place> entry : Worker.getInstance().PLACES.entrySet()) {
            Place place = entry.getValue();

            for (Device device : place.getActuators()) {
                if (Objects.equals(device.id, deviceId)) {
                    found = device;
                    break;
                }
            }

            if (found != null) {
                return found;
            }
        }

        return null;
    }

}
