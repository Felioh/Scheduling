package de.ohnes.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This is a state as described in Chen, Tao p.290; Algorithm2
 * The state can be expressed as a vector (i, v, u_1, ..., u_tau), this is a valid state if:
 * it is possible to assign the u_h largest jobs in G_h on the first i machines,
 * with objective value equal to v.
 */
@Getter
@AllArgsConstructor
public class State2 {
    
    private int i;

    private double v;

    private int[] u;

    @Setter
    private List<Machine> allotments = new ArrayList<>();

    public State2(int i, double v, int[] u) {
        this.i = i;
        this.v = v;
        this.u = u;
    }

    public int getU_I(int i) {
        return this.u[i - 1];
    }

    /**
     * 
     * @param i the index of the machine
     * @param h the index of the intervall that had another job alloted
     * @param q
     * @return
     */
    public State2 getNextState(int i, int h, int q, Job job) {
        if (h > this.u.length -1) throw new IllegalArgumentException();
        int[] u = new int[this.u.length];
        System.arraycopy(this.u, 0, u, 0, this.u.length);
        u[h]++;

        List<Machine> newAllotments = copyAllotment();
        if (newAllotments.size() - 1 < i) {
            Machine machine = new Machine();
            newAllotments.add(machine);
        }
        newAllotments.get(i).addJob(job);

        //compute new objective function.
        double v = newAllotments.stream().map(m -> Math.pow(m.getLoad(), q)).reduce(0.0, Double::sum);

        return new State2(i + 1, v, u, newAllotments);
    }

    public double getCost(int q) {
        double res = 0;
        for (Machine i : allotments) {
            res += Math.pow(i.getLoad(), q);
        }
        return res;
    }

    private List<Machine> copyAllotment() {
        List<Machine> newAllotment = new ArrayList<>();
        for (Machine oldMachine : this.allotments) {
            Machine machine = new Machine();
            machine.getJobs().addAll(oldMachine.getJobs());
            newAllotment.add(machine);
        }
        return newAllotment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        
        if (!(o instanceof State2)) {
            return false;
        }

        State2 state2 = (State2) o;
        return Arrays.equals(this.u, state2.getU());
    }
}
