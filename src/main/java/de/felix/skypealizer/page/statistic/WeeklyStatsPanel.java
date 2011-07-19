/**
 * 
 */
package de.felix.skypealizer.page.statistic;

import com.invient.vaadin.charts.Color.RGB;
import com.invient.vaadin.charts.InvientCharts;
import com.invient.vaadin.charts.InvientChartsConfig;
import com.vaadin.terminal.Sizeable;
import de.felix.skypealizer.model.skype.SkypeChat;
import de.felix.skypealizer.model.skype.SkypeMessage;
import de.felix.skypealizer.model.skype.SkypeUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import org.joda.time.DateTimeConstants;


/**
 *
 * @author felix.husse
 */
public class WeeklyStatsPanel extends BaseStatsPanel {
    
    public static final String STAT_CAPTION = "Weekly Usage";
    
    public WeeklyStatsPanel(int chartWidth, int chartHeight){
        super(STAT_CAPTION, chartWidth, chartHeight);
    }
    
    
    
    @Override
    public void initStats(SkypeChat skypeChat){

        InvientChartsConfig chartConfig = new InvientChartsConfig();
        chartConfig.getGeneralChartConfig().setType(InvientCharts.SeriesType.COLUMN);

        chartConfig.getTitle().setText("");

        InvientChartsConfig.CategoryAxis xAxis = new InvientChartsConfig.CategoryAxis();
        
        LinkedHashSet<InvientChartsConfig.XAxis> xAxesSet = new LinkedHashSet<InvientChartsConfig.XAxis>();
        xAxesSet.add(xAxis);
        chartConfig.setXAxes(xAxesSet);

        InvientChartsConfig.NumberYAxis numberYAxis = new InvientChartsConfig.NumberYAxis();
        numberYAxis.setMin(0.0);
        numberYAxis.setTitle(new InvientChartsConfig.AxisBase.AxisTitle("Messages"));
        LinkedHashSet<InvientChartsConfig.YAxis> yAxesSet = new LinkedHashSet<InvientChartsConfig.YAxis>();
        yAxesSet.add(numberYAxis);
        chartConfig.setYAxes(yAxesSet);

        InvientChartsConfig.Legend legend = new InvientChartsConfig.Legend();
        legend.setBackgroundColor(new RGB(255, 255, 255));
        legend.setReversed(true);
        chartConfig.setLegend(legend);

        chartConfig.getTooltip().setFormatterJsFunc(
                "function() {"
                        + " return ''+ this.series.name +': '+ this.y +''; "
                        + "}");

        InvientChartsConfig.SeriesConfig seriesCfg = new InvientChartsConfig.SeriesConfig();
        seriesCfg.setStacking(InvientChartsConfig.Stacking.NORMAL);
        chartConfig.addSeriesConfig(seriesCfg);

        InvientCharts chart = new InvientCharts(chartConfig);

        double[] dataSunday = new double[skypeChat.getSkypeUsers().size()];
        double[] dataMonday = new double[skypeChat.getSkypeUsers().size()];
        double[] dataTuesday = new double[skypeChat.getSkypeUsers().size()];
        double[] dataWednesday = new double[skypeChat.getSkypeUsers().size()];
        double[] dataThursday = new double[skypeChat.getSkypeUsers().size()];
        double[] dataFriday = new double[skypeChat.getSkypeUsers().size()];
        double[] dataSaturday = new double[skypeChat.getSkypeUsers().size()];
        List<String> categories = new ArrayList<String>();

        for (int i = 0; i < skypeChat.getSkypeUsers().size(); i++) {
            dataSunday[i] = 0d;
            dataMonday[i] = 0d;
            dataTuesday[i] = 0d;
            dataWednesday[i] = 0d;
            dataThursday[i] = 0d;
            dataFriday[i] = 0d;
            dataSaturday[i] = 0d;
            categories.add(skypeChat.getSkypeUsers().get(i).getDisplayName());
        }
        xAxis.setCategories(categories);
        int userIndex = 0;
        for (SkypeUser skypeUser : skypeChat.getSkypeUsers()) {
            for (SkypeMessage message : skypeUser.getSkypeMessages()) {
                switch (message.getTimeStamp().getDayOfWeek()) {
                    case DateTimeConstants.SUNDAY:
                        dataSunday[userIndex] = dataSunday[userIndex]+1;
                        break;
                    case DateTimeConstants.MONDAY:
                        dataMonday[userIndex] = dataMonday[userIndex]+1;
                        break;
                    case DateTimeConstants.TUESDAY:
                        dataTuesday[userIndex] = dataTuesday[userIndex]+1;
                        break;
                    case DateTimeConstants.THURSDAY:
                        dataThursday[userIndex] = dataThursday[userIndex]+1;
                        break;
                    case DateTimeConstants.WEDNESDAY:
                        dataWednesday[userIndex] = dataWednesday[userIndex]+1;
                        break;
                    case DateTimeConstants.FRIDAY:
                        dataFriday[userIndex] = dataFriday[userIndex]+1;
                        break;
                    case DateTimeConstants.SATURDAY:
                        dataSaturday[userIndex] = dataSaturday[userIndex]+ 1d;
                        break;
                }
            }
            userIndex++;
        }

        InvientCharts.XYSeries seriesData = new InvientCharts.XYSeries("Sunday");
        seriesData.setSeriesPoints(getPoints(seriesData, dataSunday));
        chart.addSeries(seriesData);

        seriesData = new InvientCharts.XYSeries("Monday");
        seriesData.setSeriesPoints(getPoints(seriesData, dataMonday));
        chart.addSeries(seriesData);

        seriesData = new InvientCharts.XYSeries("Tuesday");
        seriesData.setSeriesPoints(getPoints(seriesData, dataTuesday));
        chart.addSeries(seriesData);

        seriesData = new InvientCharts.XYSeries("Wednesday");
        seriesData.setSeriesPoints(getPoints(seriesData, dataWednesday));
        chart.addSeries(seriesData);

        seriesData = new InvientCharts.XYSeries("Thursday");
        seriesData.setSeriesPoints(getPoints(seriesData, dataThursday));
        chart.addSeries(seriesData);

        seriesData = new InvientCharts.XYSeries("Friday");
        seriesData.setSeriesPoints(getPoints(seriesData, dataFriday));
        chart.addSeries(seriesData);

        seriesData = new InvientCharts.XYSeries("Saturday");
        seriesData.setSeriesPoints(getPoints(seriesData, dataSaturday));
        chart.addSeries(seriesData);

        chart.setHeight(chartHeight, Sizeable.UNITS_PIXELS);
        //chart.setWidth(chartWidth, Sizeable.UNITS_PIXELS);

        this.addComponent(chart);
//        cc.add("Sun", dataSunday);
//	cc.add("Mon", dataMonday);
//	cc.add("Tue", dataTuesday);
//	cc.add("Wed", dataWednesday);
//	cc.add("Thu", dataThursday);
//        cc.add("Fri", dataFriday);
//        cc.add("Sat", dataSaturday);
//
//        cc.setWidth((chartWidth+50)+"px");
//        cc.setHeight(chartHeight+"px");
//
//        this.addComponent(cc);
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
