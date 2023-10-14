package de.ohnes;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.ohnes.util.Instance;
import de.ohnes.util.InstanceGenerator;
import de.ohnes.util.Job;
import lombok.NoArgsConstructor;

@RunWith(Parameterized.class)
@NoArgsConstructor
public class GenerationTests {

    private static final int NB_RANDOM_TESTS = 50;

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[NB_RANDOM_TESTS][]);
    }

    @Test 
    public void TestTransformedGeneration() {
        double epsilon = 0.1;
        Instance I = InstanceGenerator.generateTransformedInstance(10, 11, epsilon);

        assertTrue("Number of machines is in the specified range", I.getM() >= 10 && I.getM() <= 11);
        assertTrue("size(I) == m(I)", Math.round(I.getSize()) == I.getM()); //We have to round here because of numerical errors when adding many small numbers.
        assertTrue("no pTime can be smaller than epsilon; " + Arrays.stream(I.getJobs()).min(Comparator.comparing(Job::getP)).get().getP(),
            Arrays.stream(I.getJobs()).noneMatch(j -> j.getP() < epsilon));
        assertTrue("no pTime can be greater than 1", Arrays.stream(I.getJobs()).noneMatch(j -> j.getP() > 1));
    }
    
}
