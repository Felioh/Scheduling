package de.ohnes;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.ohnes.AlgorithmicComponents.TransformInstance;
import de.ohnes.util.Instance;
import de.ohnes.util.InstanceGenerator;
import de.ohnes.util.Job;

@RunWith(Parameterized.class)
public class TransformInstanceTests {
    
    private static final int NB_RANDOM_TESTS = 10;

    private Instance I;
    private double epsilon;
    
    public TransformInstanceTests(Instance I, double epsilon) {
        this.I = I;
        this.epsilon = epsilon;
    }

    @Parameterized.Parameters
    public static List<Object[]> input() {
        Object[][] testCases = new Object[NB_RANDOM_TESTS][2];
        for (int i = 0; i < NB_RANDOM_TESTS; i++) {

            //TODO: randomly choose size.
            Instance I = InstanceGenerator.generate(10, 20, 1, 10, 20);

            testCases[i] = new Object[]{I, 0.1};
        }
        return Arrays.asList(testCases);   
    }

    @Test
    public void testTransformation() {
        int lambda = (int) (1 / this.epsilon);  //TODO this is not correct, check!!

            
        TransformInstance.transformInstance(I, lambda);
        double L = Arrays.stream(I.getJobs()).map(Job::getP).mapToDouble(Double::doubleValue).sum() / I.getM();
        // double sizeI = Arrays.stream(I.getJobs()).map(Job::getP).mapToDouble(Double::doubleValue).sum();
        assertTrue("Processing time of every job should be greater than epsilon", Arrays.stream(I.getJobs()).allMatch(j -> j.getP() >= this.epsilon*0.9));
        assertTrue("Processing time of every job should be less than 1", Arrays.stream(I.getJobs()).allMatch(j -> j.getP() <= 1.1));
        // assertEquals("size(I) should equal m(I) after transformation", I.getM(), sizeI, 0.001);
    }
}
