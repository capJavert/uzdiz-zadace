/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.iterators;

import hr.foi.uzdiz.antbaric.zadaca.models.AlgorithmEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public void add(List<Place> places) {
        this.places = new Place[places.size()];
        Integer i = 0;

        for (Place place : places) {
            this.places[i++] = place;
        }
    }

    private class SequentialIterator implements UIterator<UEntry> {

        private Integer identifier = 0;
        private Integer index = 0;
        private Integer next = null;

        @Override
        public Boolean hasNext() {
            if (places == null) {
                return false;
            }

            return this.index < places.length;
        }

        @Override
        public UEntry next() {
            if (!this.hasNext()) {
                return null;
            }

            Integer min = 1001;
            Integer nextIndex = 0;

            for (int i = 0; i < places.length; i++) {
                if (places[i].getId() < min && places[i].getId() > this.identifier) {
                    min = places[i].getId();
                    nextIndex = i;
                }
            }

            this.index++;
            this.next = nextIndex;
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
        private Integer next = 0;

        RandomIterator() {
            this.identifiers = IntStream.rangeClosed(0, places.length - 1).boxed().collect(Collectors.toList());
            Collections.shuffle(this.identifiers);
            System.out.println(this.identifiers);
        }

        @Override
        public Boolean hasNext() {
            if (places == null) {
                return false;
            }

            return this.index < places.length;
        }

        @Override
        public UEntry next() {
            if (!this.hasNext()) {
                return null;
            }

            this.next = this.index++;

            return new UEntry<>(places[this.identifiers.get(this.next)].getName(), places[this.identifiers.get(this.next)]);
        }

        @Override
        public void set(UEntry item) {
            places[this.identifiers.get(this.next)] = (Place) item.getValue();
        }

    }

    private class IndexIterator implements UIterator<UEntry> {

        private Integer index = 0;
        private Integer next = 0;

        @Override
        public Boolean hasNext() {
            if (places == null) {
                return false;
            }

            return this.index < places.length;
        }

        @Override
        public UEntry next() {
            if (!this.hasNext()) {
                return null;
            }

            this.next = this.index++;

            return new UEntry<>(places[this.next].getName(), places[this.next]);
        }

        @Override
        public void set(UEntry item) {
            places[this.next] = (Place) item.getValue();
        }

    }

}
