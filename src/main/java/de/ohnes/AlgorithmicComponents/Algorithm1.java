package de.ohnes.AlgorithmicComponents;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ohnes.util.Instance;
import de.ohnes.util.Interval;
import de.ohnes.util.Job;
import de.ohnes.util.Machine;
import de.ohnes.util.MyMath;
import de.ohnes.util.State;
import de.ohnes.util.States;

/**
 * Algorithm 1 from the Chen, et. al. paper. As described on p.289
 * This algorithm is to be applied to intances with a small number of machines
 * (m <= \sqrt{1/\epsilon})
 */
public class Algorithm1 implements Algorithm {

    private static final Logger LOGGER = LogManager.getLogger(Algorithm1.class);

    @Override
    public void solve(Instance I, double epsilon, int q) {

        double delta = epsilon / I.getN();

        Interval[] big_gamma = MyMath.getLoadIntervals(epsilon, delta, 2);
        
        Job[] jobs = I.getJobs();


        States[] F_hat = new States[I.getN() + 1];
        //initialization, maybe not needed?
        for (int i = 0; i < F_hat.length; i++) {
            F_hat[i] = new States();
        }
        F_hat[0].add(new State(0, I.getM())); //initial state F^_0

        LOGGER.debug("starting computation of states.");

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

            State roundedState = new State(j, I.getM());
            for (Interval[] S : MyMath.getPermutationsOfLength(big_gamma, I.getM(), Interval.class)) {

                for (State state : MyMath.getCut(S, F_prime_j)) {
                    for (int i = 1; i <= I.getM(); i++) {
                        //L_i = max{L_i : (..., L_i, ...) \in F' \cut S}
                        //find the maximum L in the cut of F and S
                        double L_max = 0; //0 is the minimum.
                        List<Job> partialAllotment = new ArrayList<>();
                        if (state.getLoad(i) > L_max) {
                            L_max = state.getLoad(i);
                            partialAllotment = state.getAllotmentOnMachine(i);
                        }
                        roundedState.setLoad(i, L_max);
                        roundedState.setAllotmentOnMachine(partialAllotment, i);
                    }
                }

                if (!roundedState.isEmpty()) {
                    F_hat[j].add(roundedState);
                    roundedState = new State(j, I.getM());
                }
            }

            LOGGER.debug("Computed set of states for n = {}, out of {}", j, I.getN());
        }

        //find the allotment with minimum objective. in F_hat_n
        double minOb = Double.MAX_VALUE;
        State minState = null;
        for (State state : F_hat[I.getN()].getStates()) {
            double ob = state.getCost(q);
            if (ob < minOb) {
                minOb = ob;
                minState = state;
            }
        }

        //rebuild the solution
        LOGGER.info("Found solution with objective value {}", minOb);

        Machine[] machines = I.getMachines();
        for (int i = 1; i <= I.getM(); i++) {
            Machine machine = machines[i - 1];
            minState.getAllotments().get(i - 1).stream().forEach(j -> machine.addJob(j));
        }
    }
    
}
