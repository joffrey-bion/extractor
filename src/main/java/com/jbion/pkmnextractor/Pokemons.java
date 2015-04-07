package com.jbion.pkmnextractor;

import java.util.LinkedList;

public class Pokemons extends LinkedList<Pokemon> {

    public Pokemon get(Pokemon p) {
        int i = indexOf(p);
        if (i == -1) {
            return null;
        }
        return get(i);
    }
    
    public LinkedList<Pokemon> getSimilar(Pokemon p) {
        LinkedList<Pokemon> results = new LinkedList<>();
        for (Pokemon old : this) {
            if (old.isSimilarTo(p)) {
                results.add(old);
            }
        }
        return results;
    }
    
}
