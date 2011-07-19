/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.util;

import com.vaadin.ui.Component;
import de.felix.skypealizer.SkypeALizerApp;
import de.felix.skypealizer.page.LoginPage;
import org.vaadin.navigator7.interceptor.Interceptor;
import org.vaadin.navigator7.interceptor.PageInvocation;

/**
 *
 * @author felixhusse
 */
public class AuthorizationInterceptor implements Interceptor {

    @Override
    public void intercept(PageInvocation pageInvocation) {
        
        Class<? extends Component> pageClazz = pageInvocation.getPageClass();
        

        if (!((SkypeALizerApp)SkypeALizerApp.getCurrent()).isAuthenticated() 
                && !(pageClazz.getCanonicalName().equals(LoginPage.class.getCanonicalName()))) {
            
            pageInvocation.setPageClass(LoginPage.class);
            pageInvocation.setParams(pageInvocation.getParams());
            
            System.out.println("YOU ARE NOT AUTHORIZED!!!");
        }

        pageInvocation.invoke();
    }

}
