/**
 * 
 */
package de.felix.skypealizer.page.statistic;


import com.invient.vaadin.charts.Color.RGB;
import com.invient.vaadin.charts.Color.RGBA;
import com.invient.vaadin.charts.Gradient;
import com.invient.vaadin.charts.Gradient.LinearGradient.LinearColorStop;
import com.invient.vaadin.charts.InvientCharts;
import com.invient.vaadin.charts.InvientChartsConfig;
import com.vaadin.terminal.Sizeable;
import de.felix.skypealizer.model.skype.SkypeChat;
import de.felix.skypealizer.model.skype.SkypeMessage;
import de.felix.skypealizer.model.skype.SkypeUser;
import de.felix.skypealizer.util.MessageTimeComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 *
 * @author felix.husse
 */
public class HistoryStatsPanel extends BaseStatsPanel {
    
    public static final String STAT_CAPTION = "Chat History";
    
    public HistoryStatsPanel(int chartWidth, int chartHeight){
        super(STAT_CAPTION, chartWidth, chartHeight);
        
    }
    
    @Override
    public void initStats(SkypeChat skypeChat){
        
        LinkedList<SkypeMessage> allMessages = new LinkedList<SkypeMessage>();
        
        for (SkypeUser skypeUser : skypeChat.getSkypeUsers()) {
            allMessages.addAll(skypeUser.getSkypeMessages());
        }
        Collections.sort(allMessages,new MessageTimeComparator()); 
        
        InvientChartsConfig chartConfig = new InvientChartsConfig();
        chartConfig.getGeneralChartConfig().setZoomType(InvientChartsConfig.GeneralChartConfig.ZoomType.X);
        chartConfig.getGeneralChartConfig().setSpacing(new InvientChartsConfig.GeneralChartConfig.Spacing());
        chartConfig.getGeneralChartConfig().getSpacing().setRight(20);
        
        chartConfig.getSubtitle().setText("Click and drag in the plot area to zoom in");
        
        InvientChartsConfig.DateTimeAxis xAxis = new InvientChartsConfig.DateTimeAxis();
        xAxis.setMaxZoom(14 * 24 * 3600000);
        LinkedHashSet<InvientChartsConfig.XAxis> xAxesSet = new LinkedHashSet<InvientChartsConfig.XAxis>();
        xAxesSet.add(xAxis);
        chartConfig.setXAxes(xAxesSet);
        
        InvientChartsConfig.NumberYAxis yAxis = new InvientChartsConfig.NumberYAxis();
        yAxis.setTitle(new InvientChartsConfig.AxisBase.AxisTitle("Messages"));
        yAxis.setMin(0.6);
        yAxis.setStartOnTick(true);
        yAxis.setShowFirstLabel(false);
        LinkedHashSet<InvientChartsConfig.YAxis> yAxesSet = new LinkedHashSet<InvientChartsConfig.YAxis>();
        yAxesSet.add(yAxis);
        chartConfig.setYAxes(yAxesSet);
        
        chartConfig.getTooltip().setShared(true);
        chartConfig.getLegend().setEnabled(false);
        
        // Set plot options
        InvientChartsConfig.AreaConfig areaCfg = new InvientChartsConfig.AreaConfig();

        List<LinearColorStop> colorStops = new ArrayList<Gradient.LinearGradient.LinearColorStop>();
        colorStops.add(new LinearColorStop(0, new RGB(69, 114, 167)));
        colorStops.add(new LinearColorStop(1, new RGBA(2, 0, 0, 0)));
        // Fill color
        //areaCfg.setFillColor(new Gradient.LinearGradient(0, 0, 0, 300, colorStops));
        
        areaCfg.setLineWidth(2);
        areaCfg.setShadow(false);
        areaCfg.setHoverState(new InvientChartsConfig.SeriesState());
        areaCfg.getHoverState().setLineWidth(1);
        InvientChartsConfig.SymbolMarker marker;
        areaCfg.setMarker(marker = new InvientChartsConfig.SymbolMarker(false));
        marker.setHoverState(new InvientChartsConfig.MarkerState());
        marker.getHoverState().setEnabled(true);
        marker.getHoverState().setRadius(5);
        
        chartConfig.addSeriesConfig(areaCfg);
        InvientCharts chart = new InvientCharts(chartConfig);
        
        // Area configuration
        InvientChartsConfig.AreaConfig serieaAreaCfg = new InvientChartsConfig.AreaConfig();
        // LOAD MIN DATE!
        System.out.println("Min Date: " + allMessages.getFirst().getTimeStamp().toGregorianCalendar().getTimeInMillis());
        serieaAreaCfg.setPointStart((double) allMessages.getFirst().getTimeStamp().toGregorianCalendar().getTimeInMillis());  
        serieaAreaCfg.setPointInterval(24 * 3600 * 1000.0);
        
        // Series
        //InvientCharts.DateTimeSeries dateTimeSeries = new InvientCharts.DateTimeSeries("Total Messages",InvientCharts.SeriesType.AREA, serieaAreaCfg);
        DateTime dtFirstMsg = allMessages.getFirst().getTimeStamp();
        DateTime dtLastMsg = allMessages.getLast().getTimeStamp();
        //dateTimeSeries.addPoint((InvientCharts.DateTimePoint[]) getDateTimeSeriesPoints(dateTimeSeries, allMessages, dtFirstMsg, dtLastMsg));
        //chart.addSeries(dateTimeSeries);
        
        for (SkypeUser skypeUser : skypeChat.getSkypeUsers()) {
            InvientCharts.DateTimeSeries userTimeSeries = new InvientCharts.DateTimeSeries(skypeUser.getDisplayName(),InvientCharts.SeriesType.AREA, serieaAreaCfg);
            LinkedList<SkypeMessage> userMessages = new LinkedList<SkypeMessage>();
            userMessages.addAll(skypeUser.getSkypeMessages());
            Collections.sort(userMessages,new MessageTimeComparator()); 
            userTimeSeries.addPoint((InvientCharts.DateTimePoint[]) getDateTimeSeriesPoints(userTimeSeries, userMessages,dtFirstMsg,dtLastMsg));
            chart.addSeries(userTimeSeries);
        }
        

        chart.setHeight(chartHeight, Sizeable.UNITS_PIXELS);
        //chart.setWidth(chartWidth, Sizeable.UNITS_PIXELS);

        this.addComponent(chart);
        
    }
    
    private InvientCharts.DateTimePoint[] getDateTimeSeriesPoints(InvientCharts.DateTimeSeries series, LinkedList<SkypeMessage> allMessages, DateTime firstMessageTimeStamp, DateTime lastMessageTimeStamp) {
        Duration totalDuration = new Duration(firstMessageTimeStamp, lastMessageTimeStamp);
        int totalDays = ((int)(totalDuration.getMillis()/1000/60/60/24));
        if (totalDays==0) {
            totalDays = 1;
        }
        totalDays++;
        
        double[] values = new double[totalDays];
        int messageCount = 0;
        for (SkypeMessage message : allMessages) {
            messageCount++;
            Duration duration = new Duration(firstMessageTimeStamp, message.getTimeStamp());
            int day = ((int)(duration.getMillis()/1000/60/60/24));
            values[day] = messageCount;
        }
        double valueBefore = 0d;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < valueBefore) {
                values[i] = valueBefore;
            }
            valueBefore = values[i];
        }
        
        return getDateTimePoints(series, values).toArray(new InvientCharts.DateTimePoint[0]);
    }
    
    private LinkedHashSet<InvientCharts.DateTimePoint> getDateTimePoints(InvientCharts.Series series,double... values) {
        LinkedHashSet<InvientCharts.DateTimePoint> points = new LinkedHashSet<InvientCharts.DateTimePoint>();
        for (double value : values) {
            points.add(new InvientCharts.DateTimePoint(series, value));
        }
        return points;
    }

    
}
