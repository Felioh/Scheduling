package de.ohnes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import de.ohnes.util.Interval;
import de.ohnes.util.MyMath;
import de.ohnes.util.State;
import de.ohnes.util.States;

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

    @Test
    public void testPermutations() {

        Integer[] source = {1, 2, 3};

        List<Integer[]> res = MyMath.getPermutationsOfLength(source, 2, Integer.class);

        //TODO !!
    }

    @Test
    public void testIntervalCut() {
        Interval[] intervals = {new Interval(0, 1), new Interval(2, 4)};
        States states = new States();
        State state1 = new State(0, 2);
        state1.setLoad(1, 0.5);
        state1.setLoad(2, 3);
        states.add(state1);
        State state2 = new State(0, 2);
        state2.setLoad(1, 2);
        state2.setLoad(2, 3);
        states.add(state2);
        List<State> res = MyMath.getCut(intervals, states);

        assertTrue("State1 should be contained in the cut", res.contains(state1));
        assertTrue("State2 should not be contained in the cut", !res.contains(state2));

    }
}
