package de.ohnes.util;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Machine {

    private List<Job> jobs = new ArrayList<Job>();
    
    public void addJob(Job j) {
        jobs.add(j);
    }

    /**
     * return the load of this machine. not considering starting times.
     * @return
     */
    public double getLoad() {
        return jobs.stream().map(j -> j.getP()).mapToDouble(Double::doubleValue).sum();
    }

    public int getNumberAssignedJobs() {
        return jobs.size();
    }

    public Machine clone() {
        List<Job> jobs = new ArrayList<>();
        jobs.addAll(this.jobs);
        return new Machine(jobs); 
    }
}
