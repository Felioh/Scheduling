package de.ohnes.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Interval {
    
    private double begin;

    private double end;

    public boolean isIncluded(double n) {
        return n >= this.begin && n < end;
    }
}
