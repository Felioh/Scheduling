package de.ohnes.util;

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
    
}
