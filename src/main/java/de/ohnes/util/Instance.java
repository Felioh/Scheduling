package de.ohnes.util;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Instance {

    private Job[] jobs;

    private Machine[] machines;

    public int getN() {
        return jobs.length;
    }
    
    public int getM() {
        return machines.length;
    }

    /**
     * return the total processing time in the instance.
     * should only be used in testing.
     * @return
     */
    public double getSize() {
        double size = 0;
        for (Job job : jobs) {
            size += job.getP();
        }
        return size;
    }

    /**
     * returns the number of distinct processing times in the Instance
     * should only be used for testing.
     */
    public long getDistinctPTimes() {
        return Arrays.stream(this.jobs).mapToDouble(Job::getP).distinct().count();

    }

    /**
     * assumption: the jobs are orderd with respect to their processing time.
     * @return an array containing in the ith index the number of items with ptime p_i
     */
    public int[] getNbPTimes() {
        int[] nbPTimes = new int[(int) getDistinctPTimes()];
        double lastTime = 0;
        int i = -1;
        for(Job job : jobs) {
            if (job.getP() != lastTime) {
                i++;
                lastTime = job.getP();
            }
            nbPTimes[i]++;
        }

        return nbPTimes;
    }

    /**
     * 
     * @return the distict processing times contained in this instance.
     */
    public double[] getPTimes() {
        double[] pTimes = new double[(int) getDistinctPTimes()];
        double lastTime = 0;
        int i = -1;
        for (Job job : jobs) {
            if (job.getP() != lastTime) {
                i++;
                lastTime = job.getP();
            }
            pTimes[i] = lastTime;
        }
        return pTimes;
    }
}
