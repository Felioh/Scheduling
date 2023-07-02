package de.ohnes.util;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;

public class MyMath {

    /**
     * rounds @n up to the next integer multiple of @target.
     * assuming that @n and @target >= 0.
     */
    public static double roundUp(double n, double target) {
        return target * Math.ceil(n / target);
    }

    /**
     * returns a list of all intervals up to 2. TODO check!!!
     * @param epsilon
     * @param delta
     * @return
     */
    public static Interval[] getLoadIntervals(double epsilon, double delta, double max) {
        List<Interval> res = new ArrayList<>();
        double lower = 0;
        double upper = epsilon; //there should not be a job with p_j < epsilon, so this interval is effectively [0]
        while (lower < max) {
            res.add(new Interval(lower, upper));
                lower = upper;
                upper = upper * (1 + delta);
        }
        return res.toArray(new Interval[0]);
    }

    public static <T> HashSet<T[]> findPowerSet(T[] set, Class<T> clazz) {
        Deque<T> tmp = new ArrayDeque<>();
        HashSet<T[]> res = new HashSet<>();
        recuriveFindPowerSet(set, tmp, res, set.length, clazz);
        return res;
    }

    private static <T> void recuriveFindPowerSet(T[] in,  Deque<T> out, HashSet<T[]> res, int n, Class<T> clazz) {
        if (n == 0) {
            res.add(out.toArray((T[]) Array.newInstance(clazz, 0)));
            return;
        }

        out.addLast(in[n - 1]);
        recuriveFindPowerSet(in, out, res, n - 1, clazz);

        out.removeLast();

        recuriveFindPowerSet(in, out, res, n - 1, clazz);
    }

    public static boolean isInCut(Interval[] intervals, double[] loads) {
        if (intervals.length != loads.length) {
            return false; //this should not happen.
        }
        for (int i = 0; i < intervals.length; i++) {
            if (!intervals[i].contains(i)) {
                return false;
            }
        }
        return true;
    }

    public static <T> List<T[]> getPermutationsOfLength(T[] list, int length, Class<T> clazz) {
        List<T[]> res = new ArrayList<>();
        recursiveGetPermutations(list, (T[]) Array.newInstance(clazz, 0), res, length);
        return res;
    }

    private static <T> void recursiveGetPermutations(T[] list, T[] tmp, List<T[]> res, int length) {
        if (tmp.length == length) {
            res.add(tmp);
            return;
        }

        for (T element : list) {
            T[] tmp_new = Arrays.copyOf(tmp, tmp.length + 1);
            tmp_new[tmp_new.length - 1] = element;
            recursiveGetPermutations(list, tmp_new, res, length);
        }
    }

    public static List<State> getCut(Interval[] intervals, States states) {
        List<State> res = new ArrayList<>();
        stateloop:
        for (State state : states.getStates()) {
            for (int i = 0; i < intervals.length; i++) {
                if (!intervals[i].contains(state.getLoad(i+1))) {
                    continue stateloop;
                }
            }
            res.add(state);
        }
        return res;
    }
    
}
