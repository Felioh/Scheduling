package de.ohnes.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Group {

    private List<Job> jobs = new ArrayList<>();

    private double load = 0;

    public boolean isFull() {
        return this.load >= 2;
    }
    
    public void addJob(Job job) {
        this.jobs.add(job);
        this.load += job.getP();
    }

    public int getSize() {
        return this.jobs.size();
    }

    /**
     * This method will remove the n first items the the "jobs" list.
     * assuming the jobs are orderd it will remove the n smallest jobs.
     * @param n
     * @return the deleted jobs.
     */
    public List<Job> removeNJobs(int n) {
        List<Job> discarded_jobs = new ArrayList<>();
        while (n > 0) {
            if (this.jobs.size() == 0) break; //TODO should this happen??
            discarded_jobs.add(jobs.get(0));
            this.jobs.remove(0);
            n--;
        }
        return discarded_jobs;
    }

    /**
     * This method will set the processing time of its jobs to the highest among them
     * assuming that the jobs are ordered.
     */
    public void liftP() {
        if (this.jobs.size() == 0) return;
        double p = this.jobs.get(this.jobs.size() - 1).getP();
        for (int i = this.jobs.size() - 1; i >= 0; i--) {
            this.jobs.get(i).setP(p);
        }

    }

    public Job popJob() {
        //can this be empty?? -> Yes. And it can cost 2hr of my life :( TODO wegmachen.
        try {
            return this.jobs.remove(0);
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }
}
