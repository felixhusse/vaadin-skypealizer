/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.page.statistic;

import com.invient.vaadin.charts.Color.RGB;
import com.invient.vaadin.charts.InvientCharts;
import com.invient.vaadin.charts.InvientCharts.SeriesType;
import com.invient.vaadin.charts.InvientChartsConfig;
import de.felix.skypealizer.model.skype.SkypeChat;
import java.util.LinkedHashSet;
import java.util.List;


/**
 *
 * @author felixhusse
 */
public class MessageLengthStatsPanel extends BaseStatsPanel {
    
    public static final String STAT_CAPTION = "Average Message Length";

    public MessageLengthStatsPanel(int chartWidth, int chartHeight,SkypeChat skypeChat){
        super(STAT_CAPTION, chartWidth, chartHeight);
        initStats(skypeChat);
        this.setWidth((chartWidth+50)+"px");
        this.setHeight((chartHeight+60)+"px");
    }

    @Override
    public void initStats(SkypeChat skypeChat) {

        



//        BarChart bc = new BarChart();
//
//	bc.addXAxisLabel("Average Message Length");
//
//        for (SkypeUser skypeUser : skypeChat.getSkypeUsers()) {
//            double avrgMessageLength = 0;
//            for (SkypeMessage message : skypeUser.getSkypeMessages()) {
//                if (message.getMessageText() != null) {
//                    avrgMessageLength =avrgMessageLength + message.getMessageText().length();
//                }
//            }
//            avrgMessageLength = avrgMessageLength / skypeUser.getSkypeMessages().size();
//            bc.add(skypeUser.getDisplayName(),new double[]{avrgMessageLength});
//            System.out.println(skypeUser.getDisplayName() + " : " + avrgMessageLength);
//        }
//	bc.setOption("width", chartWidth);
//	bc.setOption("height", chartHeight);
//	bc.setOption("is3D", true);
//
//        this.addComponent(bc);
//
//        bc.setWidth((chartWidth+50)+"px");
//        bc.setHeight(chartHeight+"px");
        
    }

}
