package de.ohnes.AlgorithmicComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ohnes.util.Instance;
import de.ohnes.util.Interval;
import de.ohnes.util.Job;
import de.ohnes.util.Machine;
import de.ohnes.util.MyMath;
import de.ohnes.util.State2;
import de.ohnes.util.States2;

/**
 * Algorithm 2 from the Chen, et. al. paper. As described on p.278
 * This algorithm is to be applied to intances with a number of machines
 * (\sqrt{1/\epsilon} < m <= \frac{1}_{\epsilon^2})
 */
public class Algorithm2 implements Algorithm {

    private static final Logger LOGGER = LogManager.getLogger(Algorithm1.class);

    @Override
    public void solve(Instance I, double epsilon, int q) {

        Interval[] pTimeIntervals = MyMath.getPTimeIntervals(epsilon);

        Machine[] machines = I.getMachines();

        Job[] jobs = I.getJobs();

        List<List<Job>> G_h_jobs = new ArrayList<>();
        //TODO make smarter and faster
        for (Interval interval : pTimeIntervals) {
            G_h_jobs.add(Arrays.stream(jobs).filter(j -> interval.contains(j.getP())).sorted(Comparator.comparing(Job::getP).reversed()).collect(Collectors.toList()));
        }

        //check that all jobs got assigned to an interval
        assert G_h_jobs.stream().map(List::size).reduce(0, Integer::sum) == jobs.length;

        States2[] F = new States2[I.getM() + 1];
        for (int i = 0; i <= I.getM(); i++) {
            F[i] = new States2();
        }
        F[0].add(new State2(0, 0, new int[pTimeIntervals.length]));


        for (int i = 1; i <= I.getM(); i++) {
            //consider all valid states in F[i-1] and try all possible assignments
            Stack<State2> prev_states = new Stack<>();
            Stack<State2> next_states = new Stack<>();
            prev_states.addAll(F[i-1].getStates());

            for (int h = 0; h < G_h_jobs.size(); h++) { //for all intervalls
                //try all possible additions of jobs from interval h
                while (!prev_states.isEmpty()) {
                    State2 prev_state = prev_states.pop();
                    next_states.add(prev_state); //TODO dont use stack but list.
                    
                    //TODO: Make sure that the G_h jobs are sorted correctly.
                    if (prev_state.getU()[h] >= G_h_jobs.get(h).size()) {
                        continue;
                    }
                    Job nextJob = G_h_jobs.get(h).get(prev_state.getU()[h]);
                    // for (int hj = prev_state.getU()[h]; hj < G_h_jobs.get(h).size(); hj++) { //TODO start at index.
                    if (prev_state.getAllotments().size() > i-1 && prev_state.getAllotments().get(i-1).getJobs().contains(nextJob)) {
                        continue;   //skip, if job is already alloted to state.
                    }
                    State2 new_state = prev_state.getNextState(i-1, h, q, nextJob);
                    if (new_state.getAllotments().get(i-1).getLoad() > 0.5 && new_state.getAllotments().get(i-1).getLoad() < 2) {
                        F[i].add(new_state);
                    }
                    if (new_state.getAllotments().get(i-1).getLoad() < 2) {
                        prev_states.add(new_state);
                    }
                    // }
                }
                prev_states.addAll(next_states);
                next_states.clear();
            }
        }
        //find the state with minimum objective in F_m
        double minOb = Double.MAX_VALUE;
        State2 minState = null;
        for (State2 state : F[I.getM()].getStates()) {
            if (Arrays.stream(state.getU()).sum() < jobs.length) {
                continue;
            }
            double ob = state.getCost(q);
            if (ob < minOb) {
                minOb = ob;
                minState = state;
            }
        }

        LOGGER.info("Found solution with objective value {}", minOb);
        I.setMachines(minState.getAllotments().toArray(new Machine[0]));


    }

    
}
