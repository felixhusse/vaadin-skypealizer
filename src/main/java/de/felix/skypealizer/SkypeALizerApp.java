/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.felix.skypealizer;

import com.vaadin.Application;
import de.felix.skypealizer.exception.SkypeDatabaseException;
import de.felix.skypealizer.model.AppConfig;
import de.felix.skypealizer.model.skype.SkypeDatabase;
import de.felix.skypealizer.util.ConfigUtil;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.vaadin.navigator7.NavigableApplication;
import org.vaadin.navigator7.window.NavigableAppLevelWindow;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class SkypeALizerApp extends NavigableApplication {

    private static final long serialVersionUID = 1L;
    private static final String CONFIG_XML = "config.xml";

    private AppConfig appConfig;

    private ArrayList<SkypeDatabaseHandler> skypeDBHandlers = new ArrayList<SkypeDatabaseHandler>();

    public SkypeALizerApp() {
        try {
            setTheme("movember");
            initializeConfig();
            
        } catch (Exception ex) {
            Logger.getLogger(SkypeALizerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initializeConfig() throws Exception {
        String configHome = System.getenv("SKYPEALIZER_HOME").toString();//read the environment variable named CONFIG_PATH
        File configPath =new File(configHome);
        
        if (!configPath.exists()) {
            configPath.mkdirs();
        }
        
        if (configPath.exists() && configPath.canRead()) {
            File configFile = new File(configPath, CONFIG_XML);
            Serializer serializer = new Persister();
            if (!configFile.exists()) {
                //LOAD DEFAULT CONFIG

                String userHome = System.getProperty("user.home");
                String skypeDBPath = "";
                if (ConfigUtil.isMac()) {

                    skypeDBPath = "Library/Application Support/Skype";
                }
                else if (ConfigUtil.isWindows()) {
                    skypeDBPath = "AppData/Roaming/Skype";
                }
                File skypeFolder = new File(userHome, skypeDBPath);

                File[] skypeProfiles = skypeFolder.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File file) {
                        if (file.isDirectory()) {
                            if (!file.getName().contains("shared")) {
                               return true;
                            }
                        }
                        return false;
                    }
                });

                appConfig = new AppConfig();
                for (int i = 0; i < skypeProfiles.length; i++) {
                    SkypeDatabase skypeDatabase = new SkypeDatabase(skypeProfiles[i].getName(), new File(skypeProfiles[i],"main.db"));
                    if (i == 0) {
                        skypeDatabase.setDefault(true);
                    }
                    appConfig.getSkypeDatabases().add(skypeDatabase);
                }
                serializer.write(appConfig, configFile);
            }
            else {
                appConfig = serializer.read(AppConfig.class, configFile);
            }

        }
    }


    
    public boolean isAuthenticated() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            return true;
        }
            return false;
    }

    public boolean login(String username, String password, boolean rememberMe) {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(rememberMe);
        
        try {
            currentUser.login(token);
            return true;
        } catch (AuthenticationException ex) {
            return false;
        }
    }

    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            currentUser.logout();
        }
        for(SkypeDatabaseHandler handler : skypeDBHandlers) {
            try {
                handler.closeSkypeDatabaseConnection();
            } catch (SkypeDatabaseException ex) {
                Logger.getLogger(SkypeALizerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        super.transactionEnd(application, transactionData);
        for(SkypeDatabaseHandler handler : skypeDBHandlers) {
            try {
                handler.closeSkypeDatabaseConnection();
            } catch (SkypeDatabaseException ex) {
                Logger.getLogger(SkypeALizerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    

    @Override
    public NavigableAppLevelWindow createNewNavigableAppLevelWindow() {
        return new SkypeAppLevelWindow();
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }
    
    public List<SkypeDatabaseHandler> getDBHandlers() {
        return skypeDBHandlers;
    }

    public void addDBHandler(SkypeDatabaseHandler skypeDatabaseHandler) {
        skypeDBHandlers.add(skypeDatabaseHandler);
    }

}
