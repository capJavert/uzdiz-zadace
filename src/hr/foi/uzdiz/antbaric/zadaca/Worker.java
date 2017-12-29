/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca;

import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.components.CSVParser;
import hr.foi.uzdiz.antbaric.zadaca.extensions.Stateful;
import hr.foi.uzdiz.antbaric.zadaca.iterators.PlaceIterator;
import hr.foi.uzdiz.antbaric.zadaca.iterators.UEntry;
import hr.foi.uzdiz.antbaric.zadaca.iterators.UIterator;
import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca.models.AlgorithmEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LInfo;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.LWarning;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 *
 * @author javert
 */
public class Worker extends Stateful {

    private static volatile Worker INSTANCE;
    private Configuration CONFIG = null;
    public PlaceIterator PLACES;
    public List<Device> SENSORS;
    public List<Device> ACTUATORS;
    private HashMap<String, Integer> FAILED_DEVICES;

    static {
        INSTANCE = new Worker();
    }

    private Worker() {
    }

    public static Worker getInstance() {
        return INSTANCE;
    }

    public Configuration getConfig() {
        return CONFIG;
    }

    public void setConfig(Configuration config) {
        CONFIG = config;
    }

    public void run(Integer n) {
        for (Integer i = 0; i < n; i++) {
            Long startTimestamp = System.currentTimeMillis();

            Logger.getInstance().log(new LMessage("Working, interval #" + (i + 1)), true);

            for (final UIterator<UEntry<String, Place>> iterator = PLACES.getIterator(AlgorithmEnum.INDEX); iterator.hasNext();) {
                final UEntry<String, Place> entry = iterator.next();
                Place place = entry.getValue();
                List<Device> sensors = this.getSensorsByCategory(place.getCategory());
                Logger.getInstance().log(new LMessage("Checking Place '" + place.getName() + "'"), true);

                for (final ListIterator<Device> deviceIterator = place.getSensors().listIterator(); deviceIterator.hasNext();) {
                    Sensor sensor = (Sensor) deviceIterator.next();

                    if (!this.activateDevice(place.getName(), sensor)) {
                        place.SENSORS_TRASH.add(sensor);
                        deviceIterator.remove();
                        Logger.getInstance().log(new LWarning("Replacing Sensor '" + sensor.getName() + "'"), true);
                        sensor = (Sensor) sensor.prototype();

                        if (sensor.getStatus() == 1) {
                            deviceIterator.add(sensor);
                            Logger.getInstance().log(new LMessage("Init OK '" + sensor.getName() + "'"), true);
                        } else {
                            Logger.getInstance().log(new LError("Init FAILED '" + sensor.getName() + "'"), true);
                        }
                    }
                }

                for (final ListIterator<Device> deviceIterator = place.getActuators().listIterator(); deviceIterator.hasNext();) {
                    Actuator actuator = (Actuator) deviceIterator.next();

                    if (actuator.isSensorChanged()) {
                        if (!this.activateDevice(place.getName(), actuator)) {
                            place.ACTUATORS_TRASH.add(actuator);
                            deviceIterator.remove();
                            Logger.getInstance().log(new LMessage("Replacing Actuator '" + actuator.getName() + "'"), true);
                            actuator = (Actuator) actuator.prototype();

                            if (actuator.getStatus() == 1) {
                                deviceIterator.add(actuator);
                                Logger.getInstance().log(new LMessage("Init OK '" + actuator.getName() + "'"), true);
                            } else {
                                Logger.getInstance().log(new LError("Init FAILED '" + actuator.getName() + "'"), true);
                            }
                        }
                    } else {
                        Logger.getInstance().log(new LMessage("No change to sensors Actuator '" + actuator.getName() + "'"), true);
                        actuator.activate();
                        FAILED_DEVICES.put(place.getName() + "." + actuator.getName(), 0);
                    }

                }

                entry.setValue(place);
                iterator.set(entry);

                if (this.isLimitExceeded(startTimestamp)) {
                    Logger.getInstance().log(new LWarning("Thread interval exceeded! Stopping execution"), true);
                    break;
                }
            }
        }

        Logger.getInstance().log(new LInfo("Writing log to output file..."), true);
        Logger.getInstance().writeToFile();
    }

    public void setUp() {
        CSVParser adapter = new CSVParser(CONFIG.getPlacesFilePath(), CONFIG.getActuatorsFielPath(), CONFIG.getSensorsFilePath());
        Logger.getInstance().log(new LMessage("Reading CSV files..."), true);
        PLACES = new PlaceIterator();
        PLACES.add(adapter.getPlaces());
        SENSORS = adapter.getSensors();
        ACTUATORS = adapter.getActuators();
        FAILED_DEVICES = new HashMap<>();

        Logger.getInstance().log(new LMessage("Worker Thread started"), true);

        this.configSystem();
        this.initSystem();
    }

    public void configSystem() {
        for (final UIterator<UEntry<String, Place>> iterator = PLACES.getIterator(AlgorithmEnum.INDEX); iterator.hasNext();) {
            final UEntry<String, Place> entry = iterator.next();
            Place place = entry.getValue();
            place.setId();
            List<Device> sensors = this.getSensorsByCategory(place.getCategory());
            List<Device> actuators = this.getActuatorsByCategory(place.getCategory());
            Logger.getInstance().log(new LMessage("Placing devices at '" + place.getName() + "'"), true);

            if (!SENSORS.isEmpty()) {
                for (Integer i = 0; i < place.getSensorsNum(); i++) {
                    place.addSensor(sensors.get(Generator.getInstance().selectFrom(sensors)).prototype());
                }
            }

            if (!ACTUATORS.isEmpty()) {
                for (Integer i = 0; i < place.getActuatorsNum(); i++) {
                    place.addActuator(actuators.get(Generator.getInstance().selectFrom(actuators)).prototype());
                }
            }

            entry.setValue(place);
            iterator.set(entry);
        }
    }

    public void initSystem() {
        for (final UIterator<UEntry<String, Place>> iterator = PLACES.getIterator(AlgorithmEnum.INDEX); iterator.hasNext();) {
            final UEntry<String, Place> entry = iterator.next();
            Place place = entry.getValue();
            List<Device> sensors = this.getSensorsByCategory(place.getCategory());
            Logger.getInstance().log(new LMessage("Init devices at '" + place.getName() + " ID: " + place.getId() + "'"), true);

            for (final ListIterator<Device> deviceIterator = place.getSensors().listIterator(); deviceIterator.hasNext();) {
                final Device sensor = deviceIterator.next();

                if (sensor.getStatus() == 0) {
                    place.SENSORS_TRASH.add(sensor);
                    deviceIterator.remove();
                    Logger.getInstance().log(new LError("  Init FAILED '" + sensor.getNameAndId() + "'"), true);
                } else {
                    FAILED_DEVICES.put(place.getName() + "." + sensor.getName(), 0);
                    Logger.getInstance().log(new LMessage("  Init OK '" + sensor.getNameAndId() + "'"), true);
                }
            }

            for (final ListIterator<Device> deviceIterator = place.getActuators().listIterator(); deviceIterator.hasNext();) {
                final Device actuator = deviceIterator.next();

                if (actuator.getStatus() == 0) {
                    place.ACTUATORS_TRASH.add(actuator);
                    deviceIterator.remove();
                    Logger.getInstance().log(new LError("  Init FAILED '" + actuator.getNameAndId() + "'"), true);
                } else {
                    FAILED_DEVICES.put(place.getName() + "." + actuator.getName(), 0);
                    Logger.getInstance().log(new LMessage("  Init OK '" + actuator.getNameAndId() + "'"), true);

                    this.configActuator((Actuator) actuator, place.getSensors());
                }
            }

            entry.setValue(place);
            iterator.set(entry);
        }
    }

    private void configActuator(Actuator actuator, List<Device> sensors) {
        Logger.getInstance().log(new LMessage("  Assigning Sensors to Actuator '" + actuator.getNameAndId() + "'"), true);

        if (sensors.size() > 0) {
            for (int i = 0; i < Generator.getInstance().fromInterval(1, sensors.size() + 1); i++) {
                while (true) {
                    Sensor sensor = (Sensor) sensors.get(Generator.getInstance().selectFrom(sensors));

                    if (!actuator.sensors.contains(sensor)) {
                        actuator.sensors.add(sensor);
                        Logger.getInstance().log(new LMessage("      Added Sensor '" + sensor.getNameAndId() + "' to Actuator '" + actuator.getNameAndId() + "'"), true);
                        break;
                    }
                }
            }
        } else {
            Logger.getInstance().log(new LMessage("  Actuator '" + actuator.getNameAndId() + "' has no assigned sensors"), true);
        }
    }

    private List<Device> getSensorsByCategory(Integer category) {
        List<Device> sensors = new ArrayList<>();

        for (Device sensor : SENSORS) {
            if (Objects.equals(sensor.getType(), category) || Objects.equals(sensor.getType(), 2)) {
                sensors.add(sensor);
            }
        }

        return sensors;
    }

    private List<Device> getActuatorsByCategory(Integer category) {
        List<Device> actuators = new ArrayList<>();

        for (Device actuator : ACTUATORS) {
            if (Objects.equals(actuator.getType(), category) || Objects.equals(actuator.getType(), 2)) {
                actuators.add(actuator);
            }
        }

        return actuators;
    }

    private Boolean activateDevice(String placeName, Device device) {
        String deviceId = placeName + "." + device.getName();
        Integer overload = FAILED_DEVICES.get(deviceId);

        if (device.getStatus() == 0) {
            overload = overload == null ? 1 : overload + 1;

            if (overload.equals(3)) {
                FAILED_DEVICES.remove(deviceId);
                Logger.getInstance().log(new LWarning("Device '" + device.getName() + "ID: " + device.getId() + "'@'" + placeName + "' need replacement..."), true);

                return false;
            }
        } else {
            overload = 0;
            device.activate();
        }

        FAILED_DEVICES.put(deviceId, overload);

        return true;
    }

    private Boolean isLimitExceeded(Long startTimestamp) {
        return System.currentTimeMillis() - startTimestamp > CONFIG.getPreciseInterval();
    }

    @Override
    public Object save() {
        return new State((Place[]) Worker.copy(this.PLACES.places));
    }

    @Override
    public void restore(Object o) {
        if (o instanceof State) {
            State s = (State) o;
            INSTANCE.PLACES.places = ((State) o).state;
        }
    }

    private static class State {

        private final Place state[];

        public State(Place[] state) {
            this.state = state;
        }

        public Place[] getSavedState() {
            return state;
        }
    }

    public static Object copy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
                out.writeObject(orig);
                out.flush();
            }

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logger.getInstance().log(new LError(e.getMessage()), Boolean.TRUE);
        }

        return obj;
    }
}
