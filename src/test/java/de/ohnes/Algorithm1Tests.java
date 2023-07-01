package de.ohnes;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

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

    private static final int NB_RANDOM_TESTS = 1;

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

            //TODO: small number of machines !!
            Instance I = InstanceGenerator.generate(10, 20, 1, 10, 20);
            TransformInstance.transformInstance(I, (int) (1 / epsilon));

            testCases[i] = new Object[]{I, epsilon};
        }
        return Arrays.asList(testCases);   
    }

    @Test
    public void testAlgo1() {
        Algorithm algo1 = new Algorithm1();

        algo1.solve(I, epsilon, q);

        assertTrue("every job is assigned to a machine", Arrays.stream(I.getMachines()).map(Machine::getNumberAssignedJobs).reduce(0, Integer::sum) == I.getN());
        boolean test = false;
    }
    
}
