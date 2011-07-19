/*
 * 
 */

package de.felix.skypealizer.model.skype;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * 
 *
 * @author felixhusse
 */
public class SkypeUser {
    
    private String displayName;
    private String userId;
    private LinkedList<SkypeMessage> skypeMessages = new LinkedList<SkypeMessage>();

    public SkypeUser(String displayName, String userId){
        this.displayName = displayName;
        this.userId = userId;
    }

    public String getDisplayName(){
        return displayName;
    }

    public String getUserId(){
        return userId;
    }

    public List<SkypeMessage> getSkypeMessages() {
        return skypeMessages;
    }
    
    public void addSkypeMessage(SkypeMessage skypeMessage) {
        skypeMessages.add(skypeMessage);
    }
    
    
    
}
