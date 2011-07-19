/**
 * The contents of this file are copyright (c) 2011 by medavis GmbH, Karlsruhe, Germany
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
        this.setWidth((chartWidth+50)+"px");
        this.setHeight((chartHeight+60)+"px");
        
    }
    
    public abstract void initStats(de.felix.skypealizer.model.skype.SkypeChat skypeChat);

    
}