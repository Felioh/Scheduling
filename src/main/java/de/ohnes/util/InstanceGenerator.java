package de.ohnes.util;

public class InstanceGenerator {
    

    public static Instance generate(int minN, int maxN, int minM, int maxM, int maxP) {
        int n = (int) (Math.random() * (maxN - minN) + minN);
        int m = (int) (Math.random() * (maxM - minM) + minM);

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
