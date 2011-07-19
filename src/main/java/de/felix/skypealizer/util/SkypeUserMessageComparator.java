/**
 * The contents of this file are copyright (c) 2011 by medavis GmbH, Karlsruhe, Germany
 */
package de.felix.skypealizer.util;

import de.felix.skypealizer.model.skype.SkypeUser;
import java.util.Comparator;

/**
 *
 * @author felix.husse
 */
public class SkypeUserMessageComparator implements Comparator<SkypeUser>{

    @Override
    public int compare(SkypeUser o1, SkypeUser o2){
        if (o1.getSkypeMessages().size() > o2.getSkypeMessages().size()) {
            return -1;
        }
        
        if (o1.getSkypeMessages().size() < o2.getSkypeMessages().size()) {
            return 1;
        }
        
        return 0;
    }
    
}
