package de.ohnes.AlgorithmicComponents;

import java.util.HashSet;

import de.ohnes.util.Instance;
import de.ohnes.util.Interval;
import de.ohnes.util.Job;
import de.ohnes.util.MyMath;
import de.ohnes.util.State;
import de.ohnes.util.States;

/**
 * Algorithm 1 from the Chen, et. al. paper. As described on p.289
 */
public class Algorithm1 implements Algorithm {

    @Override
    public void solve(Instance I, double epsilon) {

        double delta = epsilon / I.getN();

        Interval[] big_gamma = MyMath.getLoadIntervals(epsilon, delta);
        
        Job[] jobs = I.getJobs();

        States[] F_hat = new States[I.getN()];
        //initialization, maybe not needed?
        for (int i = 0; i < F_hat.length; i++) {
            F_hat[i] = new States();
        }
        F_hat[0].add(new State(0, I.getM())); //initial state F^_0

        for (int j = 1; j <= I.getN(); j++) {
            States F_prime_j = new States();

            for (State prevState : F_hat[j - 1].getStates()) {
                for (int i = 1; i <= I.getM(); i++) {
                    double p_j = jobs[j-1].getP();
                    if (prevState.getLoad(i) + p_j > 2) {  //L_i + p_j <= 2
                        continue;
                    }
                    F_prime_j.add(prevState.getNextState(i, p_j));
                }
            }

            for (Interval[] S : MyMath.findPowerSet(big_gamma, Interval.class)) {
                State newState = new State(j, I.getM());
                for (int i = 1; i <= I.getM(); i++) {
                    //L_i = max{L_i : (..., L_i, ...) \in F' \cut S}
                    //find the maximum L in the cut of F and S
                    for (State state : F_prime_j.getStates()) {
                        //TODO !!
                    }
                }
                F_hat[j].add(newState);
            }
        }
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solve'");
    }
    
}
