package de.ohnes.AlgorithmicComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ohnes.util.Group;
import de.ohnes.util.Instance;
import de.ohnes.util.Job;

/**
 * Algorithm 3 from the Chen, et. al. paper. As described on p.290
 * This algorithm is to be applied to intances with a large number of machines
 * (\frac{1}_{\epsilon^2} < m)
 */
public class Algorithm3 implements Algorithm {

    private static final Logger LOGGER = LogManager.getLogger(Algorithm3.class);


    @Override
    public void solve(Instance I, double epsilon, int q) {

        int m = I.getM();

        //TODO while loop.
        //TODO deep copy jobs/instances.

        if (I.getN() <= I.getM()) {
            //TODO trivial..
        }

        //TODO check sorting
        List<Job> jobs = Arrays.stream(I.getJobs()).sorted(Comparator.comparing(Job::getP).reversed()).collect(Collectors.toList());
        assert jobs.get(0).getP() < jobs.get(1).getP(); //should always be at least 2 jobs.


        List<Group> harmonic_groups = new ArrayList<>(); //TODO other structure? stack?
        harmonic_groups.add(new Group());
        
        //group jobs.
        for (Job job : I.getJobs()) {
            if (harmonic_groups.get(harmonic_groups.size() - 1).isFull()) {
                harmonic_groups.add(new Group());
            }
            harmonic_groups.get(harmonic_groups.size() - 1).addJob(job);
        }

        //discard certain jobs. p.293
        int lastSize = harmonic_groups.get(0).getSize();
        for (int i = 1; i < harmonic_groups.size(); i++) {
            int currentSize = harmonic_groups.get(i).getSize();
            int itemsToRemove = lastSize - currentSize;
            //the job inside the groups are ordered by processing time due to the order in which they were filled.
            harmonic_groups.get(i).removeNJobs(itemsToRemove);
            lastSize = currentSize; //TODO check.
        }
        harmonic_groups.remove(0); //TODO: just completely remove the first group?

        for (Group group : harmonic_groups) {
            group.liftP();
        }




        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solve'");
    }
    
}
