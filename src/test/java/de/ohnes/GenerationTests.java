package de.ohnes;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import de.ohnes.util.Instance;
import de.ohnes.util.InstanceGenerator;

public class GenerationTests {

    @Test
    public void TestTransformedGeneration() {
        double epsilon = 0.1;
        Instance I = InstanceGenerator.generateTransformedInstance(10, 11, epsilon);

        assertTrue("Number of machines is in the specified range", I.getM() >= 10 && I.getM() <= 11);
        assertTrue("size(I) == m(I)", Math.round(I.getSize()) == I.getM()); //We have to round here because of numerical errors when adding many small numbers.
        assertTrue("no pTime can be smaller than epsilon", Arrays.stream(I.getJobs()).noneMatch(j -> j.getP() < epsilon));
        assertTrue("no pTime can be greater than 1", Arrays.stream(I.getJobs()).noneMatch(j -> j.getP() > 1));
    }
    
}
