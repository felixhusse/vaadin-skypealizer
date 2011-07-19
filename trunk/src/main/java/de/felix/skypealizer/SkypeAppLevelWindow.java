/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import de.felix.skypealizer.page.LoginPage;
import org.vaadin.navigator7.window.HeaderFooterFixedAppLevelWindow;
import org.vaadin.navigator7.window.HeaderFooterFluidAppLevelWindow;

/**
 *
 * @author felixhusse
 */
public class SkypeAppLevelWindow extends HeaderFooterFluidAppLevelWindow {
    
    private Button logoutButton;
    private Button adminButton;
    private Label welcomeMessageLabel;
    private HorizontalLayout buttonBarLayout;
    
    @Override
    protected Component createHeader() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");
        
        VerticalLayout rightSideBar = new VerticalLayout();
        
        HorizontalLayout welcomeMessageLayout = new HorizontalLayout();
        welcomeMessageLabel = new Label("Please login", Label.CONTENT_XHTML);
        welcomeMessageLayout.addComponent(welcomeMessageLabel);
        
        buttonBarLayout = new HorizontalLayout();
        
        logoutButton = new Button("logout");
        logoutButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event){
                ((SkypeALizerApp)SkypeALizerApp.getCurrent()).logout();
                SkypeALizerApp.getCurrentNavigableAppLevelWindow().getNavigator()
                            .navigateTo(LoginPage.class);
            }
        });
        logoutButton.setEnabled(false);
        
        adminButton = new Button("admin");
        adminButton.setEnabled(false);
        
        buttonBarLayout.addComponent(logoutButton);
        buttonBarLayout.addComponent(adminButton);
        
        rightSideBar.addComponent(welcomeMessageLayout);
        rightSideBar.addComponent(buttonBarLayout);
        rightSideBar.setComponentAlignment(welcomeMessageLayout, Alignment.TOP_RIGHT);
        rightSideBar.setComponentAlignment(buttonBarLayout, Alignment.TOP_RIGHT);
        headerLayout.addComponent(new Label("SkypeALizer App"));
        headerLayout.addComponent(rightSideBar);
        
        return headerLayout;
    }

    @Override
    protected Component createFooter() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        return new Label("© Fatal Radikal, 2011");
    }
    
    public void changeStatus(boolean loggedIn) {
        adminButton.setEnabled(loggedIn);
        logoutButton.setEnabled(loggedIn);
    }
    
    public void resetWelcomeMessage() {
        welcomeMessageLabel.setValue("Please login");
    }
    
    public void setWelcomeMessage(String userName) {
        welcomeMessageLabel.setValue("Welcome," + userName);
    }
    
}
