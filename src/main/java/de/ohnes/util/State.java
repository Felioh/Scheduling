package de.ohnes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;

@Getter
public class State {

    private double[] loads;

    private int h;
    
    @Setter
    private HashMap<Job, Integer> allotments = new HashMap<>();

    public State(int h, int m) {
        this.loads = new double[m];
        this.h = h;
    }

    public double getLoad(int id) {
        return this.loads[id-1];
    }

    public void setLoad(int id, double load) {
        this.loads[id-1] = load;
    }

    public void addJob(Job job, int machine) {
        this.allotments.put(job, machine);
    }

    /**
     * This method will return a new State object representing the state if a job with processing time
     * @param p_j were to be scheduled on machine @param id
     * @return the new State
     */
    public State getNextState(int id, Job job) {
        State newState = new State(this.h + 1, this.loads.length);
        for(int i = 0; i < this.loads.length; i++) {
            newState.getLoads()[i] = this.loads[i];
            if (i == id - 1) {
                newState.getLoads()[i] += job.getP();
            }
        }
        HashMap<Job, Integer> newAllotments = new HashMap<>();
        for (Entry<Job, Integer> entry : this.allotments.entrySet()) {
            newAllotments.put(entry.getKey(), entry.getValue());
        }
        newAllotments.put(job, id);
        newState.setAllotments(newAllotments);

        return newState;
    }

    /**
     * return the cost of this State (\sum{(L_i)^q})
     * @return
     */
    public double getCost(int q) {
        double res = 0;
        for (double l : this.loads) {
            res += Math.pow(l, q);
        }
        return res;
    }
    
}
