package com.example.androidprojecttemplate.models;
import android.util.Pair;

public class Pairs<U, V> extends Pair<U, V> {
    private U first;
    private V second;

    public Pairs(U first, V second) {
        super(first, second);
        /*
          this.first = first;
          this.second = second;
         */
    }

    public U getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}