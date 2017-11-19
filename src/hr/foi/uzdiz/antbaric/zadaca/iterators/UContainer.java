/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.iterators;

import hr.foi.uzdiz.antbaric.zadaca.models.AlgorithmEnum;
import java.util.List;

/**
 *
 * @author javert
 */
public interface UContainer<E> {
    public UIterator getIterator(AlgorithmEnum type);
    public void add(List<E> item);
}
