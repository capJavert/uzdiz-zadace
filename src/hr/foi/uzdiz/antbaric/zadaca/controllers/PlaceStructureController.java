/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.controllers;

import hr.foi.uzdiz.antbaric.zadaca.Worker;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.views.PlaceStructureView;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author javert
 */
public class PlaceStructureController extends Controller<PlaceStructureView, Integer> {

    public PlaceStructureController(PlaceStructureView view, Integer model) {
        super(view, model);
    }

    @Override
    public void init() {
        super.init();

        Boolean exists = false;

        for (Map.Entry<Integer, Place> entry : Worker.getInstance().PLACES.entrySet()) {
            if (Objects.equals(entry.getValue().id, this.model)) {
                Logger.getInstance().log(new LMessage("\n"), true);
                        
                if (entry.getValue().getSubPlaces().size() > 0) {
                    Logger.getInstance().log(new LMessage("──┬ " + entry.getValue().getNameAndId()), true);
                } else {
                    Logger.getInstance().log(new LMessage("─── " + entry.getValue().getNameAndId()), true);
                }
                
                Integer i = 0;
                for (Map.Entry<Integer, Place> subEntry : entry.getValue().getSubPlaces().entrySet()) {
                    if (i+1 == entry.getValue().getSubPlaces().size()) {
                        Logger.getInstance().log(new LMessage("  └── " + subEntry.getValue().getNameAndId()), true);
                    } else {
                        Logger.getInstance().log(new LMessage("  ├── " + subEntry.getValue().getNameAndId()), true);
                    }
                    i++;
                }
                
                exists = true;
                break;
            }
        }

        if (!exists) {
            Logger.getInstance().log(new LError("Place with that ID does not exist!"), true);
        }
    }

}
