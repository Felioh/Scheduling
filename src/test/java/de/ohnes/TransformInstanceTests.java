package de.ohnes;

import static org.junit.Assert.assertEquals;
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
    
    private Instance I;
    private double epsilon;
    
    public TransformInstanceTests(Instance I, double epsilon) {
        this.I = I;
        this.epsilon = epsilon;
    }

    @Parameterized.Parameters
    public static List<Object[]> input() {
        Instance I = InstanceGenerator.generate(1, 10, 1, 10, 20);
        return Arrays.asList(new Object[][] {{I, 0.1}});   
    }

    @Test
    public void testTransformation() {
        double L = Arrays.stream(I.getJobs()).map(Job::getP).mapToDouble(Double::doubleValue).sum() / I.getM();
        int lambda = (int) (this.epsilon / L);  //TODO this is not correct, check!!

        TransformInstance.transformInstance(I, lambda);
        double sizeI = Arrays.stream(I.getJobs()).map(Job::getP).mapToDouble(Double::doubleValue).sum();
        assertEquals("size(I) should equal m(I) after transformation", I.getM(), sizeI, 0.001);
        assertTrue("Processing time of every job should be greater than epsilon", Arrays.stream(I.getJobs()).allMatch(j -> j.getP() >= this.epsilon));
        assertTrue("Processing time of every job should be less than 1", Arrays.stream(I.getJobs()).allMatch(j -> j.getP() <= 1));
    }
}
