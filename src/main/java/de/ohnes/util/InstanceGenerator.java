package de.ohnes.util;

import java.util.ArrayList;
import java.util.List;

public class InstanceGenerator {
    
    /**
     * 
     * @param minN minimum number of jobs
     * @param maxN maximum number of jobs (should be greater than maxM)
     * @param minM minimum number of jobs
     * @param maxM maximum number of jobs
     * @param maxP the maximum processing time for each job.
     * @return
     */
    public static Instance generate(int minN, int maxN, int minM, int maxM, int maxP) {
        int m = (int) (Math.random() * (maxM - minM) + minM);
        minN = Math.max(minN, m); //less jobs than machines is trivial.
        int n = (int) (Math.random() * (maxN - minN) + minN);

        Job[] jobs = new Job[n];
        for (int i = 0; i < n; i++) {
            double p = Math.random() * maxP;
            jobs[i] = new Job(p, i);
        }
        Machine[] machines = new Machine[m];
        for (int i = 0; i < m; i++) {
            machines[i] = new Machine();
        }

        return new Instance(jobs, machines);
    }

     /**
     * Generates an instance like described in Alon et al. (2.1) such that:
     *      (1) size(I) = m(I)
     *      (2) the processing time of each job in I belongs to [\epsilon, 1]
     *      (3) there exists an optimal solution for I such that the load of each machine belongs to [1/2, 2]
     * where size(I) is the total processing time of jobs in I.
     * The number of jobs in the constructed instance will be at least M (and a multiple of it).
     * @param minM minimum number of jobs
     * @param maxM maximum number of jobs
     * @param epsilon epsilon needed for canstraint 2.
     */
    public static Instance generateTransformedInstance(int minM, int maxM, double epsilon) {
        if (epsilon <= 0 || epsilon >= 1) {
            throw new IllegalArgumentException("Epsilon must be in (0, 1)");
        }
        int m = (int) (Math.random() * (maxM - minM) + minM);
        double totalProcessingTime = m; // Initialize the total processing time
        List<Job> jobs = new ArrayList<>();
        double minLoad = (m / 2.0);
        double maxLoad = (2.0 * m);
        
        double[] machineLoads = new double[m];
        
        while (totalProcessingTime > 0) {
            double processingTime = epsilon + Math.random() * (1 - epsilon);

            // Ensure processing time does not exceed remaining total processing time
            if (processingTime > totalProcessingTime) {
                if (totalProcessingTime < epsilon) {
                    Job firstJob = jobs.get(0);
                    totalProcessingTime += firstJob.getP();
                    jobs.remove(firstJob);
                    continue;
                }
                processingTime = totalProcessingTime;
            }

            Job job = new Job(processingTime, jobs.size() + 1);
            jobs.add(job);
            totalProcessingTime -= processingTime;
            
            // Assign the job to a machine
            int machineIndex = jobs.size() % m;
            machineLoads[machineIndex] += processingTime;
            
            // Check and adjust load if necessary
            while (machineLoads[machineIndex] > maxLoad) {
                double excessLoad = machineLoads[machineIndex] - maxLoad;
                processingTime -= excessLoad;
                totalProcessingTime += excessLoad; //keep track of the pTime that is not allocated.
                machineLoads[machineIndex] = maxLoad;
                job.setP(processingTime);
                machineIndex = (machineIndex + 1) % m;
            }
        }
        
        Machine[] machines = new Machine[m];
        for (int i = 0; i < m; i++) {
            machines[i] = new Machine();
        }
        return new Instance(jobs.toArray(new Job[0]), machines);
    }
    
}
