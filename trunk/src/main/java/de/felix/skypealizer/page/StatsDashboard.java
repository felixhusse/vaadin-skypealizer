/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.page;

import com.vaadin.ui.Panel;
import de.felix.skypealizer.SkypeALizerApp;
import de.felix.skypealizer.SkypeDatabaseHandler;
import de.felix.skypealizer.exception.SkypeDatabaseException;
import de.felix.skypealizer.model.skype.SkypeChat;
import de.felix.skypealizer.model.skype.SkypeDatabase;
import de.felix.skypealizer.page.statistic.HistoryStatsPanel;
import de.felix.skypealizer.page.statistic.MessageLengthStatsPanel;
import de.felix.skypealizer.page.statistic.UserUsagePanel;
import de.felix.skypealizer.page.statistic.WeeklyStatsPanel;
import java.util.List;
import org.vaadin.navigator7.Navigator.NavigationEvent;
import org.vaadin.navigator7.Page;
import org.vaadin.navigator7.ParamChangeListener;
import org.vaadin.navigator7.uri.Param;
import org.vaadin.sasha.portallayout.PortalLayout;

/**
 *
 * @author felixhusse
 */
@Page(uriName="dash")
public class StatsDashboard extends PortalLayout implements ParamChangeListener {

    @Param(pos=0,name="id", required=true)
    private String id;

    @Param(name="db", required=true)
    private String db;

    private SkypeChat skypeChat;

    public StatsDashboard() {
        
        this.setWidth("100%");        
    }

    private Panel createUserPieChart(SkypeChat skypeChat) {
        UserUsagePanel userUsagePanel = new UserUsagePanel(450, 300);
        userUsagePanel.initStats(skypeChat);
        return userUsagePanel;
    }

    private Panel createWeeklyChart(SkypeChat skypeChat) {
        WeeklyStatsPanel panel = new WeeklyStatsPanel(450, 300);
        panel.initStats(skypeChat);
        return panel;
    }
    
    private Panel createHistoryPanel(SkypeChat skypeChat) {
        HistoryStatsPanel historyPanel = new HistoryStatsPanel(900, 300);
        historyPanel.initStats(skypeChat);
        
        return historyPanel;
    }

    private Panel createMessageLength(SkypeChat skypeChat) {
        MessageLengthStatsPanel msgLenthPanel = new MessageLengthStatsPanel(400, 300, skypeChat);
        msgLenthPanel.initStats(skypeChat);
        return msgLenthPanel;
    }

    @Override
    public void paramChanged(NavigationEvent navigationEvent) {
        try {
            SkypeDatabaseHandler currentSkypeDBHandler = null;
            List<SkypeDatabaseHandler> skypeHandlers = ((SkypeALizerApp)SkypeALizerApp.getCurrent()).getDBHandlers();

            for (SkypeDatabaseHandler skypeHandler : skypeHandlers) {
                if (skypeHandler.getSkypeDatabase().getDbName().equals(db)) {
                    currentSkypeDBHandler = skypeHandler;
                    break;
                }

            }
            SkypeChat currentSkypeChat = null;
            if (currentSkypeDBHandler == null) {
                List<SkypeDatabase> skypeDbs = ((SkypeALizerApp)SkypeALizerApp.getCurrent()).getAppConfig().getSkypeDatabases();
                for (SkypeDatabase skypeDB : skypeDbs) {
                    if (skypeDB.getDbName().equals(db)) {
                        currentSkypeDBHandler = new SkypeDatabaseHandler(skypeDB);
                        currentSkypeDBHandler.loadSkypeChats();
                        for (SkypeChat skypeChat : currentSkypeDBHandler.getSkypeDatabase().getSkypeChats()) {
                            if (skypeChat.getConvoId().equals(id)) {
                                currentSkypeChat = currentSkypeDBHandler.loadSkypeChat(skypeChat);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            else {
                for (SkypeChat skypeChat : currentSkypeDBHandler.getSkypeDatabase().getSkypeChats()) {
                    if (skypeChat.getConvoId().equals(id)) {
                        currentSkypeChat = currentSkypeDBHandler.loadSkypeChat(skypeChat);
                        break;
                    }
                }
            }
            Panel userPieChart = createUserPieChart(currentSkypeChat);
            Panel weeklyChart = createWeeklyChart(currentSkypeChat);
            Panel historyChart = createHistoryPanel(currentSkypeChat);
            Panel messageLengthChart = createMessageLength(currentSkypeChat);

            this.addComponent(userPieChart);
            this.addComponent(weeklyChart);
            this.addComponent(historyChart);
            this.addComponent(messageLengthChart);

            this.setComponentCaption(userPieChart, userPieChart.getCaption());
            this.setComponentCaption(weeklyChart, weeklyChart.getCaption());
            this.setComponentCaption(historyChart, historyChart.getCaption());
            this.setComponentCaption(messageLengthChart, messageLengthChart.getCaption());

            userPieChart.setCaption("");
            weeklyChart.setCaption("");
            historyChart.setCaption("");
            messageLengthChart.setCaption("");

        } catch (SkypeDatabaseException ex) {
            ex.printStackTrace();
        }
        
    }

}
