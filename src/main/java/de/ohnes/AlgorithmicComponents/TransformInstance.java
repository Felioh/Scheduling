package de.ohnes.AlgorithmicComponents;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
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
        Job[] newJobs = new Job[I.getN()];
        
        Stream<Job> jobs = Arrays.stream(I.getJobs());

        //the value L as defined in Alon.
        double L = jobs.map(Job::getP).mapToDouble(Double::doubleValue).sum() / I.getM();
        double roundTerm = L / (lambda * lambda);
        Job[] bigJobs = jobs.filter(j -> j.getP() > L / lambda).toArray(Job[]::new);
        
        int i = 0;
        
        //the processing time of all big jobs gets rounded up to the next multiple of L/lamba^2
        for (; i < bigJobs.length; i++) {
            double newP = MyMath.roundUp(bigJobs[i].getP(), roundTerm);
            newJobs[i] = new Job(newP, bigJobs[i].getId());
        }
        double S = jobs.filter(j -> j.getP() <= L / lambda).map(Job::getP).mapToDouble(Double::doubleValue).sum();
        double S_sharp = MyMath.roundUp(S, L / lambda);

    }
}
