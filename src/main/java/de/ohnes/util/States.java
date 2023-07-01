package de.ohnes.util;

import java.util.HashSet;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class States {

    private HashSet<State> states = new HashSet<>();

    public void add(State state) {
        this.states.add(state);
    }
    
}
