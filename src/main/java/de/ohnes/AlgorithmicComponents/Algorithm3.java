package de.ohnes.AlgorithmicComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPSolverParameters;
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

        Instance I_final = new Instance(new Job[0], new Machine[0]); //this intance will contain the jobs that are scheduled.
        
        if (I.getN() <= I.getM()) {
            //TODO trivial..
        }
        int temp_n = I.getN();
        
        Instance I_D = new Instance(new Job[0], new Machine[0]);
        
        Loader.loadNativeLibraries();
        
        while (I.getM() > 10) {
            Job[] jobs = Arrays.stream(I.getJobs()).sorted(Comparator.comparing(Job::getP)).toArray(Job[] :: new);

            assert jobs[0].getP() <= jobs[1].getP(); //should always be at least 2 jobs.

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
                //the job inside the groups are ordered by processing time due to the order in which they were added.
                I_d_jobs.addAll(harmonic_groups.get(i).removeNJobs(itemsToRemove));
                lastSize = currentSize; //TODO check.
            }
            // I_d_jobs.addAll(harmonic_groups.get(0).getJobs());
            harmonic_groups.remove(0);
            //line 4 in the pseudo code.
            I_D.addJobs(I_d_jobs.toArray(new Job[0]));

            //make sure that the total number of jobs stays constant.
            assert I_d_jobs.size() + harmonic_groups.stream().map(g -> g.getJobs().size()).mapToDouble(Double::valueOf).sum() == jobs.length;
            
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

            //remove empty groups
            List<Group> emptyGroups = new ArrayList<>();
            for (Group g : harmonic_groups) {
                if (g.getSize() == 0) {
                    emptyGroups.add(g);
                }
            }
            //not using removeAll in order to preserve ordering.
            for (Group g : emptyGroups) {
                harmonic_groups.remove(g);
            }

            int[] X = I_prime.getNbPTimes();
            double[] p = I_prime.getPTimes();
            
            //TODO restrict number of configurations.
            List<Configuration> configurations = new ArrayList<>();
            Configuration firstConfig = new Configuration(X);
            configurations.add(firstConfig);
            firstConfig.constructAllConfigs(configurations);
            int N = configurations.size();
            LOGGER.debug("Found {} configuraitons for the conf-LP", N);

            MPSolver solver = MPSolver.createSolver("PDLP"); //GLOP or PDLP (for approx. solutions)
            

            //LP: Variables
            double infinity = java.lang.Double.POSITIVE_INFINITY;
            MPVariable[] x = new MPVariable[N];
            for (int j = 0; j < N; j++) {
                x[j] = solver.makeNumVar(0, infinity, String.format("nb of machines scheduled acording to t_%d", j));
            }

            //LP: constraints
            //the total number of used machines is m(I) (\sum^{N}_{j=1}x_j = m(I))
            MPConstraint numberMachines = solver.makeConstraint(I.getM(), I.getM(), "number used machines");
            for (int j = 0; j < N; j++) {
                numberMachines.setCoefficient(x[j], 1);
            }

            //number of items with processing time (\sum^{N}_{j=1}{t_{ij} \geq b_i} , i \in [1, 2, .. X])
            //for group i ...
            for (int i = 0; i < X.length; i++) {
                //... at least b_i jobs ...
                MPConstraint numberItems = solver.makeConstraint(X[i], infinity, String.format("at least b_%d: %d jobs of size p_%d are used", i, X[i], i));
                
                for (int j = 0; j < N; j++) {
                    //... are assigned by all configurations.
                    numberItems.setCoefficient(x[j], configurations.get(j).get(i));
                }
            }

            //LP: objective
            MPObjective objective = solver.objective();
            for (int j = 0; j < N; j++) {
                double load = 0;
                for (int i = 0; i < X.length; i++) {
                    load += configurations.get(j).get(i) * p[i];
                }
                objective.setCoefficient(x[j], Math.pow(load, q));
            }
            objective.setMinimization();

            int nb_residueMachines = I.getM();
            //LP: solve
            // String model = solver.exportModelAsLpFormat();
            // System.out.println(model);
          
            MPSolver.ResultStatus resultStatus = solver.solve();
            if (resultStatus == MPSolver.ResultStatus.OPTIMAL || resultStatus == MPSolver.ResultStatus.FEASIBLE) {
                LOGGER.debug("Found solution to the LP with objective value: {}", objective.value());
                //construct rounded down solution.
                List<Job> assigned_jobs = new ArrayList<>();
                List<Machine> assigned_machines = new ArrayList<>();
                for (int j = 0; j < N; j++) {
                    Configuration configuration = configurations.get(j);
                    int x_j = (int) Math.floor(x[j].solutionValue());
                    for (int i = 0; i < x_j; i++) { //assign this configuration x_j times.
                        //TODO: add special "NULL"-config??
                        Machine machine = new Machine();
                        for (int p_i = 0; p_i < X.length; p_i++) {
                            for (int n = 0; n < configuration.get(p_i); n++) {
                                Job job = harmonic_groups.get(p_i).popJob();
                                if (job == null) {
                                    //this means that the LP solution assigned more jobs than available.
                                    continue;
                                }
                                I_prime_jobs.remove(job);   //the jobs that remain in I_prime_jobs will be the residue Instance
                                machine.addJob(job);
                                //make sure that the jobs has not been assigned before.
                                assert assigned_machines.stream().noneMatch(m -> m.getJobs().contains(job));

                                assigned_jobs.add(job);
                            }
                        }
                        if (machine.getLoad() != 0) {
                            assigned_machines.add(machine);
                            nb_residueMachines--; //decrease the number of machines still in the residue instance.
                            // assert machine.getLoad() != 0 : "We should not close an empty machine.";
                        }
                    }
                }
                I_final.addMachines(assigned_machines.toArray(new Machine[0]));
                I_final.addJobs(assigned_jobs.toArray(new Job[0]));

            } else {
                LOGGER.error("No feasible solution found.");
                return; //This does not happen.
            }

            //construct residue instance.
            I.setJobs(I_prime_jobs.toArray(new Job[0]));
            List<Machine> machines = new ArrayList<>();
            for (int i = 0; i < nb_residueMachines; i++) {
                machines.add(new Machine());
            }
            I.setMachines(machines.toArray(new Machine[0]));

            assert temp_n == I.getN() + I_final.getN() + I_D.getN();

        }

        //line 9 schedule I using Algorithm 1
        Algorithm1 algo1 = new Algorithm1(); 
        
        assert I.getMachines() != null;

        //apparently there is a very small chance that this happens. Rounding Problems??
        if (!(I.getM() != 0 || I.getN() == 0)) {
            LOGGER.error("testing!!!");
        }
        assert I.getM() != 0 || I.getN() == 0 : String.format("I has %d machines, but %d jobs.", I.getM(), I.getN());
        assert Arrays.stream(I.getJobs()).noneMatch(j -> Arrays.stream(I_final.getMachines()).anyMatch(m -> m.getJobs().contains(j)));
        //transform instance I to satisfy the needs of AL1 
        TransformInstance.transformInstance(I, (int) (1 / epsilon)); //TODO check if 1 / epsilon is correct.
        algo1.solve(I, epsilon, q);
        //Algorithm1 should assign all jobs.
        assert Arrays.stream(I.getJobs()).allMatch(j -> Arrays.stream(I.getMachines()).anyMatch(m -> m.getJobs().contains(j)));
        I.addJobs(I_final.getJobs());
        I.addMachines(I_final.getMachines());


        //while the loads are unbalanced (L_i - L_j > 1) -> move largest ptime from i to j
        Comparator<Machine> comp = Comparator.comparing(Machine::getLoad);
        List<Machine> sortedMachines = Arrays.asList(I.getMachines());
        sortedMachines.sort(comp);
        assert Arrays.stream(I.getJobs()).max(Comparator.comparing(Job::getP)).get().getP() < 1.1 : Arrays.stream(I.getJobs()).max(Comparator.comparing(Job::getP)).get().getP();
        
        int lastIndex = sortedMachines.size() - 1;
        while (sortedMachines.get(lastIndex).getLoad() - sortedMachines.get(0).getLoad() > 1.1) {

            sortedMachines.get(0).addJob(sortedMachines.get(lastIndex).removeLargestJob());

            sortedMachines.sort(comp);
        }

        //schedule I_D
        I.addJobs(I_D.getJobs());
        for (Job j : I_D.getJobs()) {
            assert Arrays.stream(I.getMachines()).noneMatch(m -> m.getJobs().contains(j));

            sortedMachines.sort(comp);
            sortedMachines.get(0).addJob(j);
        }
        
    }
    
}
