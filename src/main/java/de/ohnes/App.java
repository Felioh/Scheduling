package de.ohnes;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import de.ohnes.AlgorithmicComponents.TransformInstance;
import de.ohnes.util.Instance;
import de.ohnes.util.InstanceGenerator;


public class App {

    private static final Logger LOGGER = LogManager.getLogger(App.class);

    
    /** 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Configurator.setRootLevel(Level.ALL);
        
        LOGGER.info("Starting Algorithm!");
        //TODO
        Instance I = InstanceGenerator.generate(1, 3, 1, 3, 10);

        TransformInstance.transformInstance(I, 1);

        LOGGER.info("END");
    }

}
