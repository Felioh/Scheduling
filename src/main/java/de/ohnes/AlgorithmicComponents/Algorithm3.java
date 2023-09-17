package de.ohnes.AlgorithmicComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import de.ohnes.util.Configuration;
import de.ohnes.util.Group;
import de.ohnes.util.Instance;
import de.ohnes.util.Job;
import de.ohnes.util.Machine;

/**
 * Algorithm 3 from the Chen, et. al. paper. As described on p.290
 * This algorithm is to be applied to intances with a large number of machines
 * (\frac{1}_{\epsilon^2} < m)
 */
public class Algorithm3 implements Algorithm {

    private static final Logger LOGGER = LogManager.getLogger(Algorithm3.class);


    @Override
    public void solve(Instance I, double epsilon, int q) {

        int m = I.getM();
        Job[] jobs = Arrays.stream(I.getJobs()).sorted(Comparator.comparing(Job::getP)).toArray(Job[] :: new);
        //TODO check sorting
        assert jobs[0].getP() < jobs[1].getP(); //should always be at least 2 jobs.

        if (I.getN() <= I.getM()) {
            //TODO trivial..
        }
        
        Instance I_D = new Instance(new Job[0], new Machine[0]);

        Loader.loadNativeLibraries();
        
        while (m > 10) {
            Instance I_prime = new Instance(null, null);

            List<Group> harmonic_groups = new ArrayList<>(); //TODO other structure? stack?
            harmonic_groups.add(new Group());
            
            //group jobs. line 3 in the pseudo code.
            for (Job job : jobs) {
                if (harmonic_groups.get(harmonic_groups.size() - 1).isFull()) {
                    harmonic_groups.add(new Group());
                }
                harmonic_groups.get(harmonic_groups.size() - 1).addJob(job.copy());
            }
    
            //discard certain jobs. p.293
            List<Job> I_d_jobs = new ArrayList<>();
            I_d_jobs.addAll(harmonic_groups.get(0).getJobs());
            int lastSize = harmonic_groups.get(0).getSize();
            for (int i = 1; i < harmonic_groups.size(); i++) {
                int currentSize = harmonic_groups.get(i).getSize();
                int itemsToRemove = lastSize - currentSize;
                //the job inside the groups are ordered by processing time due to the order in which they were filled.
                I_d_jobs.addAll(harmonic_groups.get(i).removeNJobs(itemsToRemove));
                lastSize = currentSize; //TODO check.
            }
            harmonic_groups.remove(0);
            //line 4 in the pseudo code.
            List<Job> I_D_jobs = new ArrayList<>(Arrays.asList(I_D.getJobs()));
            I_D_jobs.addAll(I_d_jobs);
            I_D.setJobs(I_D_jobs.toArray(new Job[0]));

            
            //"lift the processing time"
            //"and let it be I'"
            List<Job> I_prime_jobs = new ArrayList<>();
            for (Group group : harmonic_groups) {
                group.liftP();
                I_prime_jobs.addAll(group.getJobs());
            }
            I_prime.setJobs(I_prime_jobs.toArray(new Job[0]));

            //the number of distinct job processing times in I' is at most size(I)/2
            assert I_prime.getDistinctPTimes() <= I.getSize() / 2;


            int[] X = I_prime.getNbPTimes();
            double[] p = I_prime.getPTimes();
            
            //TODO restrict number of configurations.
            List<Configuration> configurations = new ArrayList<>();
            Configuration firstConfig = new Configuration(X);
            configurations.add(firstConfig);
            firstConfig.constructAllConfigs(configurations);
            int N = configurations.size();
            LOGGER.debug("Found {} configuraitons for the conf-LP", N);

            MPSolver solver = MPSolver.createSolver("GLOP");
            

            //Variables
            double infinity = java.lang.Double.POSITIVE_INFINITY;
            MPVariable[] x = new MPVariable[N];
            for (int j = 0; j < N; j++) {
                x[j] = solver.makeNumVar(0, infinity, "nb of machines scheduled acording to t_j");
            }

            //constraints
            //the total number of used machines is m(I)
            MPConstraint numberMachines = solver.makeConstraint(I.getM(), I.getM(), "number used machines");
            for (int j = 0; j < N; j++) {
                numberMachines.setCoefficient(x[j], 1);
            }
            //number of items with processing time
            for (int i = 0; i < X.length; i++) {
                MPConstraint numberItems = solver.makeConstraint(X[i], infinity, String.format("at least b_{} jobs of size p_{} are used", i, i));
                for (int j = 0; j < N; j++) {
                    numberItems.setCoefficient(x[j], configurations.get(j).get(i));
                }
            }

            //objective
            MPObjective objective = solver.objective();
            for (int j = 0; j < N; j++) {
                double load = 0;
                for (int i = 0; i < X.length; i++) {
                    load += configurations.get(j).get(i) * p[i];
                }
                objective.setCoefficient(x[j], Math.pow(load, q));
            }
            objective.setMinimization();

            //solve
            MPSolver.ResultStatus resultStatus = solver.solve();
            if (resultStatus == MPSolver.ResultStatus.OPTIMAL || resultStatus == MPSolver.ResultStatus.FEASIBLE) {
                LOGGER.debug("Found solution to the LP with objective value: {}", objective.value());
                //construct rounded down solution.
                Job[] prime_jobs = I_prime.getJobs();
                List<Machine> machines = new ArrayList<>();
                for (int j = 0; j < N; j++) {
                    Configuration configuration = configurations.get(j);
                    int x_j = (int) Math.floor(x[j].solutionValue());
                    for (int i = 0; i < x_j; i++) {
                        Machine machine = new Machine();
                        for (int p_i = 0; p_i < X.length; p_i++) {
                            for (int n = 0; n < configuration.get(p_i); n++) {
                                machine.addJob(harmonic_groups.get(p_i).popJob());
                            }
                        }
                        machines.add(machine);
                    }
                }
                I_prime.setMachines(machines.toArray(new Machine[0]));
                //TODO construst residue instance. <- add remaining jobs in harmonic groups (and remove them from I')

                //TODO schedule according to the solution. (rounded down)
            } else {
                LOGGER.error("No feasible solution found.");
                return; //TODO decide how to handle this.
            }
        }

        //line 9 schedule I using Algorithm 1
        Algorithm1 algo1 = new Algorithm1();
        algo1.solve(I, epsilon, q);
        
        //while the loads are unbalanced (L_i - L_j > 1)
        while (true) {
            //TODO
            break;
        }
        //TODO schedule I_D





        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solve'");
    }
    
}
