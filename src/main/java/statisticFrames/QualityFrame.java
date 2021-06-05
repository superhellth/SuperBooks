package statisticFrames;

import bookManagement.BookStatistics;
import bookManagement.Genre;
import bookManagement.Subgenre;
import com.sun.java.swing.plaf.windows.WindowsScrollBarUI;
import gui.swing.CusJFrame;
import gui.swing.SwingFactory;
import main.MainFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.plaf.basic.BasicPanelUI;
import java.awt.*;

public class QualityFrame extends CusJFrame {

    private MainFrame mainFrame;
    private int chartWidth;
    private int chartHeight;

    private JPanel graphPanel;
    private JScrollPane graphScroll = new JScrollPane();

    private SwingFactory swingFactory = new SwingFactory(this);

    QualityFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        //set basic gui
        setBasicGui();

        //extended gui
        chartWidth = getWidth() / 2 - 10;
        chartHeight = (int) Math.round(chartWidth / 1.778);
        getContentPane().setBackground(mainFrame.bgColor);

        swingFactory.createLabel("Das liest du:", new Dimension(800, 80), 25, 0, mainFrame.bgColor, new Font(mainFrame.myFont.getName(),
                mainFrame.myFont.getStyle(), 75), mainFrame.fontColor);

        init();

        setVisible(true);
    }

    private void setBasicGui() {
        setTitle("Das liest du");
        setSize(1920, 1080);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        swingFactory.setContainer(null);
        graphPanel = swingFactory.createPanel(new Dimension(1920, chartHeight * 2), 0, 0, mainFrame.bgColor, null);
        graphPanel.setLayout(new GridLayout(0, 2));
        graphScroll.setSize(getWidth(), chartHeight);
        graphScroll.setViewportView(graphPanel);
        graphScroll.setLocation(0, 90);
        graphScroll.getVerticalScrollBar().setUnitIncrement(chartHeight);
        graphScroll.getVerticalScrollBar().setUI(new WindowsScrollBarUI());
        add(graphScroll);

        createGenreDiagramm();
        createSubgenreDiagramm();
        createTextGUI();
    }

    private void createTextGUI() {
        BookStatistics bookStatistics = new BookStatistics(mainFrame);
        swingFactory.setContainer(this);
        JLabel header = swingFactory.createLabel("Du liest zu:", new Dimension(getWidth() / 2 - 10, 80), 25,
                graphScroll.getHeight() + graphScroll.getY() + 25, mainFrame.bgColor, new Font(mainFrame.myFont.getName(),
                        Font.BOLD, 75), mainFrame.fontColor);
        JLabel epic = swingFactory.createLabel("- " + bookStatistics.getPercentOfGenre(new Genre("Epik")) +
                        "% Epik", new Dimension(800, 60), 25, header.getY() + header.getHeight() + 10,
                mainFrame.bgColor, new Font(mainFrame.myFont.getName(), Font.ITALIC, 55), mainFrame.fontColor);
        JLabel drama = swingFactory.createLabel("- " + bookStatistics.getPercentOfGenre(new Genre("Drama")) +
                        "% Drama", new Dimension(800, 60), 25, epic.getY() + epic.getHeight(),
                mainFrame.bgColor, new Font(mainFrame.myFont.getName(), Font.ITALIC, 55), mainFrame.fontColor);
        JLabel lyric = swingFactory.createLabel("- " + bookStatistics.getPercentOfGenre(new Genre("Lyrik")) +
                        "% Lyrik", new Dimension(800, 60), 25, drama.getY() + drama.getHeight(),
                mainFrame.bgColor, new Font(mainFrame.myFont.getName(), Font.ITALIC, 55), mainFrame.fontColor);
        swingFactory.createLabel("- " + bookStatistics.getPercentOfGenre(new Genre("Sachliteratur")) + "% Sachliteratur",
                new Dimension(800, 60), 25, lyric.getY() + lyric.getHeight(), mainFrame.bgColor,
                new Font(mainFrame.myFont.getName(), Font.ITALIC, 55), mainFrame.fontColor);

        JLabel header2 = swingFactory.createLabel("Insgesamt liest du zu:", new Dimension(getWidth() / 2 - 10, 80),
                header.getWidth() + header.getX(), header.getY(), mainFrame.bgColor, new Font(mainFrame.myFont.getName(),
                        Font.BOLD, 75), mainFrame.fontColor);
        JLabel germanBooks = swingFactory.createLabel("- " + bookStatistics.getPercentOfLanguage("Deutsch") + "% deutsche Bücher",
                new Dimension(800, 60), header2.getX(), header2.getY() + header2.getHeight() + 10, mainFrame.bgColor,
                new Font(mainFrame.myFont.getName(), Font.ITALIC, 55), mainFrame.fontColor);
        JLabel pages = swingFactory.createLabel("- " + bookStatistics.getPercentOfBooksWithMorePagesThan(500) +  "% Bücher mit " +
                        "500+ Seiten",
                new Dimension(1000, 60), germanBooks.getX(), germanBooks.getY() + germanBooks.getHeight(), mainFrame.bgColor,
                new Font(mainFrame.myFont.getName(), Font.ITALIC, 55), mainFrame.fontColor);
        JLabel author = swingFactory.createLabel("<html>Dein meist gelesener Autor ist <br>  " + bookStatistics.getFavAuthor() +
                "</html>",
                new Dimension(800, 120), pages.getX(), pages.getY() + pages.getHeight() + 10, mainFrame.bgColor,
                new Font(mainFrame.myFont.getName(), Font.ITALIC, 55), mainFrame.primaryColor);
    }

    private void createGenreDiagramm() {
        DefaultPieDataset genreDataset = getGenreData();
        JFreeChart genreChart = ChartFactory.createPieChart("Aufteilung der Genres", genreDataset, false, true, false);
        ChartPanel genreChartPanel = new ChartPanel(genreChart);
        genreChartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        genreChartPanel.setBackground(mainFrame.secondaryColor);
        genreChartPanel.setSize(chartWidth, chartHeight);
        genreChartPanel.setLocation(0, chartHeight * 3);
        genreChartPanel.setUI(new BasicPanelUI());
        graphPanel.add(genreChartPanel);
    }

    private DefaultPieDataset getGenreData() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Genre genre : mainFrame.dbReader.getGenres()) {
            int numBooksOfGenre = mainFrame.bookFilter.getBooksOfGenre(genre).size();
            dataset.setValue(genre.getName(), numBooksOfGenre);
        }

        return dataset;
    }

    private void createSubgenreDiagramm() {
        DefaultPieDataset subgenreDataset = getSubgenreData();
        JFreeChart subgenreChart = ChartFactory.createPieChart("Aufteilung der Subgenres", subgenreDataset, false, true, false);
        ChartPanel subgenreChartPanel = new ChartPanel(subgenreChart);
        subgenreChartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        subgenreChartPanel.setBackground(mainFrame.secondaryColor);
        subgenreChartPanel.setSize(chartWidth, chartWidth);
        subgenreChartPanel.setLocation(0, chartHeight * 2);
        subgenreChartPanel.setUI(new BasicPanelUI());
        graphPanel.add(subgenreChartPanel);
    }

    private DefaultPieDataset getSubgenreData() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Subgenre subgenre : mainFrame.dbReader.getSubgenres()) {
            int numBooksOfSubgenre = mainFrame.bookFilter.getBooksOfSubgenre(subgenre).size();
            dataset.setValue(subgenre.getSubgenre() + "\n(" + subgenre.getGenre() + ")", numBooksOfSubgenre);
        }

        return dataset;
    }
}
