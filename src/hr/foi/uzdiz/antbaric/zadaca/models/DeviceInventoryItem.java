/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.models;

import hr.foi.uzdiz.antbaric.zadaca.Router;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author javert
 */
public class DeviceInventoryItem {

    public Integer count;
    public Integer availableCount;
    private Integer orders;
    private Integer replaceCount;
    public ConcurrentHashMap<Integer, Integer> failedCount;

    public DeviceInventoryItem() {
        this.count = 0;
        this.availableCount = Router.getConfig().getkMin();
        this.failedCount = new ConcurrentHashMap<>();
        this.orders = 0;
        this.replaceCount = 0;
    }

    public Boolean isFull() {
        return this.count >= this.availableCount;
    }
    
    public void decrement(Integer deviceId) {
        this.count -= 1;
        this.failedCount.put(deviceId, Router.getConfig().getDeviceFixInterval());
        this.replaceCount += 1;
        
        if (this.count < 0) {
            this.count = 0;
        }
    }

    public void makeOrder() {
        if (this.count >= Router.getConfig().getkMax()) {
            Logger.getInstance().log(new LWarning("Can't place order! Max Devices number is reached: " + Router.getConfig().getkMax()), Boolean.TRUE);
        } else {
            this.orders += 1;
            
            this.availableCount += Router.getConfig().getkPov();

            Logger.getInstance().log(new LNotification("Placing order for new devices: " + Router.getConfig().getkPov()), Boolean.TRUE);

            if (this.availableCount >= Router.getConfig().getkMax()) {
                Logger.getInstance().log(new LWarning("Devices received but maximum devices number reached: " + Router.getConfig().getkMax()), Boolean.TRUE);
                this.availableCount = Router.getConfig().getkMax();
            } else {
                Logger.getInstance().log(new LNotification("Devices received successfully!"), Boolean.TRUE);
            }
        }
    }

    public Integer getOrders() {
        return orders;
    }

    public Integer getReplaceCount() {
        return replaceCount;
    }

}
