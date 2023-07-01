package de.ohnes;

import static org.junit.Assert.assertEquals;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

import org.junit.Test;

import de.ohnes.util.Interval;
import de.ohnes.util.MyMath;

public class MyMathTests {
    
    @Test
    public void testRoudUpSmallValues() {
        double n = 0.04;
        double target = 0.5;

        assertEquals("The rounded up value should equal 0.5", 0.5, MyMath.roundUp(n, target), 0.00001);

    }

    @Test
    public void testRoudUpBigValues() {
        double n = 34.5;
        double target = 20;

        assertEquals("The rounded up value should equal 40", 40, MyMath.roundUp(n, target), 0.00001);

    }

    @Test
    public void testFindPowerset() {
        Deque<Integer> target = new ArrayDeque<>();

        Integer[] source = {1, 2, 3};

        HashSet<Integer[]> res = MyMath.findPowerSet(source, Integer.class);
        
        //TODO !!
    }
}
