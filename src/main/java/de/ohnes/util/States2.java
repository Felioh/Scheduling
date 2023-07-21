package de.ohnes.util;

import java.util.HashSet;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class States2 {

    private HashSet<State2> states = new HashSet<>();

    public void add(State2 state) {
        this.states.add(state);
    }

    public boolean contains(State2 state) {
        return states.contains(state);
    }
    
}
