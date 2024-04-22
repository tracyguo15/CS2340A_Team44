package com.example.androidprojecttemplate.models;

import java.util.Hashtable;
import java.util.Set;

public class AbstractDatabase<K, V> {
    private Hashtable<K, V> table;

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

    public boolean contains(V value) {
        return table.contains(value);
    }

    public boolean containsKey(K key) {
        return table.containsKey(key); }

    public int size() {
        return table.size();
    }

    public Set<K> keySet() {
        return table.keySet();
    }
}
