/**
 * 
 */
package de.felix.skypealizer.page.statistic;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author felix.husse
 */
public abstract class BaseStatsPanel extends Panel {
    
    protected String caption;
    protected int chartWidth;
    protected int chartHeight;
    
    public BaseStatsPanel(String caption, int chartWidth, int chartHeight) {
        this.setCaption(caption);
        this.chartHeight = chartHeight;
        this.chartWidth = chartWidth;
        ((VerticalLayout)this.getContent()).setMargin(false);
        
    }
    
    public abstract void initStats(de.felix.skypealizer.model.skype.SkypeChat skypeChat);

    
}
