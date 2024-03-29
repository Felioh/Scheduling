package de.ohnes.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Machine {

    private List<Job> jobs = new ArrayList<Job>();
    
    public void addJob(Job j) {
        if (j == null) {
            throw new IllegalArgumentException();
        }
        jobs.add(j);
    }

    public void removeLastJob() {
        this.jobs.remove(this.jobs.size() - 1);
    }

    public List<Job> getJobs() {
        return this.jobs;
    }

    public Job removeLargestJob() {
        if (jobs.isEmpty()) {
            return null;
        }
        Job maxJob = jobs.stream().max(Comparator.comparing(Job::getP)).get();
        jobs.remove(maxJob);
        return maxJob;
        
    }

    /**
     * return the load of this machine. not considering starting times.
     * @return
     */
    public double getLoad() {
        if (jobs.size() == 0) {
            return 0;
        }
        assert !jobs.contains(null);
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
