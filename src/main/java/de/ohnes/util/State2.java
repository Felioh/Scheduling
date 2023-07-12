package de.ohnes.util;

import java.sql.Array;
import java.util.ArrayList;
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
    private List<Machine> allotments;

    public State2(int i, double v, int[] u) {
        this.i = i;
        this.v = v;
        this.u = u;
    }

    public int getU_I(int i) {
        return this.u[i - 1];
    }

    public State2 getNextState(Machine machine, int[] u, int q) {
        if (u.length != this.u.length) throw new IllegalArgumentException();
        
        for (int i = 0; i < u.length; i++) {
            u[i] += this.u[i];
        }
        List<Machine> newAllotments = new ArrayList<>();
        newAllotments.addAll(allotments);
        newAllotments.add(machine);

        //compute new objective function.
        double v = newAllotments.stream().map(m -> Math.pow(m.getLoad(), q)).reduce(0.0, Double::sum);

        return new State2(i + 1, v, u, newAllotments);
    }

}
