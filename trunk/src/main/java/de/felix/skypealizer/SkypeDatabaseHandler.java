/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer;

import de.felix.skypealizer.exception.SkypeDatabaseException;
import de.felix.skypealizer.model.skype.SkypeChat;
import de.felix.skypealizer.model.skype.SkypeDatabase;
import de.felix.skypealizer.model.skype.SkypeMessage;
import de.felix.skypealizer.model.skype.SkypeUser;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.joda.time.DateTime;

/**
 *
 * @author felixhusse
 */
public class SkypeDatabaseHandler {

    private Connection connection;
    private SkypeDatabase skypeDatabase;

    public SkypeDatabaseHandler(SkypeDatabase skypeDatabase) throws SkypeDatabaseException {
        this.skypeDatabase = skypeDatabase;
        loadSkypeDatabase();
    }

    private void loadSkypeDatabase() throws SkypeDatabaseException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            throw new SkypeDatabaseException("Database driver not found!");
        }
        File dbFile = skypeDatabase.getDbFile();
        if (dbFile.exists()) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            } catch (SQLException ex) {
                throw new SkypeDatabaseException("Cannot connect to Skype Database." + ex.getMessage());
            }
        }
        else {
            throw new SkypeDatabaseException("Database file not found! Please check Database path.");
        }
    }

    /**
     * Loads all SkypeChats with SkypeUsers.
     * @throws SkypeDatabaseException
     */
    public void loadSkypeChats() throws SkypeDatabaseException  {
        String query = "SELECT * FROM conversations;";
        try {
            Statement stmtConversations = connection.createStatement();
            ResultSet rs = stmtConversations.executeQuery(query);

            while(rs.next()) {
                SkypeChat skypeChat = new SkypeChat();

                skypeChat.setChatName(rs.getString("displayname"));
                skypeChat.setConvoId(rs.getString("id"));

                String queryPart = "SELECT * FROM participants WHERE convo_id="+skypeChat.getConvoId()+";";
                Statement stmtUser = connection.createStatement();
                ResultSet rsPart = stmtUser.executeQuery(queryPart);

                while(rsPart.next()) {
                    String id = rsPart.getString("Identity");
                    Statement stmtUserName = connection.createStatement();
                    ResultSet rsContacts = stmtUserName.executeQuery("Select * FROM Contacts WHERE skypename='"+id+"';");
                    while (rsContacts.next()) {
                        SkypeUser skypeUser = new SkypeUser(rsContacts.getString("displayname"), id);
                        skypeChat.addSkypeUser(skypeUser);
                    }
                    rsContacts.close();
                    stmtUserName.close();
                }
                rsPart.close();
                skypeDatabase.getSkypeChats().add(skypeChat);
                stmtUser.close();
            }
            rs.close();
            stmtConversations.close();

        } catch (SQLException ex) {
            throw new SkypeDatabaseException(ex.getLocalizedMessage());
        }
    }

    public SkypeChat loadSkypeChat(SkypeChat skypeChat) throws SkypeDatabaseException {
        try {
            for (SkypeUser skypeUser : skypeChat.getSkypeUsers()) {
                String messageQuery = "SELECT * FROM messages WHERE convo_id=" + skypeChat.getConvoId()+" AND author='"+skypeUser.getUserId()+"';";
                Statement stmtMessages = connection.createStatement();
                ResultSet rsMsgs = stmtMessages.executeQuery(messageQuery);

                while (rsMsgs.next()) {

                    long time = Long.parseLong(rsMsgs.getString("timestamp"))*1000;
                    DateTime dtTimeStamp = new DateTime(time);

                    String messageText = rsMsgs.getString("body_xml");

                    SkypeMessage skypeMessage = new SkypeMessage(messageText, dtTimeStamp);
                    skypeUser.addSkypeMessage(skypeMessage);
                }
                rsMsgs.close();
                stmtMessages.close();
            }


        } catch (SQLException ex) {
            throw new SkypeDatabaseException(ex.getLocalizedMessage());
        }
        
        return skypeChat;
    }

    public void openSkypeDatabaseConnection() throws SkypeDatabaseException {
        loadSkypeDatabase();
    }

    public void closeSkypeDatabaseConnection() throws SkypeDatabaseException {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                throw new SkypeDatabaseException("Connection is already closed");
            }
        }
    }

    public SkypeDatabase getSkypeDatabase() {
        return  skypeDatabase;
    }

}
