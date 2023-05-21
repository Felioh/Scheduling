package de.ohnes.util;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Instance {

    private List<Job> jobs;

    private List<Machine> machines;

    public int getN() {
        return jobs.size();
    }
    
    public int getM() {
        return machines.size();
    }
}
