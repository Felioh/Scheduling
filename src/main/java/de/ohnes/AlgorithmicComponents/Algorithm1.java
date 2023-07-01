package de.ohnes.AlgorithmicComponents;

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
                    if (prevState.getLoad(i) + jobs[j-1].getP() > 2) {  //L_i + p_j <= 2
                        continue;
                    }
                    F_prime_j.add(prevState.getNextState(i, jobs[j-1]));
                }
            }

            for (Interval[] S : MyMath.getPermutationsOfLength(big_gamma, I.getM(), Interval.class)) {
                State roundedState = new State(j, I.getM());
                for (int i = 1; i <= I.getM(); i++) {
                    //L_i = max{L_i : (..., L_i, ...) \in F' \cut S}
                    //find the maximum L in the cut of F and S
                    double L_max = 0; //0 is the minimum.
                    for (State state : F_prime_j.getStates()) {
                        if (MyMath.isInCut(S, state.getLoads())) {
                            if (state.getLoad(i) > L_max) {
                                L_max = state.getLoad(i);
                            }
                        }
                    }
                    roundedState.setLoad(i, L_max);
                }
                F_hat[j].add(roundedState);
            }
        }

        //find the allotment with minimum objective. in F_hat_n
        
    }
    
}
