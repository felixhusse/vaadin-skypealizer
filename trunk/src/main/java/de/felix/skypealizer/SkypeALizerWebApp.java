/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer;

import de.felix.skypealizer.page.AdminPage;
import de.felix.skypealizer.page.StatsDashboard;
import de.felix.skypealizer.page.LoginPage;
import de.felix.skypealizer.page.SkypeChatPage;
import de.felix.skypealizer.util.AuthorizationInterceptor;
import org.vaadin.navigator7.WebApplication;
import org.vaadin.navigator7.uri.ParamUriAnalyzer;

/**
 *
 * @author felixhusse
 */
public class SkypeALizerWebApp extends WebApplication {

    public SkypeALizerWebApp() {
        registerPages(new Class[] {
                    SkypeChatPage.class,
                    StatsDashboard.class,
                    LoginPage.class,
                    AdminPage.class
        });
        
        setUriAnalyzer(new ParamUriAnalyzer());
    }

    @Override
    protected void registerInterceptors() {
        // 1st interceptor to call: check if user really wanna quit.
        
        registerInterceptor(new AuthorizationInterceptor());
        super.registerInterceptors();   // Default interceptors.
    }

 


}
