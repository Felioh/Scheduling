package de.ohnes.util;

import java.util.Arrays;

import de.ohnes.Exceptions.StateOutOfBoudsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This is a state as described in Chen, Tao p.290; Algorithm2
 * The state can be expressed as a vector (i, v, u_1, ..., u_tau), this is a valid state if:
 * it is possible to assign the u_h largest jobs in G_h on the first i machines,
 * with objective value equal to v.
 * TODO: there is room for optimization with the datastructure. Work a bit more with just indicies rather than Objects.
 */
@Getter
@AllArgsConstructor
public class State2 {
    
    private int i;

    private double[] l;

    private int[] u;

    private State2 prev_state;

    // @Setter
    // private List<Machine> allotments = new ArrayList<>();

    public State2(int i, int[] u, int m) {
        this.i = i;
        this.u = u;
        this.l = new double[m];
    }

    public int getU_I(int i) {
        return this.u[i - 1];
    }

    /**
     * 
     * @param i the machine index.
     * @param h the g_h group that gets another job.
     * @param p
     * @return
     * @throws StateOutOfBoudsException
     */
    public State2 getNextState(int i, int h, double p) throws StateOutOfBoudsException {
        if (h > this.u.length -1) throw new IllegalArgumentException();

        if (l[i] + p >= 2) {
            throw new StateOutOfBoudsException("This state is not valid.");
        }

        double[] l = new double[this.l.length];
        System.arraycopy(this.l, 0, l, 0, this.l.length);
        l[i] += p;

        int[] u = new int[this.u.length];
        System.arraycopy(this.u, 0, u, 0, this.u.length);
        u[h]++;

        return new State2(i + 1, l, u, this);
    }

    public double getCost(int q) {
        double res = 0;
        for (double load : this.l) {
            res += Math.pow(load, q);
        }
        return res;
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
