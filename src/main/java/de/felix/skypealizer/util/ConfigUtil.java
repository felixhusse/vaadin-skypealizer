/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author felixhusse
 */
public class ConfigUtil {

    public static final String CONFIG_NAME = "configuration.xml";
    
    public static final DateTimeFormatter dtFormatter = DateTimeFormat.forPattern("dd MMM yyyy");
  
    public static boolean isWindows(){
        String os = System.getProperty("os.name").toLowerCase();
        //windows
        return (os.indexOf( "win" ) >= 0); 

    }

    public static boolean isMac(){
        String os = System.getProperty("os.name").toLowerCase();
        //Mac
        return (os.indexOf( "mac" ) >= 0); 
    }

    public static boolean isUnix(){
        String os = System.getProperty("os.name").toLowerCase();
        //linux or unix
        return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);

    }


}
