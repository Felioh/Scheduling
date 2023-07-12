package de.ohnes.AlgorithmicComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
            for (State2 state : F[i-1].getStates()) {
                //try all possible assignments on the new machine i
                // List<List<Job>> remaining_G_h_jobs = new ArrayList<>();
                // for (List<Job> G_h : G_h_jobs) {
                //     remaining_G_h_jobs.add(G_h.stream().sorted(Comparator.comparing(Job::getP).reversed()).skip(state.getU_I(i)).collect(Collectors.toList()));
                // }
                int[] newU = new int[G_h_jobs.size()]; //the allotment on machine i
                int h = 0;
                while (h < G_h_jobs.size()) {
                    Machine newMachine = new Machine(); //the machine i
                    Job nextJob = G_h_jobs.get(h).get(newU[h] + state.getU()[h]);
                    while (newMachine.getLoad() + nextJob.getP() < 2) {
                        newMachine.addJob(nextJob);
                        newU[h]++;

                        if (newMachine.getLoad() > 0.5) {
                            F[i].add(state.getNextState(newMachine.clone(), newU, q));
                        }
                        //TODO: add all possible assignements respecting lemma 2.2 and lemma 2.3
                    }

                    h++;
                }
            }
        }

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solve'");
    }

    
}
