package de.ohnes;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.ohnes.util.Configuration;

public class ConfigurationTests {

    @Test
    public void testConstructAllConfigs() {
        int[] initAllotment = new int[]{2, 2, 2};
        Configuration config = new Configuration(initAllotment);
        List<Configuration> configs = new ArrayList<>();
        configs.add(config);
        config.constructAllConfigs(configs);

        Configuration test1 = new Configuration(new int[]{1, 1, 1});
        Configuration test2 = new Configuration(new int[]{1, 1, 2});
        Configuration test3 = new Configuration(new int[]{2, 0, 2});
        Configuration test4 = new Configuration(new int[]{2, 2, 0});
        assertTrue("The array contains a (1, 1, 1)- config", configs.contains(test1));
        assertTrue("The array contains a (1, 1, 2)- config", configs.contains(test2));
        assertTrue("The array contains a (2, 0, 2)- config", configs.contains(test3));
        assertTrue("The array contains a (2, 2, 0)- config", configs.contains(test4));
    }
    
}
