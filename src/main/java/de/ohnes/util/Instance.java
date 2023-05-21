package de.ohnes.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Instance {

    private Job[] jobs;

    private Machine[] machines;

    public int getN() {
        return jobs.length;
    }
    
    public int getM() {
        return machines.length;
    }
}
