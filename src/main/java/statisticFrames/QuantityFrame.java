package statisticFrames;

import bookManagement.BookStatistics;
import com.sun.java.swing.plaf.windows.WindowsButtonUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;
import gui.swing.CusJFrame;
import gui.swing.SwingFactory;
import main.MainFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import statisticFrames.data.QuantityDataGetter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicPanelUI;
import java.awt.*;
import java.util.List;

public class QuantityFrame extends CusJFrame {

    private SwingFactory swingFactory = new SwingFactory(this);

    private JButton refresh;
    private JCheckBox hideZeroMonths;
    private JComboBox<String> from;
    private JComboBox<String> to;
    private JToggleButton yearOrMonth;
    private JToggleButton bookOrPage;
    private JToggleButton barOrLine;

    private ChartPanel chartPanel = null;
    private JFreeChart chart;

    private MainFrame mainFrame;
    private BookStatistics bookStatistics;

    private QuantityDataGetter dataGetter;

    QuantityFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        bookStatistics = new BookStatistics(this.mainFrame);
        prepareFrame();
        createUpperBar();
        createLowerBar();

        setVisible(true);
        refreshGraph();
    }

    private void refreshGraph() {
        if (chartPanel != null) {
            remove(chartPanel);
        }

        dataGetter = new QuantityDataGetter(mainFrame, getChartTitle(), yearOrMonth.isSelected(), bookOrPage.isSelected(),
                getStartingYear(), getEndingYear(), hideZeroMonths.isSelected());
        CategoryDataset dataset = dataGetter.getData();
        chart = null;
        if (barOrLine.isSelected()) {
            chart = ChartFactory.createBarChart(getChartTitle(), yearOrMonth.isSelected() ? "Jahr" : "Monat",
                    bookOrPage.isSelected() ? "gelesene Bücher" : "gelesene Seiten", dataset, PlotOrientation.VERTICAL, false, true, false);
        } else {
            chart = ChartFactory.createLineChart(getChartTitle(), yearOrMonth.isSelected() ? "Jahr" : "Monat",
                    bookOrPage.isSelected() ? "gelesene Bücher" : "gelesene Seiten", dataset, PlotOrientation.VERTICAL, false, true, false);
        }
        chartPanel = null;
        chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(mainFrame.secondaryColor);
        chartPanel.setSize(getWidth() - 50, getHeight() - 100 - (bookOrPage.getY() + bookOrPage.getHeight()));
        chartPanel.setUI(new BasicPanelUI());
        add(chartPanel);
        chartPanel.setLocation(25, bookOrPage.getY() + bookOrPage.getHeight() + 25);
        repaint();
    }

    private String getChartTitle() {
        String title = "";
        if (bookOrPage.isSelected()) {
            title += "Bücher";
        } else {
            title += "Seiten";
        }
        title += " pro ";
        if (yearOrMonth.isSelected()) {
            title += "Jahr";
        } else {
            title += "Monat";
        }

        return title;
    }

    private int getStartingYear() {
        int startingYear;
        String startingYearString = from.getSelectedItem().toString();

        if (startingYearString.equalsIgnoreCase("Früher")) {
            startingYear = 0;
        } else {
            startingYear = Integer.parseInt(startingYearString);
        }
        return startingYear;
    }

    private int getEndingYear() {
        int endingYear;
        String startingYearString = to.getSelectedItem().toString();

        if (startingYearString.equalsIgnoreCase("Früher")) {
            endingYear = 0;
        } else {
            endingYear = Integer.parseInt(startingYearString);
        }
        return endingYear;
    }

    private void createUpperBar() {
        createFromToGui();
        addAll(from, to);

        refresh = swingFactory.createButton("Refresh", new Dimension(200, 75), getWidth() - 200 - 25, 25, Color.WHITE,
                new Font("Times New Roman", Font.BOLD, 40), mainFrame.fontColor, new LineBorder(mainFrame.secondaryColor, 5), e -> refreshGraph());

        hideZeroMonths = new JCheckBox();
        hideZeroMonths.setSize(75, 75);
        hideZeroMonths.setUI(new WindowsButtonUI());
        hideZeroMonths.setLocation(refresh.getX() - 175, refresh.getY());
        add(hideZeroMonths);
        swingFactory.createLabel("Alle Monate anzeigen", new Dimension(400, 64), hideZeroMonths.getX() - 400 - 25, hideZeroMonths.getY(),
                null, new Font("Times New Roman", Font.BOLD, 40), mainFrame.fontColor);
    }

    private void createLowerBar() {
        int labelWidth = 130;
        yearOrMonth = createBasicToggleButton();
        yearOrMonth.setLocation(getWidth() / 5, from.getY() + from.getHeight() + 25);
        swingFactory.createLabel("Jahre", new Dimension(labelWidth, 64), yearOrMonth.getX() - labelWidth - 25,
                yearOrMonth.getY(), null, new Font("Times New Roman", Font.BOLD, 40), mainFrame.fontColor);
        swingFactory.createLabel("Monate", new Dimension(labelWidth, 64), yearOrMonth.getX() + 25 + 64, yearOrMonth.getY(), null,
                new Font("Times New Roman", Font.BOLD, 40), mainFrame.fontColor);

        bookOrPage = createBasicToggleButton();
        bookOrPage.setLocation((int) (getWidth() / 5 * 2.5), from.getY() + from.getHeight() + 25);
        swingFactory.createLabel("Bücher", new Dimension(labelWidth, 64), bookOrPage.getX() - labelWidth - 25,
                yearOrMonth.getY(), null, new Font("Times New Roman", Font.BOLD, 40), mainFrame.fontColor);
        swingFactory.createLabel("Seiten", new Dimension(labelWidth, 64), bookOrPage.getX() + 25 + 64, yearOrMonth.getY(), null,
                new Font("Times New Roman", Font.BOLD, 40), mainFrame.fontColor);

        barOrLine = createBasicToggleButton();
        barOrLine.setLocation(getWidth() / 5 * 4, from.getY() + from.getHeight() + 25);
        swingFactory.createLabel("Balken", new Dimension(labelWidth, 64), barOrLine.getX() - labelWidth - 25,
                yearOrMonth.getY(), null, new Font("Times New Roman", Font.BOLD, 40), mainFrame.fontColor);
        swingFactory.createLabel("Linie", new Dimension(labelWidth, 64), barOrLine.getX() + 25 + 64, yearOrMonth.getY(), null,
                new Font("Times New Roman", Font.BOLD, 40), mainFrame.fontColor);
    }

    private JToggleButton createBasicToggleButton() {
        JToggleButton button = new JToggleButton();

        button.setIcon(new ImageIcon(MainFrame.read("switch rechts.png")));
        button.addActionListener(e -> {
            if (button.isSelected()) {
                button.setIcon(new ImageIcon(MainFrame.read("switch links.png")));
            } else {
                button.setIcon(new ImageIcon(MainFrame.read("switch rechts.png")));
            }
        });
        button.setSize(64, 64);
        button.setBackground(Color.WHITE);
        button.setOpaque(false);
        button.setBorder(null);
        add(button);

        return button;
    }

    private void createFromToGui() {
        JLabel fromLabel = swingFactory.createLabel("Von: ", new Dimension(150, 75), 25, 25, null,
                new Font("Times New Roman", Font.BOLD, 60), mainFrame.fontColor);
        String[] relevantYears = getRelevantReadingYears();
        from = new JComboBox<>(relevantYears);
        from.setSize(200, 75);
        from.setLocation(fromLabel.getX() + fromLabel.getWidth() + 25, 25);
        from.setUI(new WindowsComboBoxUI());
        from.setFont(new Font("Times New Roman", Font.BOLD, 60));

        JLabel toLabel = swingFactory.createLabel("Bis: ", new Dimension(150, 75), from.getX() + from.getWidth() + 50, 25, null,
                new Font("Times New Roman", Font.BOLD, 60), mainFrame.fontColor);
        to = new JComboBox<>(relevantYears);
        to.setSize(200, 75);
        to.setLocation(toLabel.getX() + toLabel.getWidth() + 25, 25);
        to.setUI(new WindowsComboBoxUI());
        to.setFont(new Font("Times New Roman", Font.BOLD, 60));
    }

    private String[] getRelevantReadingYears() {
        List<Integer> yearInts = mainFrame.getReadingYears();
        String[] array = new String[yearInts.size()];
        for (int i = 0; i < yearInts.size(); i++) {
            if (yearInts.get(i) == 0) {
                array[i] = "Früher";
            } else {
                array[i] = yearInts.get(i).toString();
            }
        }
        return array;
    }

    private void prepareFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);
        setSize(1920, 1080);
        getContentPane().setBackground(Color.WHITE);
        setExtendedState(MAXIMIZED_BOTH);
    }

}
