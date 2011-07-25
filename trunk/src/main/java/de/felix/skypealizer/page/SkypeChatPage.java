/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.page;

import com.google.gwt.user.client.ui.Panel;
import com.invient.vaadin.charts.InvientChartsConfig.HorzAlign;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
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
import org.vaadin.sasha.portallayout.PortalLayout;

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
        HorizontalLayout portalLayout = new HorizontalLayout();
        portalLayout.setSizeFull();
        

        LinkedList<SkypeChat> usedSkypeChats = new LinkedList<SkypeChat>();
        //Limit ChatListing to USERSIZE > 2
        for (SkypeChat skypeChat : skypeDBHandler.getSkypeDatabase().getSkypeChats()) {
            if (skypeChat.getSkypeUsers().size() > 2) {
                
                skypeChat = skypeDBHandler.loadSkypeChat(skypeChat);
                usedSkypeChats.add(skypeChat);
            }
        }

        PortalLayout leftLayout = new PortalLayout();
        PortalLayout centerLayout = new PortalLayout();
        PortalLayout rightLayout = new PortalLayout();

        for (int i = 0; i < usedSkypeChats.size(); i=i+2) {
            if (i < usedSkypeChats.size()) {
                ChatInfoPanel chatInfoPanel = new ChatInfoPanel(usedSkypeChats.get(i), 375, 300, skypeDBHandler.getSkypeDatabase().getDbName());
                leftLayout.addComponent(chatInfoPanel);
                leftLayout.setComponentCaption(chatInfoPanel, chatInfoPanel.getCaption());
                chatInfoPanel.setCaption("");
                chatInfoPanel.setStyleName(Runo.PANEL_LIGHT);
            }
            if (i+1 < usedSkypeChats.size()) {
                ChatInfoPanel chatInfoPanel = new ChatInfoPanel(usedSkypeChats.get(i+1), 375, 300, skypeDBHandler.getSkypeDatabase().getDbName());
                centerLayout.addComponent(chatInfoPanel);
                centerLayout.setComponentCaption(chatInfoPanel, chatInfoPanel.getCaption());
                chatInfoPanel.setCaption("");
                chatInfoPanel.setStyleName(Runo.PANEL_LIGHT);
            }
            if (i+2 < usedSkypeChats.size()) {
                ChatInfoPanel chatInfoPanel = new ChatInfoPanel(usedSkypeChats.get(i+2), 375, 300, skypeDBHandler.getSkypeDatabase().getDbName());
                rightLayout.addComponent(chatInfoPanel);
                rightLayout.setComponentCaption(chatInfoPanel, chatInfoPanel.getCaption());
                chatInfoPanel.setCaption("");
                chatInfoPanel.setStyleName(Runo.PANEL_LIGHT);
            }
        }
        portalLayout.addComponent(leftLayout);
        portalLayout.addComponent(centerLayout);
        portalLayout.addComponent(rightLayout);
        portalLayout.setSpacing(true);
        portalLayout.setMargin(true);
        this.addComponent(portalLayout);
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        
        String dbName = event.getProperty().toString();
        SkypeALizerApp.getCurrentNavigableAppLevelWindow().getNavigator()
                                  .navigateTo(SkypeChatPage.class, dbName);
    }

}
