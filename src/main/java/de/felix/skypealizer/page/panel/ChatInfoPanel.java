/**
 * 
 */
package de.felix.skypealizer.page.panel;


import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import de.felix.skypealizer.SkypeALizerApp;
import de.felix.skypealizer.model.skype.SkypeChat;
import de.felix.skypealizer.model.skype.SkypeMessage;
import de.felix.skypealizer.model.skype.SkypeUser;
import de.felix.skypealizer.page.StatsDashboard;
import de.felix.skypealizer.util.ConfigUtil;
import de.felix.skypealizer.util.MessageTimeComparator;
import de.felix.skypealizer.util.SkypeUserMessageComparator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.Duration;
import org.vaadin.navigator7.PageResource;
import org.vaadin.navigator7.uri.ParamPageResource;

/**
 *
 * @author felix.husse
 */
public class ChatInfoPanel extends Panel {
    
    private String dbName;

    public ChatInfoPanel(SkypeChat skypeChat, int height, int width, String dbName) {
        this.dbName = dbName;
        this.setCaption(skypeChat.getChatName());
        ((VerticalLayout)this.getContent()).setMargin(false);
        this.setWidth(width + "px");
        this.setHeight(height+ "px");
        
        GridLayout gridLayout = new GridLayout(2, 4);
        gridLayout.setSizeFull();
        gridLayout.setMargin(true);
        gridLayout.setColumnExpandRatio(1, 0.75f);
        gridLayout.setColumnExpandRatio(0, 0.25f);
        
        LinkedList<SkypeMessage> skypeMessages = new LinkedList<SkypeMessage>();
        VerticalLayout userListLayout = new VerticalLayout();
        Collections.sort(skypeChat.getSkypeUsers(), new SkypeUserMessageComparator());
        for (SkypeUser skypeUser : skypeChat.getSkypeUsers()) {
            StringBuilder userLabelText = new StringBuilder();
            userLabelText.append(skypeUser.getDisplayName()).append(" (").append(skypeUser.getSkypeMessages().size()).append(")");
            Label userLabel = new Label(userLabelText.toString());
            userListLayout.addComponent(userLabel);
            skypeMessages.addAll(skypeUser.getSkypeMessages());
        }
        
        
        gridLayout.addComponent(new Label("Range"), 0, 0);
        Label rangeLabel = createRangeLabel(skypeMessages);
        gridLayout.addComponent(rangeLabel, 1, 0);   
        
        gridLayout.addComponent(new Label("Messages"), 0, 1);
        Label messageCountLabel = createMessageCountLable(skypeMessages);
        gridLayout.addComponent(messageCountLabel, 1, 1);
        
        gridLayout.addComponent(new Label("User"), 0, 2);
        gridLayout.addComponent(userListLayout, 1, 2);

        Button moreDetail = createButton(skypeChat);
        gridLayout.addComponent(moreDetail, 0, 3, 1, 3);
        gridLayout.setComponentAlignment(gridLayout, Alignment.TOP_CENTER);
        
        this.setContent(gridLayout);
    }
    
    private Label createRangeLabel(List<SkypeMessage> skypeMessages) {
        Collections.sort(skypeMessages, new MessageTimeComparator());
        
        StringBuilder rangeLabelText = new StringBuilder();
        if (skypeMessages.size()>1) {
            
            Duration duration = new Duration(skypeMessages.get(0).getTimeStamp(), skypeMessages.get(skypeMessages.size()-1).getTimeStamp());
            int days = ((int)(duration.getMillis()/1000/60/60/24));
            if (days==0) {
                days = 1;
            }
            rangeLabelText.append(skypeMessages.get(0).getTimeStamp().toString(ConfigUtil.dtFormatter))
                .append("-")
                .append(skypeMessages.get(skypeMessages.size()-1).getTimeStamp().toString(ConfigUtil.dtFormatter))
                .append("<br>(").append(days).append(" days)");
        }
        else {
            rangeLabelText.append("No data");
        }
        Label label = new Label(rangeLabelText.toString(), Label.CONTENT_XHTML);    
        return label;
    }
    
    private Label createMessageCountLable(List<SkypeMessage> skypeMessages) {
        StringBuilder messageCountText = new StringBuilder();
        if (skypeMessages.size()>1) {
            Duration duration = new Duration(skypeMessages.get(0).getTimeStamp(), skypeMessages.get(skypeMessages.size()-1).getTimeStamp());
            int totalMessageCount = skypeMessages.size();
            int averageMessages = (int) ((double)(totalMessageCount) / ((double)duration.getMillis()/1000/60/60/24));
            if (averageMessages > totalMessageCount) {
                averageMessages = totalMessageCount;
            }
            messageCountText.append(totalMessageCount).append(" Msgs. ( Ø ")
                    .append(averageMessages).append(" per day)");    
        }
        else {
            messageCountText.append("No data");
        }
        
        Label label = new Label(messageCountText.toString());
        
        return label;
    }
    
    private Button createButton(SkypeChat skypeChat) {
        Button button = new Button("more");
        button.setData(skypeChat.getConvoId());
        
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                String chatId = event.getButton().getData().toString();
                PageResource pr = (new ParamPageResource(StatsDashboard.class, chatId))   // Strong (typed) link construction
                                    .addParam("db", dbName);
                SkypeALizerApp.getCurrentNavigableAppLevelWindow().getNavigator()
                                          .navigateTo(pr);
            }
        });
        
        return button;
    }
    
}
