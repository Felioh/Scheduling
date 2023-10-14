package de.ohnes.util;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Configuration {

    /**
     * this is a tuple, where the ith element idicates 
     * how many job of processing time p_i are scheduled in the configuraion.
     */
    private int[] allottment;

    public int get(int i) {
        return this.allottment[i];
    }

    /**
     * TODO restrict the number of configs. This is too expensive to construct.
     * @param list
     */
    public void constructAllConfigs(List<Configuration> list) {
        int index = this.allottment.length - 1;
        int[] nextAllotment = new int[this.allottment.length];
        for (int i = 0; i < this.allottment.length; i++) {
            nextAllotment[i] = this.allottment[i];
        }

        while (index >= 0) {
            // if (index == 0 && nextAllotment[index] == 1) {
            //     return; //do not add the allotment where no jobs are alloted.
            // }

            if (nextAllotment[index] > 0) {
                nextAllotment[index]--;
                addConfigToList(list, nextAllotment);


                if (index != nextAllotment.length - 1) {
                    index = this.allottment.length - 1;
                }
            } else { //the element at this index is equal to zero

                //reset this index.
                nextAllotment[index] = this.allottment[index];
                index--;
            }
        }
    }

    private void addConfigToList(List<Configuration> list, int[] allotment) {
        int[] newAllotment = new int[allotment.length];
        for (int i = 0; i < allotment.length; i++) {
            newAllotment[i] = allotment[i];
        }
        list.add(new Configuration(newAllotment));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (!(o instanceof Configuration)) return false;

        return Arrays.equals(((Configuration) o).allottment, this.allottment);
    }

    
}
