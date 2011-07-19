/**
 * 
 */
package de.felix.skypealizer.model.skype;

import org.joda.time.DateTime;

/**
 *
 * @author felix.husse
 */
public class SkypeMessage {
    
    private String messageText = "";
    private DateTime timeStamp;

    public SkypeMessage(String messageText, DateTime timeStamp){
        this.messageText = messageText;
        this.timeStamp = timeStamp;
    }

    public String getMessageText(){
        return messageText;
    }

    public void setMessageText(String messageText){
        this.messageText = messageText;
    }

    public DateTime getTimeStamp(){
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp){
        this.timeStamp = timeStamp;
    }
    
}
