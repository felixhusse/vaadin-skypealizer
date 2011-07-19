/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.util;

import de.felix.skypealizer.model.skype.SkypeMessage;
import java.util.Comparator;

/**
 *
 * @author felixhusse
 */
public class MessageTimeComparator implements Comparator<SkypeMessage>{

    public int compare(SkypeMessage t, SkypeMessage t1) {
        return t.getTimeStamp().compareTo(t1.getTimeStamp());
    }

}
