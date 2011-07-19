/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.page;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import de.felix.skypealizer.SkypeALizerApp;
import de.felix.skypealizer.SkypeDatabaseHandler;
import de.felix.skypealizer.exception.SkypeDatabaseException;
import de.felix.skypealizer.model.AppConfig;
import de.felix.skypealizer.model.skype.SkypeChat;
import de.felix.skypealizer.model.skype.SkypeDatabase;
import de.felix.skypealizer.page.panel.ChatInfoPanel;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.navigator7.Navigator.NavigationEvent;
import org.vaadin.navigator7.Page;
import org.vaadin.navigator7.ParamChangeListener;
import org.vaadin.navigator7.uri.Param;

/**
 *
 * @author felixhusse
 */
@Page(uriName="chats")
public class SkypeChatPage extends VerticalLayout implements ParamChangeListener, ValueChangeListener {

    @Param(pos=0,name="db", required=false)
    private String db;

    


    public SkypeChatPage() {
        
        ComboBox dbSelect = new ComboBox("select your Database");

        AppConfig appConfig = ((SkypeALizerApp)SkypeALizerApp.getCurrent()).getAppConfig();
        for (SkypeDatabase skypeDB : appConfig.getSkypeDatabases()) {
            dbSelect.addItem(skypeDB.getDbName());
        }


        dbSelect.setFilteringMode(Filtering.FILTERINGMODE_OFF);
        dbSelect.setImmediate(true);
        dbSelect.addListener(this);
        this.addComponent(dbSelect);
        //this.setWidth((this.getColumns()*300)+"px");
        //this.setHeight((this.getRows()*375)+"px");
    }

    @Override
    public void paramChanged(NavigationEvent navigationEvent) {
        try {
            List<SkypeDatabaseHandler> skypeDBHandlers = ((SkypeALizerApp)SkypeALizerApp.getCurrent()).getDBHandlers();
            SkypeDatabaseHandler currentDBHandler = null;
            //load default DB. By default FIRST element is List is default!
            if (db == null) {
                for (SkypeDatabaseHandler skypeDBHandler : skypeDBHandlers) {
                    if (skypeDBHandler.getSkypeDatabase().isDefault()) {
                        currentDBHandler = skypeDBHandler;
                        break;
                    }
                }

                if (currentDBHandler == null) {
                    AppConfig appConfig = ((SkypeALizerApp)SkypeALizerApp.getCurrent()).getAppConfig();
                    for (SkypeDatabase skypeDB : appConfig.getSkypeDatabases()) {
                        if (skypeDB.isDefault()) {
                            currentDBHandler = new SkypeDatabaseHandler(skypeDB);
                            currentDBHandler.loadSkypeChats();
                            ((SkypeALizerApp)SkypeALizerApp.getCurrent()).addDBHandler(currentDBHandler);
                        }
                    }
                }

            }
            else {
                for (SkypeDatabaseHandler skypeDBHandler : skypeDBHandlers) {
                    if (skypeDBHandler.getSkypeDatabase().getDbName().equals(db)) {
                        currentDBHandler = skypeDBHandler;
                        break;
                    }
                }

                if (currentDBHandler == null) {
                    AppConfig appConfig = ((SkypeALizerApp)SkypeALizerApp.getCurrent()).getAppConfig();
                    for (SkypeDatabase skypeDB : appConfig.getSkypeDatabases()) {
                        if (skypeDB.getDbName().equals(db)) {
                            currentDBHandler = new SkypeDatabaseHandler(skypeDB);

                            currentDBHandler.loadSkypeChats();
                            ((SkypeALizerApp)SkypeALizerApp.getCurrent()).addDBHandler(currentDBHandler);
                        }
                    }
                }
            }
            loadSkypeChatGrid(currentDBHandler);

            
        } catch (SkypeDatabaseException ex) {
            //THROW SOME EXCEPTION!
        }        
    }

    private void loadSkypeChatGrid(SkypeDatabaseHandler skypeDBHandler) throws SkypeDatabaseException {
        GridLayout gridLayout = new GridLayout();

        LinkedList<SkypeChat> usedSkypeChats = new LinkedList<SkypeChat>();
        //Limit ChatListing to USERSIZE > 2
        for (SkypeChat skypeChat : skypeDBHandler.getSkypeDatabase().getSkypeChats()) {
            if (skypeChat.getSkypeUsers().size() > 2) {
                
                skypeChat = skypeDBHandler.loadSkypeChat(skypeChat);
                usedSkypeChats.add(skypeChat);
            }
        }

        gridLayout.setColumns(3);
        if (usedSkypeChats.size()%3==0) {
            gridLayout.setRows(usedSkypeChats.size()/3);
        }
        else {
            gridLayout.setRows((usedSkypeChats.size()/3)+1);
        }

        int skypeCounter = 0;
        for (int i = 0; i < gridLayout.getRows(); i++) {
            if (skypeCounter < usedSkypeChats.size()) {
                gridLayout.addComponent(new ChatInfoPanel(usedSkypeChats.get(skypeCounter),375,310,skypeDBHandler.getSkypeDatabase().getDbName()), 0, i);
                skypeCounter++;
                if (skypeCounter < usedSkypeChats.size()) {
                    gridLayout.addComponent(new ChatInfoPanel(usedSkypeChats.get(skypeCounter),375,310,skypeDBHandler.getSkypeDatabase().getDbName()), 1, i);
                    skypeCounter++;
                    if (skypeCounter < usedSkypeChats.size()) {
                        gridLayout.addComponent(new ChatInfoPanel(usedSkypeChats.get(skypeCounter),375,310,skypeDBHandler.getSkypeDatabase().getDbName()), 2, i);
                        skypeCounter++;
                    }
                }
            }
        }

        gridLayout.setMargin(true);
        gridLayout.setSpacing(true);
        this.addComponent(gridLayout);
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        
        String dbName = event.getProperty().toString();
        SkypeALizerApp.getCurrentNavigableAppLevelWindow().getNavigator()
                                  .navigateTo(SkypeChatPage.class, dbName);
    }

}
