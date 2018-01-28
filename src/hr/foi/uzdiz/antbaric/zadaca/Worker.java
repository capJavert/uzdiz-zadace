/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca;

import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.components.CSVParser;
import hr.foi.uzdiz.antbaric.zadaca.extensions.Stateful;
import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.DeviceInventoryItem;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.LNotification;
import hr.foi.uzdiz.antbaric.zadaca.models.LWarning;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 *
 * @author javert
 */
public class Worker extends Stateful {

    private static volatile Worker INSTANCE;
    private Configuration CONFIG = null;
    public ConcurrentHashMap<Integer, Place> PLACES;
    public ConcurrentHashMap<Integer, Device> SENSORS;
    public ConcurrentHashMap<Integer, Device> ACTUATORS;
    public ConcurrentHashMap<Integer, DeviceInventoryItem> SENSOR_INVENTORY;
    public ConcurrentHashMap<Integer, DeviceInventoryItem> ACTUATOR_INVENTORY;
    private ConcurrentHashMap<String, Integer> FAILED_DEVICES;

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

            for (Map.Entry<Integer, Place> entry : PLACES.entrySet()) {
                Place place = entry.getValue();
                Logger.getInstance().log(new LMessage("Checking Place '" + place.getId() + "'"), true);

                for (Map.Entry<Integer, Device> hEntry : place.getSensors().entrySet()) {
                    Sensor sensor = (Sensor) hEntry.getValue();

                    if (!this.activateDevice(place.getId(), sensor)) {
                        place.SENSORS_TRASH.add(sensor);
                        place.getSensors().remove(sensor.id);
                        Logger.getInstance().log(new LWarning("Replacing Sensor '" + sensor.getId() + "'"), true);

                        DeviceInventoryItem inventoryItem = Worker.getInstance().SENSOR_INVENTORY.get(sensor.getModelId());

                        if (inventoryItem != null) {
                            inventoryItem.decrement(sensor.id);

                            if (inventoryItem.isFull()) {
                                Logger.getInstance().log(new LWarning("Inventory is full!"), Boolean.TRUE);
                                inventoryItem.makeOrder();
                            }

                            if (!inventoryItem.isFull()) {
                                inventoryItem.count += 1;
                                sensor = (Sensor) sensor.prototype(0);

                                if (sensor.getStatus() == 1) {
                                    place.getSensors().put(sensor.id, sensor);
                                    Logger.getInstance().log(new LMessage("Init OK '" + sensor.getId() + "'"), true);
                                } else {
                                    Logger.getInstance().log(new LError("Init FAILED '" + sensor.getId() + "'"), true);
                                }
                            } else {
                                Logger.getInstance().log(new LWarning("Can't replace sensor! There is no available sensors!"), Boolean.TRUE);
                            }
                        } else {
                            Logger.getInstance().log(new LError("Inventory for device model missing!"), true);
                        }
                    }
                }

                for (Map.Entry<Integer, Device> hEntry : place.getActuators().entrySet()) {
                    Actuator actuator = (Actuator) hEntry.getValue();

                    if (actuator.isSensorChanged()) {
                        if (!this.activateDevice(place.getId(), actuator)) {
                            place.ACTUATORS_TRASH.add(actuator);
                            place.getActuators().remove(actuator.id);
                            Logger.getInstance().log(new LMessage("Replacing Actuator '" + actuator.getId() + "'"), true);

                            DeviceInventoryItem inventoryItem = Worker.getInstance().ACTUATOR_INVENTORY.get(actuator.getModelId());

                            if (inventoryItem != null) {
                                inventoryItem.decrement(actuator.id);

                                if (inventoryItem.isFull()) {
                                    Logger.getInstance().log(new LWarning("Inventory is full!"), Boolean.TRUE);
                                    inventoryItem.makeOrder();
                                }

                                if (!inventoryItem.isFull()) {
                                    inventoryItem.count += 1;
                                    actuator = (Actuator) actuator.prototype(0);

                                    if (actuator.getStatus() == 1) {
                                        place.getActuators().put(actuator.id, actuator);
                                        Logger.getInstance().log(new LMessage("Init OK '" + actuator.getId() + "'"), true);
                                    } else {
                                        Logger.getInstance().log(new LError("Init FAILED '" + actuator.getId() + "'"), true);
                                    }
                                } else {
                                    Logger.getInstance().log(new LWarning("Can't replace actuator! There is no available actuators!"), Boolean.TRUE);
                                }
                            } else {
                                Logger.getInstance().log(new LError("Inventory for device model missing!"), true);
                            }
                        }
                    } else {
                        Logger.getInstance().log(new LMessage("No change to sensors Actuator '" + actuator.getId() + "'"), true);
                        actuator.activate();
                        FAILED_DEVICES.put(place.getId() + "." + actuator.getId(), 0);
                    }

                }

                entry.setValue(place);

                if (this.isLimitExceeded(startTimestamp)) {
                    Logger.getInstance().log(new LWarning("Thread interval exceeded! Stopping execution"), true);
                    break;
                }
            }

            for (Map.Entry<Integer, DeviceInventoryItem> inventoryEntry : this.SENSOR_INVENTORY.entrySet()) {
                for (Map.Entry<Integer, Integer> deviceEntry : inventoryEntry.getValue().failedCount.entrySet()) {
                    Integer fixCyclesLeft = deviceEntry.getValue();
                    fixCyclesLeft -= 1;
                    deviceEntry.setValue(fixCyclesLeft);

                    if (fixCyclesLeft == 0) {
                        Logger.getInstance().log(new LNotification("Device with ID: " + deviceEntry.getKey() + " is fixed!"), true);
                    }
                }

                inventoryEntry.getValue().failedCount.entrySet().removeIf(entry -> !inventoryEntry.getValue().failedCount.contains(0));
            }

            for (Map.Entry<Integer, DeviceInventoryItem> inventoryEntry : this.ACTUATOR_INVENTORY.entrySet()) {
                for (Map.Entry<Integer, Integer> deviceEntry : inventoryEntry.getValue().failedCount.entrySet()) {
                    Integer fixCyclesLeft = deviceEntry.getValue();
                    fixCyclesLeft -= 1;
                    deviceEntry.setValue(fixCyclesLeft);

                    if (fixCyclesLeft == 0) {
                        Logger.getInstance().log(new LNotification("Device with ID: " + deviceEntry.getKey() + " is fixed!"), true);
                    }
                }

                inventoryEntry.getValue().failedCount.entrySet().removeIf(entry -> !inventoryEntry.getValue().failedCount.contains(0));
            }
        }

        Logger.getInstance().log(new LNotification("Worker Thread finished!"), true);
        // Logger.getInstance().writeToFile();
    }

    public void setUp() {
        CSVParser adapter = new CSVParser(CONFIG.getPlacesFilePath(), CONFIG.getActuatorsFielPath(), CONFIG.getSensorsFilePath(), CONFIG.getScheduleFilePath());
        Logger.getInstance().log(new LMessage("Reading CSV files..."), true);
        SENSOR_INVENTORY = new ConcurrentHashMap<>();
        ACTUATOR_INVENTORY = new ConcurrentHashMap<>();
        PLACES = adapter.getPlaces();
        SENSORS = adapter.getSensors();
        ACTUATORS = adapter.getActuators();
        FAILED_DEVICES = new ConcurrentHashMap<>();

        Logger.getInstance().log(new LMessage("Assigning devices..."), true);
        adapter.mapDevices(new ArrayList<>(), Boolean.FALSE);
        Logger.getInstance().log(new LMessage("Init system..."), true);
        this.initSystem();
    }

    public void initSystem() {
        for (Map.Entry<Integer, Place> entry : PLACES.entrySet()) {
            Place place = entry.getValue();

            Logger.getInstance().log(new LMessage("Init devices at '" + place.getName() + " ID: " + place.getId() + "'"), true);

            for (Map.Entry<Integer, Device> hEntry : place.getSensors().entrySet()) {
                Device sensor = hEntry.getValue();

                if (sensor.getStatus() == 0) {
                    place.SENSORS_TRASH.add(sensor);
                    place.getSensors().remove(sensor.id);
                    Logger.getInstance().log(new LError("  Init FAILED '" + sensor.getId() + "'"), true);
                } else {
                    FAILED_DEVICES.put(place.getId() + "." + sensor.getId(), 0);
                    Logger.getInstance().log(new LMessage("  Init OK '" + sensor.getId() + "'"), true);
                }
            }

            for (Map.Entry<Integer, Device> hEntry : place.getActuators().entrySet()) {
                Device actuator = hEntry.getValue();

                if (actuator.getStatus() == 0) {
                    place.ACTUATORS_TRASH.add(actuator);
                    place.getActuators().remove(actuator.id);
                    Logger.getInstance().log(new LError("  Init FAILED '" + actuator.getId() + "'"), true);
                } else {
                    FAILED_DEVICES.put(place.getId() + "." + actuator.getId(), 0);
                    Logger.getInstance().log(new LMessage("  Init OK '" + actuator.getId() + "'"), true);
                }
            }

            entry.setValue(place);
        }
    }

    private Boolean activateDevice(String placeName, Device device) {
        String deviceId = placeName + "." + device.getId();
        Integer overload = FAILED_DEVICES.get(deviceId);

        if (device.getStatus() == 0) {
            overload = overload == null ? 1 : overload + 1;

            if (overload.equals(3)) {
                FAILED_DEVICES.remove(deviceId);
                Logger.getInstance().log(new LWarning("Device '" + device.getId() + "@'" + placeName + "' need replacement..."), true);

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
        return new State(Worker.copy(this.PLACES));
    }

    @Override
    public void restore(Object o) {
        if (o instanceof State) {
            State s = (State) o;
            INSTANCE.PLACES = ((State) o).state;
        }
    }

    private static class State {

        private final ConcurrentHashMap<Integer, Place> state;

        public State(ConcurrentHashMap<Integer, Place> state) {
            this.state = state;
        }

        public ConcurrentHashMap<Integer, Place> getSavedState() {
            return state;
        }
    }

    @SuppressWarnings("unchecked")
    public static ConcurrentHashMap<Integer, Place> copy(Object orig) {
        ConcurrentHashMap<Integer, Place> obj = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
                out.writeObject(orig);
                out.flush();
            }

            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = (ConcurrentHashMap<Integer, Place>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logger.getInstance().log(new LError(e.getMessage()), Boolean.TRUE);
        }

        return obj;
    }
}
