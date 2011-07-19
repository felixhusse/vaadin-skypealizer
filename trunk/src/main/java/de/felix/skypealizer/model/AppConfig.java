/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.model;

import de.felix.skypealizer.model.skype.SkypeDatabase;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 *
 *
 * @author felixhusse
 */
@Root
public class AppConfig {

    @ElementList
    private List<SkypeDatabase> skypeDatabases;

    public AppConfig() {
        this.skypeDatabases = new ArrayList<SkypeDatabase>();
    }

    public List<SkypeDatabase> getSkypeDatabases() {
        return skypeDatabases;
    }

    

    
}
