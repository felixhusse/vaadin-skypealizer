/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer;

import com.vaadin.Application;
import com.vaadin.ui.Window;
import java.io.BufferedWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.vaadin.navigator7.NavigableApplication;
import org.vaadin.navigator7.NavigableApplicationServlet;

/**
 *
 * @author felixhusse
 */
public class SkypeALizerAppServlet extends NavigableApplicationServlet {

    @Override
    protected void writeAjaxPageHtmlVaadinScripts(Window window,
            String themeName, Application application, BufferedWriter page,
            String appUrl, String themeUri, String appId,
            HttpServletRequest request) throws ServletException, IOException {
        page.write("<script type=\"text/javascript\">\n");
        page.write("//<![CDATA[\n");
        page.write("document.write(\"<script language='javascript' src='./VAADIN/jquery/jquery-1.4.4.min.js'><\\/script>\");\n");
        page.write("document.write(\"<script language='javascript' src='./VAADIN/js/highcharts.js'><\\/script>\");\n");
        page.write("document.write(\"<script language='javascript' src='./VAADIN/js/modules/exporting.js'><\\/script>\");\n");
        page.write("//]]>\n</script>\n");
        super.writeAjaxPageHtmlVaadinScripts(window, themeName, application,
                page, appUrl, themeUri, appId, request);
    }

}
