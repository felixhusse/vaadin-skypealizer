/**
 * The contents of this file are copyright (c) 2011 by medavis GmbH, Karlsruhe, Germany
 */
package de.felix.skypealizer.page.statistic;

import com.invient.vaadin.charts.InvientCharts;
import com.invient.vaadin.charts.InvientChartsConfig;
import com.vaadin.terminal.Sizeable;
import de.felix.skypealizer.model.skype.SkypeChat;
import de.felix.skypealizer.model.skype.SkypeUser;
import java.util.LinkedHashSet;


/**
 *
 * @author felix.husse
 */
public class UserUsagePanel extends BaseStatsPanel {
    
    public static final String STAT_CAPTION = "User Statistics";
    
    public UserUsagePanel(int chartWidth, int chartHeight){
        super(STAT_CAPTION, chartWidth, chartHeight);
    }
    
    @Override
    public void initStats(SkypeChat skypeChat){

        InvientChartsConfig chartConfig = new InvientChartsConfig();
        chartConfig.getGeneralChartConfig().setType(InvientCharts.SeriesType.PIE);

        chartConfig.getGeneralChartConfig().setMargin(new InvientChartsConfig.GeneralChartConfig.Margin());
        chartConfig.getGeneralChartConfig().getMargin().setTop(50);
        chartConfig.getGeneralChartConfig().getMargin().setRight(0);
        chartConfig.getGeneralChartConfig().getMargin().setBottom(0);
        chartConfig.getGeneralChartConfig().getMargin().setLeft(0);

        chartConfig.getTitle().setText(
                "User Usage");


        InvientCharts chart = new InvientCharts(chartConfig);
        // Series 1
        //
        InvientChartsConfig.PieConfig pieCfg = new InvientChartsConfig.PieConfig();
        pieCfg.setInnerSize(65);
        pieCfg.setDataLabel(new InvientChartsConfig.PieDataLabel());

        InvientCharts.XYSeries series = new InvientCharts.XYSeries("2008", InvientCharts.SeriesType.PIE, pieCfg);
        LinkedHashSet<InvientCharts.DecimalPoint> points = new LinkedHashSet<InvientCharts.DecimalPoint>();

        for (SkypeUser skypeUser : skypeChat.getSkypeUsers()) {
            points.add(new InvientCharts.DecimalPoint(series,skypeUser.getDisplayName(), skypeUser.getSkypeMessages().size()));
        }
        
        series.setSeriesPoints(points);

        chart.addSeries(series);

        chart.setHeight(chartHeight, Sizeable.UNITS_PIXELS);
        //chart.setWidth(chartWidth, Sizeable.UNITS_PIXELS);

        this.addComponent(chart);
    }
    
}
