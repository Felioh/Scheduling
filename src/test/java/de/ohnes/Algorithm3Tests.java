package de.ohnes;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.AlgorithmicComponents.Algorithm3;
import de.ohnes.AlgorithmicComponents.TransformInstance;
import de.ohnes.util.Instance;
import de.ohnes.util.InstanceGenerator;
import de.ohnes.util.Machine;

@RunWith(Parameterized.class)
public class Algorithm3Tests {

    private static final Logger LOGGER = LogManager.getLogger(Algorithm3Tests.class);


    private static final int NB_RANDOM_TESTS = 10;

    private Instance I;
    private double epsilon;
    private int q = 2;  //TODO !!
    
    public Algorithm3Tests(Instance I, double epsilon) {
        this.I = I;
        this.epsilon = epsilon;
    }


    @Parameterized.Parameters
    public static List<Object[]> input() {
        Object[][] testCases = new Object[NB_RANDOM_TESTS][2];
        double epsilon = 0.1;
        for (int i = 0; i < NB_RANDOM_TESTS; i++) {

            Instance I = InstanceGenerator.generateTransformedInstance(11, 11, epsilon);
            // TransformInstance.transformInstance(I, (int) (1 / epsilon));

            testCases[i] = new Object[]{I, epsilon};
        }
        return Arrays.asList(testCases);   
    }

    @Test
    public void testAlgo3() {
        LOGGER.info("starting test");
        Algorithm algo3= new Algorithm3();

        algo3.solve(I, epsilon, q);
        assertTrue("The load of every machine should be <= 2 ; " + Arrays.stream(I.getMachines()).max(Comparator.comparing(Machine::getLoad)).get().getLoad(), 
            Arrays.stream(I.getMachines()).allMatch(m -> m.getLoad() <= 2));
        assertTrue("The load of every machine should be >= 0.5 ; " + Arrays.stream(I.getMachines()).min(Comparator.comparing(Machine::getLoad)).get().getLoad(),
            Arrays.stream(I.getMachines()).allMatch(m -> m.getLoad() >= 0.35));
        assertTrue("every job should be assigned to a machine ; " + I.getN() + " != " + Arrays.stream(I.getMachines()).map(Machine::getNumberAssignedJobs).reduce(0, Integer::sum),
            Arrays.stream(I.getMachines()).map(Machine::getNumberAssignedJobs).reduce(0, Integer::sum) == I.getN());
    }
    
}
