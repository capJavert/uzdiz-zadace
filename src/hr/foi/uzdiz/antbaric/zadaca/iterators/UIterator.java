/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.iterators;

/**
 *
 * @author javert
 */
public interface UIterator<E> {
    Boolean hasNext();
    E next();
    void set(E item);
}
