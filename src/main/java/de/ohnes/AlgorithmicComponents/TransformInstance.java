package de.ohnes.AlgorithmicComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import de.ohnes.util.Instance;
import de.ohnes.util.Job;
import de.ohnes.util.MyMath;

public class TransformInstance {
    
    /**
     * Tranform an instance like described in Alon et al. such that:
     *      (1) size(I) = m(I)
     *      (2) the processing time of each job in I belongs to [\epsilon, 1]
     *      (3) there exists an optimal solution for I such that the load of each machine belongs to [1/2, 2]
     * where size(I) is the total processing time of jobs in I.
     * @param I the instance to be transformed.
     * @param lambda
     */
    public static void transformInstance(Instance I, final int lambda) {
        List<Job> newJobs = new ArrayList<Job>();
        
        //the value L as defined in Alon.
        double L = Arrays.stream(I.getJobs()).map(Job::getP).mapToDouble(Double::doubleValue).sum() / I.getM();
        double roundTerm = L / (lambda * lambda);
        Stream<Job> bigJobs = Arrays.stream(I.getJobs()).filter(j -> j.getP() > L / lambda);
        
        //the processing time of all big jobs gets rounded up to the next multiple of L/lamba^2
        bigJobs.forEach(j -> {
            double newP = MyMath.roundUp(j.getP(), roundTerm);
            newJobs.add(new Job(newP, j.getId()));
        });

        double S = Arrays.stream(I.getJobs()).filter(j -> j.getP() <= L / lambda).map(Job::getP).mapToDouble(Double::doubleValue).sum();
        double S_sharp = MyMath.roundUp(S, L / lambda);

        // S_sharp * (L/lambda) =^ The integer multiple of L that S was rounded to. TODO just calculate the value to avoid rounding errors!!
        double p_small = L / lambda; 
        for (int i = 0; i < S_sharp*(lambda/L); i++) {
            newJobs.add(new Job(p_small, i));       //TODO deal with ids !!!
        }

        I.setJobs(newJobs.toArray(Job[]::new));
    }
}
