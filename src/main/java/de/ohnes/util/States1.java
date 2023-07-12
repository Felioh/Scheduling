package de.ohnes.util;

import java.util.HashSet;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class States1 {

    private HashSet<State1> states = new HashSet<>();

    public void add(State1 state) {
        this.states.add(state);
    }
    
}
