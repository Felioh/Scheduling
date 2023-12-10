package de.ohnes.AlgorithmicComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.ohnes.util.Instance;
import de.ohnes.util.Job;
import de.ohnes.util.Machine;
import de.ohnes.util.MyMath;

public class TransformInstance {
    
    /**
     * Tranform an instance like described in Alon et al. (2.1) such that:
     *      (1) size(I) = m(I)
     *      (2) the processing time of each job in I belongs to [\epsilon, 1]
     *      (3) there exists an optimal solution for I such that the load of each machine belongs to [1/2, 2]
     * where size(I) is the total processing time of jobs in I.
     * @param I the instance to be transformed.
     * @param lambda should be 1/epsilon. to ensure the processing time of each job in I belongs to [\epsilon, 1].
     */
    public static void transformInstance(Instance I, final int lambda) {
        List<Job> jobs = Arrays.asList(I.getJobs());
        List<Job> newJobs = new ArrayList<>();
        int nb_machines = I.getM();
        //the value L as defined in Alon.
        double L = jobs.stream().map(Job::getP).mapToDouble(Double::doubleValue).sum() / nb_machines;
        //recursively delete all big jobs that are to be scheduled on a single machine. (see Alon et al. Observation 2.1)
        do {
            int nb_jobs = jobs.size();
            jobs = removeBigJobs(jobs, L);
            nb_machines -= nb_jobs - jobs.size(); //update number of machines because the big jobs should be scheduled on individual machines.
            L = jobs.stream().map(Job::getP).mapToDouble(Double::doubleValue).sum() / nb_machines;
        } while (containsBigJobs(jobs, L));

        // Note: from here on L is an upper bound for the processing time of each job in I.

        final double LDivLambdaSquared = L / (lambda * lambda);
        final double LDivLambda = L / lambda;
        // find all big jobs
        Stream<Job> bigJobs = jobs.stream().filter(j -> j.getP() > LDivLambda);
        
        //the processing time of all big jobs gets rounded up to the next multiple of L/lamba^2
        bigJobs.forEach(j -> {
            double newP = MyMath.roundUp(j.getP(), LDivLambdaSquared);
            newJobs.add(new Job(newP, j.getId()));
        });
        
        //add small jobs.
        double S = jobs.stream().filter(j -> j.getP() <= LDivLambda).map(Job::getP).mapToDouble(Double::doubleValue).sum();
        double pTimeSmallJobs = 0.0;
        while (pTimeSmallJobs < S) {
            newJobs.add(new Job(LDivLambda, 0)); //just give all small jobs the index 0... TODO
            pTimeSmallJobs += LDivLambda;
        }
        
        double maxP = MyMath.roundUp(L, LDivLambdaSquared);
        assert newJobs.stream().allMatch(p -> p.getP() <= maxP);
        assert newJobs.stream().allMatch(p -> p.getP() >= LDivLambda);

        //skale the instance so that every job length is in [\epsilon, 1]
        for (Job job : newJobs) {
            double newP = job.getP() / maxP;
            assert newP <= 1.0;
            job.setP(newP);
        }

        //update the instance object I
        Machine[] machines = new Machine[nb_machines];
        for (int i = 0; i < nb_machines; i++) {
            machines[i] = new Machine();
        }
        I.setMachines(machines);
        I.setJobs(newJobs.toArray(new Job[0]));
    }

    private static boolean containsBigJobs(List<Job> jobs, final double L) {
        return jobs.stream().anyMatch(j -> j.getP() > L);
    }

    private static List<Job> removeBigJobs(List<Job> jobs, final double L) {
        return jobs.stream().filter(j -> j.getP() <= L).collect(Collectors.toList());
    }
}
