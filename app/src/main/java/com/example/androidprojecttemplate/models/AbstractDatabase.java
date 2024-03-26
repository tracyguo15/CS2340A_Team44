package com.example.androidprojecttemplate.models;

import java.util.Hashtable;

public class AbstractDatabase<K,V> {
    private Hashtable<K,V> table;

    public AbstractDatabase() {
        table = new Hashtable<>();
    }

    public V get(K key) {
        return table.get(key);
    }

    public V put(K key, V val) {
        return table.put(key, val);
    }

    public V remove(K key) {
        return table.remove(key);
    }

    public boolean contains(K key) {
        return table.contains(key);
    }

    public int size() {
        return table.size();
    }

    public Set keySet() {
        return table.keySet();
    }
}
