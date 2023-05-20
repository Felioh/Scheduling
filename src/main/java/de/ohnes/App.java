package de.ohnes;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;


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

    }

}
