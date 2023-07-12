package de.ohnes.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
public class State1 {

    private double[] loads;

    private int h;
    
    @Setter
    private List<List<Job>> allotments;
    // private HashMap<Job, Integer> allotments = new HashMap<>();

    public State1(int h, int m) {
        this.loads = new double[m];
        this.h = h;
        this.allotments = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            this.allotments.add(i, new ArrayList<>());
        }
    }

    public double getLoad(int id) {
        return this.loads[id-1];
    }
    
    public void setLoad(int id, double load) {
        this.loads[id-1] = load;
    }
    
    public List<Job> getAllotmentOnMachine(int id) {
        return this.allotments.get(id - 1);
    }

    public void setAllotmentOnMachine(List<Job> allotment, int id) {
        this.allotments.set(id - 1, allotment);
    }
    
    public void addJob(Job job, int machine) {
        if (this.allotments.get(machine) == null) {
            this.allotments.set(machine, new ArrayList<>());
        }
        this.allotments.get(machine).add(job);
    }

    /**
     * This method will return a new State object representing the state if a job with processing time
     * @param p_j were to be scheduled on machine @param id
     * @return the new State
     */
    public State1 getNextState(int id, Job job) {
        State1 newState = new State1(this.h + 1, this.loads.length);
        for(int i = 0; i < this.loads.length; i++) {
            newState.getLoads()[i] = this.loads[i];
            if (i == id - 1) {
                newState.getLoads()[i] += job.getP();
            }
        }
        List<List<Job>> newAllotments = new ArrayList<>();
        for (int i = 0; i < this.allotments.size(); i++) {
            //order is kept, because of the for-loop
            newAllotments.add(new ArrayList<>(this.allotments.get(i)));
        }
        newAllotments.get(id - 1).add(job);
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

    public boolean isEmpty() {
        return this.allotments.stream().allMatch(l -> l.isEmpty());
    }
    
}
