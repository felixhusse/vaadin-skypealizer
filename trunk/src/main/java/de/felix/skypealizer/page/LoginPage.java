/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.page;


import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import de.felix.skypealizer.SkypeALizerApp;
import de.felix.skypealizer.SkypeAppLevelWindow;
import org.vaadin.navigator7.Navigator.NavigationEvent;
import org.vaadin.navigator7.Page;
import org.vaadin.navigator7.ParamChangeListener;

/**
 *
 * @author felixhusse
 */
@Page(uriName="login")
public class LoginPage extends VerticalLayout implements ParamChangeListener {

    private TextField txtUsername;
    private PasswordField txtPassword;
    private Button btnLogin;
    private Button btnReset;
    private CheckBox chkRememberMe;
    
    private String dashId = "";
    
    public LoginPage() {
        ((SkypeAppLevelWindow)SkypeALizerApp.getCurrentNavigableAppLevelWindow()).changeStatus(false);
        ((SkypeAppLevelWindow)SkypeALizerApp.getCurrentNavigableAppLevelWindow()).resetWelcomeMessage();
        
        Panel loginPanel = new Panel("Please Login");
        loginPanel.setWidth("300px");
        loginPanel.setHeight("240px");

        GridLayout gridLayout = new GridLayout(2, 4);
        gridLayout.setSizeFull();
        gridLayout.setMargin(true);

        Label lblUsername = new Label("Username");
        txtUsername = new TextField();
        gridLayout.addComponent(lblUsername, 0, 0);
        gridLayout.setComponentAlignment(lblUsername, Alignment.MIDDLE_RIGHT);
        gridLayout.addComponent(txtUsername, 1, 0);
        gridLayout.setComponentAlignment(txtUsername, Alignment.MIDDLE_RIGHT);

        Label lblPassword = new Label("Password");
        txtPassword = new PasswordField();

        gridLayout.addComponent(lblPassword, 0, 1);
        gridLayout.setComponentAlignment(lblPassword, Alignment.MIDDLE_RIGHT);
        gridLayout.addComponent(txtPassword, 1, 1);
        gridLayout.setComponentAlignment(txtPassword, Alignment.MIDDLE_RIGHT);

        chkRememberMe = new CheckBox("Remember me");
        gridLayout.addComponent(chkRememberMe, 0, 2, 1, 2);
        gridLayout.setComponentAlignment(chkRememberMe, Alignment.MIDDLE_LEFT);

        btnLogin = new Button("login");
        btnLogin.setClickShortcut(KeyCode.ENTER, null);
        btnReset = new Button("reset");
        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.addComponent(btnLogin);
        buttonBar.addComponent(btnReset);
        gridLayout.addComponent(buttonBar,0, 3, 1, 3);
        gridLayout.setComponentAlignment(buttonBar, Alignment.MIDDLE_RIGHT);

        loginPanel.setContent(gridLayout);
        bind();
        this.addComponent(loginPanel);
        this.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
        
    }

    private void bind() {

        btnLogin.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                String userName = txtUsername.toString();
                String password = txtPassword.toString();
                if (((SkypeALizerApp)SkypeALizerApp.getCurrent()).login(userName, password, chkRememberMe.booleanValue())) {
                    ((SkypeAppLevelWindow)SkypeALizerApp.getCurrentNavigableAppLevelWindow()).changeStatus(true);
                    ((SkypeAppLevelWindow)SkypeALizerApp.getCurrentNavigableAppLevelWindow()).setWelcomeMessage(userName);
                    
                    if (dashId != null && dashId.length()>0) {
                            SkypeALizerApp.getCurrentNavigableAppLevelWindow().getNavigator()
                                            .navigateTo(StatsDashboard.class,dashId);
                        
                    }
                    else {
                        SkypeALizerApp.getCurrentNavigableAppLevelWindow().getNavigator()
                            .navigateTo(SkypeChatPage.class);
                    }
                    
                }
                else {
                    ((SkypeALizerApp)SkypeALizerApp.getCurrent()).getMainWindow().showNotification("","Samma...gehts noch!! Falscher Login du nase!",
                                                               Notification.TYPE_WARNING_MESSAGE);
                }
            }
        });

        btnReset.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                txtPassword.setValue("");
                txtUsername.setValue("");
            }
        });
    }

    @Override
    public void paramChanged(NavigationEvent navigationEvent){
        
        dashId = navigationEvent.getParams();
        
    }

}
