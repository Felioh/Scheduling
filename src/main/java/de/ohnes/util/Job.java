package de.ohnes.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Job {
    
    private double p;
    private int id;
    private double s = -1;

    public Job(double p, int id) {
        this.p = p;
        this.id = id;
    }
}
