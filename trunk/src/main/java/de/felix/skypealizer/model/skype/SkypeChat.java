/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.model.skype;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author felixhusse
 */
public class SkypeChat {

    private String chatName;
    private String convoId;
    private List<SkypeUser> skypeUsers = new ArrayList<SkypeUser>();


    public SkypeChat() {
        
    }

    public String getChatName(){
        return chatName;
    }

    public void setChatName(String chatName){
        this.chatName = chatName;
    }

    public String getConvoId(){
        return convoId;
    }

    public void setConvoId(String convoId){
        this.convoId = convoId;
    }

    public List<SkypeUser> getSkypeUsers(){
        return skypeUsers;
    }

    public void addSkypeUser(SkypeUser skypeUser){
        this.skypeUsers.add(skypeUser);
    }

    @Override
    public String toString() {
        return "SkypeChat{" + "chatName=" + chatName + "|convoId=" + convoId + "|skypeUsers=" + skypeUsers.size() + '}';
    
    }

}
