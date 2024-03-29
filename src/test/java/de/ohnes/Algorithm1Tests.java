package de.ohnes;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.AlgorithmicComponents.Algorithm1;
import de.ohnes.AlgorithmicComponents.TransformInstance;
import de.ohnes.util.Instance;
import de.ohnes.util.InstanceGenerator;
import de.ohnes.util.Machine;

@RunWith(Parameterized.class)
public class Algorithm1Tests {

    private static final Logger LOGGER = LogManager.getLogger(Algorithm1Tests.class);


    private static final int NB_RANDOM_TESTS = 50;

    private Instance I;
    private double epsilon;
    private int q = 2;  //TODO !!
    
    public Algorithm1Tests(Instance I, double epsilon) {
        this.I = I;
        this.epsilon = epsilon;
    }


    @Parameterized.Parameters
    public static List<Object[]> input() {
        Object[][] testCases = new Object[NB_RANDOM_TESTS][2];
        double epsilon = 0.1;
        for (int i = 0; i < NB_RANDOM_TESTS; i++) {

            Instance I = InstanceGenerator.generate(5, 10, 2, 2, 20);
            TransformInstance.transformInstance(I, (int) (1 / epsilon));

            testCases[i] = new Object[]{I, epsilon};
        }
        return Arrays.asList(testCases);   
    }

    @Test
    public void testAlgo1() {
        LOGGER.info("starting test");
        Algorithm algo1 = new Algorithm1();

        algo1.solve(I, epsilon, q);
        assertTrue("The load of every machine is <= 2", Arrays.stream(I.getMachines()).allMatch(m -> m.getLoad() <= 2));
        assertTrue("The load of every machine is >= 0.5", Arrays.stream(I.getMachines()).allMatch(m -> m.getLoad() >= 0.5));
        assertTrue("every job is ass(intigned to a machine", Arrays.stream(I.getMachines()).map(Machine::getNumberAssignedJobs).reduce(0, Integer::sum) == I.getN());
    }
    
}
