/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.model.skype;

import de.felix.skypealizer.exception.SkypeDatabaseException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author felixhusse
 */
@Root
public class SkypeDatabase {

    @Element
    private String dbFile;
    @Element
    private String dbName;

    @Element
    private boolean isDefault = false;

    private List<SkypeChat> skypeChats = new ArrayList<SkypeChat>();

    public SkypeDatabase() {
        
    }

    public SkypeDatabase(String dbName, File dbFile) throws SkypeDatabaseException {
        this.dbFile = dbFile.getAbsolutePath();
        this.dbName = dbName;
        
    }

    public void setDefault(boolean defaultDB) {
        isDefault = defaultDB;
    }

    public File getDbFile() {
        return new File(dbFile);
    }

    public String getDbName() {
        return dbName;
    }

    public List<SkypeChat> getSkypeChats() {
        return skypeChats;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
