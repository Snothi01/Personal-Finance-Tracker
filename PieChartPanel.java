import javax.swing.*;
import java.awt.*;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Lightweight wrapper to host a JFreeChart pie chart and update it from a Map<Category, Double>
 */
public class PieChartPanel extends RoundedPanel {
    private DefaultPieDataset dataset;
    private ChartPanel chartPanel;

    public PieChartPanel() {
        super(18);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        dataset = new DefaultPieDataset();
        JFreeChart chart = ChartFactory.createPieChart(
                "Expense Breakdown", dataset, true, true, false);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPopupMenu(null);
        chartPanel.setDomainZoomable(false);
        chartPanel.setPreferredSize(new Dimension(320, 300));
        add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Set data for the pie chart. Map keys are category labels and values are amounts.
     */
    public void setData(Map<?, Double> map) {
        dataset.clear();
        if (map == null || map.isEmpty()) {
            dataset.setValue("No data", 1.0);
            return;
        }
        for (Map.Entry<?, Double> e : map.entrySet()) {
            String key = e.getKey() == null ? "Other" : e.getKey().toString();
            double val = e.getValue() == null ? 0.0 : e.getValue();
            if (val <= 0) continue;
            dataset.setValue(key, val);
        }
    }
}
