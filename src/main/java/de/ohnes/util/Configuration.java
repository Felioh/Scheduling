package de.ohnes.util;

public class Configuration {

    /**
     * this is a tuple, where the ith element idicates 
     * how many job of processing time p_i are scheduled in the configuraion.
     */
    private int[] allottment;

    /**
     * create a new configuration
     * @param x the number of distinct processing times
     */
    public Configuration(int x) {
        this.allottment = new int[x];
    }

    public int get(int i) {
        return this.allottment[i];
    }

    
}
