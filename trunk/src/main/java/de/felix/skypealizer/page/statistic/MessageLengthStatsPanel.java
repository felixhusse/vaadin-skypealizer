/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.page.statistic;

import com.invient.vaadin.charts.Color.RGB;
import com.invient.vaadin.charts.InvientCharts;
import com.invient.vaadin.charts.InvientCharts.SeriesType;
import com.invient.vaadin.charts.InvientChartsConfig;
import com.vaadin.terminal.Sizeable;
import de.felix.skypealizer.model.skype.SkypeChat;
import de.felix.skypealizer.model.skype.SkypeMessage;
import de.felix.skypealizer.model.skype.SkypeUser;
import java.util.Arrays;
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
    }

    @Override
    public void initStats(SkypeChat skypeChat) {
        InvientChartsConfig chartConfig = new InvientChartsConfig();
        chartConfig.getGeneralChartConfig().setType(SeriesType.COLUMN);

        chartConfig.getTitle().setText("Average Message Length");

        InvientChartsConfig.CategoryAxis xAxis = new InvientChartsConfig.CategoryAxis();
        xAxis.setCategories(Arrays.asList("User"));
        LinkedHashSet<InvientChartsConfig.XAxis> xAxesSet = new LinkedHashSet<InvientChartsConfig.XAxis>();
        xAxesSet.add(xAxis);
        chartConfig.setXAxes(xAxesSet);

        InvientChartsConfig.NumberYAxis yAxis = new InvientChartsConfig.NumberYAxis();
        yAxis.setMin(0.0);
        yAxis.setTitle(new InvientChartsConfig.AxisBase.AxisTitle("Message length (chars)"));
        LinkedHashSet<InvientChartsConfig.YAxis> yAxesSet = new LinkedHashSet<InvientChartsConfig.YAxis>();
        chartConfig.setYAxes(yAxesSet);

        chartConfig.getTooltip().setFormatterJsFunc(
                "function() {" + " return '' + this.x +': '+ this.y +' chars'; "
                        + "}");

        InvientChartsConfig.ColumnConfig colCfg = new InvientChartsConfig.ColumnConfig();
        colCfg.setPointPadding(0.2);
        colCfg.setBorderWidth(0);

        chartConfig.addSeriesConfig(colCfg);

        InvientCharts chart = new InvientCharts(chartConfig);

        for (SkypeUser skypeUser : skypeChat.getSkypeUsers()) {
            double avrgMessageLength = 0;
            for (SkypeMessage message : skypeUser.getSkypeMessages()) {
                if (message.getMessageText() != null) {
                    avrgMessageLength =avrgMessageLength + message.getMessageText().length();
                }
            }
            avrgMessageLength = avrgMessageLength / skypeUser.getSkypeMessages().size();
            InvientCharts.XYSeries seriesData = new InvientCharts.XYSeries(skypeUser.getDisplayName());
            seriesData.setSeriesPoints(getPoints(seriesData, avrgMessageLength));
            chart.addSeries(seriesData);
        }

        chart.setHeight(chartHeight, Sizeable.UNITS_PIXELS);
        //chart.setWidth(chartWidth, Sizeable.UNITS_PIXELS);

        this.addComponent(chart);
        
    }

    private static LinkedHashSet<InvientCharts.DecimalPoint> getPoints(InvientCharts.Series series,
            double... values) {
        LinkedHashSet<InvientCharts.DecimalPoint> points = new LinkedHashSet<InvientCharts.DecimalPoint>();
        for (double value : values) {
            points.add(new InvientCharts.DecimalPoint(series, value));
        }
        return points;
    }

}
