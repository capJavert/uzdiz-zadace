/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1.iterators;

/**
 *
 * @author javert
 */
public class UEntry<K, V> {
    private K key;
    private V value;

    public UEntry(K getKey, V getValue) {
        this.key = getKey;
        this.value = getValue;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
    
}
