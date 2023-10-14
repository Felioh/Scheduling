package de.ohnes;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.AlgorithmicComponents.Algorithm1;
import de.ohnes.AlgorithmicComponents.Algorithm2;
import de.ohnes.AlgorithmicComponents.Algorithm3;
import de.ohnes.util.Instance;
import de.ohnes.util.InstanceGenerator;
import de.ohnes.util.TestResult;


public class App {

    private static final Logger LOGGER = LogManager.getLogger(App.class);
    private static double EPSILON;
    private static int MIN_MACHINES;
    private static int MAX_MACHINES;
    private static int Q;
    private static String ES_HOST;
    private static String ES_INDEX;

    private static Algorithm ALGO1 = new Algorithm1();
    private static Algorithm ALGO2 = new Algorithm2();
    private static Algorithm ALGO3 = new Algorithm3();
    
    /** 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Configurator.setRootLevel(Level.ALL);

        EPSILON = Double.parseDouble(System.getenv("EPSILON"));
        MIN_MACHINES = Integer.parseInt(System.getenv("MIN_MACHINES"));
        MAX_MACHINES = Integer.parseInt(System.getenv("MAX_MACHINES"));
        Q = Integer.parseInt(System.getenv("Q"));
        ES_HOST = System.getenv("ES_HOST");
        ES_INDEX = System.getenv("ES_INDEX");
        

        
        LOGGER.info("Starting Application!");
        
        while(true) {
            runTest();
        }

        // LOGGER.info("END");
    }

    private static TestResult runTest() {
        TestResult testResult = new TestResult();
        Algorithm algo;
        //just generate the transformed instance, since the generation has to be done for every algorithm anyway.
        Instance I = InstanceGenerator.generateTransformedInstance(MIN_MACHINES, MAX_MACHINES, EPSILON);
        testResult.setJobs(I.getN());
        testResult.setMachines(I.getM());
        if (I.getM() <= Math.sqrt(1 / EPSILON)) {
            //(m <= \sqrt{1/\epsilon})
            algo = ALGO1;
        } else if (I.getM() <= (1 / (EPSILON * EPSILON))) {
            //(\sqrt{1/\epsilon} < m <= \frac{1}_{\epsilon^2})
            algo = ALGO2;
        } else {
            algo = ALGO3;
        }

        LOGGER.info("Running instance [n = {}; m = {}] with {}", I.getN(), I.getM(), algo.getClass().getSimpleName());
        
        long startTime = System.currentTimeMillis();     //starting timer.
        algo.solve(I, EPSILON, Q);
        long endTime = System.currentTimeMillis(); 
        
        LOGGER.info("Computed Solution with Objective value {} in {}ms", I.getObjective(Q), (endTime - startTime));//ending timer.

        testResult.setMilliseconds(endTime - startTime);

        return testResult;
    }

}
