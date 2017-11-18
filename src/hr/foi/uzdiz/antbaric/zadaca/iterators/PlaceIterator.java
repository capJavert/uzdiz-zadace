/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.iterators;

import hr.foi.uzdiz.antbaric.zadaca.components.Generator;
import hr.foi.uzdiz.antbaric.zadaca.models.AlgorithmEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author javert
 */
public class PlaceIterator implements UContainer<Place> {

    protected Place places[];

    @Override
    public UIterator getIterator(AlgorithmEnum type) {
        switch (type) {
            case SEQUENTIAL:
                return new SequentialIterator();
            case RANDOM:
                return new RandomIterator();
            case INDEX:
                return new IndexIterator();
            default:
                return new SequentialIterator();
        }
    }

    @Override
    public void add(Place item) {
        this.places[this.places.length] = item;
    }

    private class SequentialIterator implements UIterator<UEntry> {

        private Integer identifier = 0;
        private Integer index = 0;
        private Integer next;

        @Override
        public Boolean hasNext() {
            return this.index < places.length;
        }

        @Override
        public UEntry next() {
            if (!this.hasNext()) {
                return null;
            }

            this.next = null;

            for (int i = 0; i < places.length; i++) {
                if (this.next != null && (places[i].getId() < this.identifier || places[this.next].getId() < places[i].getId())) {
                    continue;
                }

                this.next = i;
            }

            this.index++;
            this.identifier = places[this.next].getId();

            return new UEntry<>(places[this.next].getName(), places[this.next]);
        }

        @Override
        public void set(UEntry item) {
            places[this.next] = (Place) item.getValue();
        }

    }

    private class RandomIterator implements UIterator<UEntry> {

        private Integer index = 0;
        private final List<Integer> identifiers;

        RandomIterator() {
            this.identifiers = Generator.getInstance().getUsedIdentifiers();
            Collections.shuffle(this.identifiers);
        }

        @Override
        public Boolean hasNext() {
            return this.index < places.length;
        }

        @Override
        public UEntry next() {
            return new UEntry<>(places[this.identifiers.get(this.index++)].getName(), places[this.identifiers.get(this.index++)]);
        }

        @Override
        public void set(UEntry item) {
            places[this.index] = (Place) item.getValue();
        }

    }

    private class IndexIterator implements UIterator<UEntry> {

        private Integer index = 0;

        @Override
        public Boolean hasNext() {
            return this.index < places.length;
        }

        @Override
        public UEntry next() {
            if (!this.hasNext()) {
                return null;
            }

            return new UEntry<>(places[this.index++].getName(), places[this.index++]);
        }

        @Override
        public void set(UEntry item) {
            places[this.index] = (Place) item.getValue();
        }

    }

}
