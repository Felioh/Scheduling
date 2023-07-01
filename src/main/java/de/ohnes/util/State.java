package de.ohnes.util;

import lombok.Getter;

@Getter
public class State {

    private double[] loads;

    private int h;

    public State(int h, int m) {
        this.loads = new double[m];
        this.h = h;
    }

    public double getLoad(int id) {
        return this.loads[id-1];
    }

    /**
     * This method will return a new State object representing the state if a job with processing time
     * @param p_j were to be scheduled on machine @param id
     * @return the new State
     */
    public State getNextState(int id, double p_j) {
        State newState = new State(this.h + 1, this.loads.length);
        for(int i = 0; i < this.loads.length; i++) {
            newState.getLoads()[i] = this.loads[i];
            if (i == id - 1) {
                newState.getLoads()[i] += p_j;
            }
        }
        return newState;
    }
    
}
