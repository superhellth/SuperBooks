package main;

import bookManagement.*;
import com.sun.java.swing.plaf.windows.WindowsScrollBarUI;
import database.BookDbReader;
import database.BookDbWriter;
import gui.BookPanel;
import gui.EditPanel;
import gui.swing.CusJFrame;
import gui.swing.SwingFactory;
import main.gui.BookTableV2;
import main.gui.SortBoxFactory;
import otherFrames.AddBookFrame;
import otherFrames.AddGenreFrame;
import otherFrames.SettingFrame;
import statisticFrames.MainStatFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.*;

public class MainFrame extends CusJFrame {

    public final static int BY_GENRE = 0;
    public final static int BY_NAME = 1;
    public final static int BY_AUTHOR_FIRST_NAME = 2;
    public final static int BY_AUTHOR_LAST_NAME = 3;
    public final static int BY_PAGES_ASCENDING = 5;
    public final static int BY_PAGES_DESCENDING = 6;
    public final static int BY_TENSION = 7;
    public final static int BY_STORY = 8;
    public final static int BY_FUN = 9;
    public final static int BY_INTEREST = 10;
    public final static int BY_TOTAL_RAT = 11;
    public final static int BY_READING_DATE = 12;
    public final static int BY_PUB_COMP = 13;
    public final static int BY_LANGUAGE_RAT = 14;


    public int sortedByFirst = BY_GENRE;
    public int sortedBySecond = BY_NAME;

    public ArrayList<Integer> pageSections;

    public JScrollPane scrollPane;

    public BookDbReader dbReader;
    public BookDbWriter dbWriter;

    public ArrayList<Book> allBooks;
    public Map<String, BookPanel> allBookPanels = new HashMap<>();
    public Map<String, EditPanel> allEditPanels = new HashMap<>();
    public BookFilter bookFilter;
    public BookSorter bookSorter;
    public ArrayList<Genre> genres;

    public int bigPanelHeight = 70;
    public int smallPanelHeight = 60;
    public int headerHeight;

    public int numBigLabelsAdded = 0;
    public int numSmallLabelsAdded = 0;
    public int numBooksAdded = 0;
    public int numExtraPanels = 0;

    private JComboBox<String> firstCrit;
    private JComboBox<String> secondCrit;
    private JButton addBook;
    private JButton addGenre;
    private JButton analyzeButton;
    private JLabel sortByLabel;
    private JButton settings;
    private JLabel header;

    public Map<String, Boolean> isHidden = new HashMap<>();
    public Map<String, Boolean> editIsOpen = new HashMap<>();

    public Font myFont = new Font("AR ESSENCE", Font.BOLD, 35);
    public String coverPath;
    private String CONNECTION;
    private Connection connection;
    public Color bgColor = new Color(255, 255, 255);
    public Color fontColor = new Color(129, 1, 0);
    public Color primaryColor;
    public Color secondaryColor;
    public boolean showRat;
    public boolean showPages;
    public boolean resetScroll;

    public boolean skipInit;
    public int currentID = 0;

    public MainFrame(String CONNECTION) {
        this.CONNECTION = CONNECTION;
        createDbConnection();
        getInfoFromDb();
        allBooks = new ArrayList<>(dbReader.getBooks());
        genres = new ArrayList<>(dbReader.getGenres());

        JWindow startingWindow = new JWindow();
        createStartingWindow(startingWindow);

        bookFilter = new BookFilter(this, allBooks);
        bookSorter = new BookSorter();
        createAllBookPanels();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        designFrame(screenSize);

        createPageSections();

        designUpperGui();

        SortBoxFactory guiFactory = new SortBoxFactory(this);
        firstCrit = guiFactory.createFirstCrit();
        secondCrit = guiFactory.createSecondCrit();
        add(firstCrit);
        add(secondCrit);

        addOnCloseListener();

        firstCrit.setSelectedItem(dbReader.getInfo("erstesKriterium"));
        secondCrit.setSelectedItem(dbReader.getInfo("zweitesKriterium"));


        startingWindow.dispose();
        setVisible(true);
        init();
        repaint();
    }

    public void init() {
        if (skipInit) {
            return;
        }

        //reset scroll pane
        if (scrollPane != null) {
            remove(scrollPane);
        }

        //check for scroll value
        int scroll = 0;
        if (scrollPane != null) {
            scroll = scrollPane.getVerticalScrollBar().getValue();
        }

        //reset init stats
        numBigLabelsAdded = 0;
        numSmallLabelsAdded = 0;
        numBooksAdded = 0;
        numExtraPanels = 0;

        //recreate components
        JPanel bookPanel = createBookPanel();
        scrollPane = createScrollPane(bookPanel);
        createTable(bookPanel);
        add(scrollPane);

        //refresh scroll bar
        scrollPane.getVerticalScrollBar().setValue(1);
        scrollPane.getVerticalScrollBar().setValue(0);

        //scroll to earlier position
        if (!resetScroll) {
            scrollPane.getVerticalScrollBar().setValue(scroll);
        }
    }

    private void createStartingWindow(JWindow startingWindow) {
        startingWindow.setSize(700, 450);
        startingWindow.setLocation(getScreenWidth() / 2 - startingWindow.getWidth() / 2, getScreenHeight() / 2 - startingWindow.getHeight() / 2);
        startingWindow.setLayout(null);

        BufferedImage coverImage = new BufferedImage(700, 450, 2);
        Graphics2D g = coverImage.createGraphics();
        BufferedImage originalImage = null;

        try {
            originalImage = ImageIO.read(read("loadingBackground.jpg"));
        } catch (Exception e) {

        }
        g.drawImage(originalImage, 0, 0, 700, 450, null);
        g.dispose();

        JLabel background = new JLabel(new ImageIcon(coverImage), SwingConstants.CENTER);
        background.setSize(startingWindow.getWidth(), startingWindow.getHeight());

        JLabel loading = new JLabel("<html>Lädt Bücher aus<br>der Datenbank...<html>", SwingConstants.CENTER);
        loading.setFont(new Font("Times New Roman", Font.BOLD, 60));
        loading.setSize(500, 200);
        loading.setForeground(Color.BLACK);
        background.setLocation(0, 0);
        loading.setLocation(100, 25);

        ImageIcon animation = new ImageIcon(read("loadingAnimation.gif"), "");

        JLabel loadingAnimationLabel = new JLabel("Datenbank lesen... ", animation, JLabel.CENTER);
        loadingAnimationLabel.setSize(600, 400);
        loadingAnimationLabel.setLocation(50, 150);
        loadingAnimationLabel.setFont(new Font("Times New Roman", Font.BOLD, 50));
        loadingAnimationLabel.setForeground(Color.BLACK);
        startingWindow.add(loadingAnimationLabel);
        startingWindow.add(loading);
        startingWindow.add(background);

        startingWindow.repaint();
        startingWindow.setVisible(true);
    }

    private void createAllBookPanels() {
        allBookPanels.clear();
        allEditPanels.clear();
        for (Book book : allBooks) {
            book.setKey("book_" + getID());
            allBookPanels.put(book.getKey(), new BookPanel(this, book));
            //allEditPanels.put(book.getKey(), new EditPanel(this, book));
        }
    }

    public void updateABookPanel(Book oldBook, Book newBook) {
        String key = oldBook.getKey();
        allBooks.remove(oldBook);
        allBooks.add(newBook);

        allBookPanels.remove(key);
        allBookPanels.put(key, new BookPanel(this, newBook));
        allEditPanels.remove(key);
        allEditPanels.put(key, new EditPanel(this, newBook));
    }

    public void addABookPanel(Book book) {
        allBooks.add(book);
        book.setKey("book_" + getID());
        allBookPanels.put(book.getKey(), new BookPanel(this, book));
        allEditPanels.put(book.getKey(), new EditPanel(this, book));
    }

    public void deleteABookPanel(Book book) {
        allBooks.remove(book);
        allBookPanels.remove(book.getKey());
        allEditPanels.remove(book.getKey());
    }

    private void createDbConnection() {
        try {
            connection = DriverManager.getConnection(CONNECTION);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbReader = new BookDbReader(connection);
        dbWriter = new BookDbWriter(connection);
    }

    private void designUpperGui() {
        SwingFactory swingFactory = new SwingFactory(MainFrame.this);

        headerHeight = 1080 / 10;
        header = swingFactory.createLabel("Nicos Bücherdatenbank", new Dimension((int) ((1920 - 300) / 1920d * getWidth()),
                (int) (headerHeight / 1080d * getHeight())), 0, 0, bgColor, new Font(myFont.getName(), myFont.getStyle(), 110), fontColor);
        header.setBorder(new LineBorder(secondaryColor, 10));

        settings = swingFactory.createButton("Einstellungen", new Dimension((int) (300 / 1920d * getWidth()),
                        (int) (headerHeight / 1080d * getHeight())),
                (int) ((1920 - 300) / 1920d * getWidth()), 0, bgColor, new Font(myFont.getName(), myFont.getStyle(), 40),
                fontColor, null, e -> new SettingFrame(this));
        settings.setBorder(new LineBorder(primaryColor, 10, false));

        addBook = swingFactory.createButton("Buch hinzufügen", new Dimension((int) (200 / 1920d * getWidth()),
                        (int) (80 / 1080d * getHeight())), (int) (25 / 1920d * getWidth()), (int) ((headerHeight + 10) / 1080d * getHeight()),
                bgColor, new Font(myFont.getName(), myFont.getStyle(), 20), fontColor,
                new LineBorder(secondaryColor, 8, true), e -> new AddBookFrame(MainFrame.this));
        addGenre = swingFactory.createButton("Genre hinzufügen", new Dimension((int) (200 / 1920d * getWidth()), (int) (80 / 1080d * getHeight())),
                (int) ((50 + 200) / 1920d * getWidth()), (int) ((headerHeight + 10) / 1080d * getHeight()), bgColor,
                new Font(myFont.getName(), myFont.getStyle(), 20), fontColor, new LineBorder(secondaryColor, 8, true),
                e -> new AddGenreFrame(MainFrame.this));

        analyzeButton = swingFactory.createButton("Statistik", new Dimension((int) (200 / 1920d * getWidth()), (int) (80 / 1080d * getHeight())),
                (int) ((1920 - 200 - 25) / 1920d * getWidth()), (int) ((headerHeight + 10) / 1080d * getHeight()), bgColor, new Font(myFont.getName(),
                        myFont.getStyle(), 25), fontColor, new LineBorder(secondaryColor, 8, true),
                e -> new MainStatFrame(MainFrame.this));

        sortByLabel = swingFactory.createLabel("Sortiert nach:", new Dimension((int) (300 / 1920d * getWidth()), (int) (90 / 1080d * getHeight())),
                (int) ((525) / 1920d * getWidth()), (int) ((headerHeight + 5) / 1080d * getHeight()), bgColor,
                new Font(myFont.getName(), myFont.getStyle(), 45), fontColor);
    }

    private void getInfoFromDb() {
        showRat = Boolean.parseBoolean(dbReader.getInfo("BewertungAnzeigen"));
        showPages = Boolean.parseBoolean(dbReader.getInfo("SeitenAnzeigen"));
        resetScroll = Boolean.parseBoolean(dbReader.getInfo("ScrollZurücksetzen"));
        myFont = new Font(dbReader.getInfo("font"), myFont.getStyle(), myFont.getSize());
        StringTokenizer tok = new StringTokenizer(dbReader.getInfo("ersteFarbe"), ", ");
        primaryColor = new Color(Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()),
                Integer.parseInt(tok.nextToken()));
        tok = new StringTokenizer(dbReader.getInfo("zweiteFarbe"), ", ");
        secondaryColor = new Color(Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()),
                Integer.parseInt(tok.nextToken()));
        tok = new StringTokenizer(dbReader.getInfo("fontFarbe"), ", ");
        fontColor = new Color(Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()),
                Integer.parseInt(tok.nextToken()));
        coverPath = dbReader.getInfo("coverPfad");
    }

    private void designFrame(Dimension screenSize) {
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());     //Mein Computer: 1920 * 1080
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setIconImage(new ImageIcon(read("book.png")).getImage());
        setTitle("Bücherdatenbank");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(bgColor);
        setForeground(fontColor);
    }

    private void createPageSections() {
        pageSections = new ArrayList<>();
        pageSections.add(0);
        pageSections.add(100);
        pageSections.add(200);
        pageSections.add(500);
        pageSections.add(1000);
        pageSections.add(Integer.MAX_VALUE);
    }

    private void addOnCloseListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dbWriter.updateInfo("erstesKriterium",
                        Objects.requireNonNull(firstCrit.getSelectedItem()).toString());
                String secondCritValue = null;
                if (secondCrit.getSelectedItem() != null) {
                    secondCritValue = secondCrit.getSelectedItem().toString();
                }
                dbWriter.updateInfo("zweitesKriterium", secondCritValue);
                dbWriter.updateInfo("BewertungAnzeigen", Boolean.toString(showRat));
                dbWriter.updateInfo("SeitenAnzeigen", Boolean.toString(showPages));
                dbWriter.updateInfo("ScrollZurücksetzen", Boolean.toString(resetScroll));
                dbWriter.updateInfo("font", myFont.getName());
                dbWriter.updateInfo("ersteFarbe", primaryColor.getRed() + ", " + primaryColor.getGreen() +
                        ", " + primaryColor.getBlue());
                dbWriter.updateInfo("zweiteFarbe", secondaryColor.getRed() + ", " + secondaryColor.getGreen() +
                        ", " + secondaryColor.getBlue());
                dbWriter.updateInfo("fontFarbe", fontColor.getRed() + ", " + fontColor.getGreen() +
                        ", " + fontColor.getBlue());
                dbWriter.updateInfo("coverPfad", coverPath);

                dbReader.disconnect();
                dbWriter.disconnect();
            }
        });
    }

    public void updateSettings() {
        ArrayList<Component> toUpdate = new ArrayList<>();
        toUpdate.add(firstCrit);
        firstCrit.setBackground(secondaryColor);
        toUpdate.add(secondCrit);
        secondCrit.setBackground(secondaryColor);
        toUpdate.add(addGenre);
        addGenre.setBorder(new LineBorder(secondaryColor, 8, true));
        toUpdate.add(addBook);
        addBook.setBorder(new LineBorder(secondaryColor, 8, true));
        toUpdate.add(analyzeButton);
        analyzeButton.setBorder(new LineBorder(secondaryColor, 8, true));
        toUpdate.add(sortByLabel);
        toUpdate.add(header);
        header.setBorder(new LineBorder(secondaryColor, 10));
        toUpdate.add(settings);
        settings.setBorder(new LineBorder(primaryColor, 10, false));
        for (Component component : toUpdate) {
            component.setFont(new Font(myFont.getName(), component.getFont().getStyle(), component.getFont().getSize()));
            component.setForeground(fontColor);
        }
        createAllBookPanels();
    }

    private void createTable(JPanel bigPanel) {
        BookTableV2 bookTable = new BookTableV2(this, bigPanel);
        bookFilter.setBooksToFilter(allBooks);
        switch (sortedByFirst) {
            case BY_GENRE:
                bookTable.createTableByGenre();
                break;
            case BY_AUTHOR_FIRST_NAME:
                bookTable.createTableByAuthorFirst();
                break;
            case BY_AUTHOR_LAST_NAME:
                bookTable.createTabelByAuthorLast();
                break;
            case BY_NAME:
                bookTable.createTableByName();
                break;
            case BY_PAGES_ASCENDING:
                bookTable.createTableByPagesAs();
                break;
            case BY_PAGES_DESCENDING:
                bookTable.createTableByPagesDes();
                break;
            case BY_READING_DATE:
                bookTable.createTabelByReadingDate();
                break;
            case BY_TOTAL_RAT:
                bookTable.createTableByTotalRat();
                break;
            case BY_TENSION:
                bookTable.createTableByRat(BY_TENSION);
                break;
            case BY_FUN:
                bookTable.createTableByRat(BY_FUN);
                break;
            case BY_STORY:
                bookTable.createTableByRat(BY_STORY);
                break;
            case BY_INTEREST:
                bookTable.createTableByRat(BY_INTEREST);
                break;
            case BY_LANGUAGE_RAT:
                bookTable.createTableByRat(BY_LANGUAGE_RAT);
                break;
            case BY_PUB_COMP:
                bookTable.createTableByPubComp();
                break;
        }

    }

    public boolean isHidden(String text) {
        Boolean oldValue = isHidden.get(text);
        return Boolean.TRUE.equals(oldValue);
    }

    public boolean editIsOpen(String text) {
        Boolean oldValue = editIsOpen.get(text);
        return Boolean.TRUE.equals(oldValue);
    }

    public boolean hasBooks(Subgenre subgenre) {
        for (Book book : allBooks) {
            if (book.getSubgenre().getGenre().equalsIgnoreCase(subgenre.getGenre()) &&
                    book.getSubgenre().getSubgenre().equalsIgnoreCase(subgenre.getSubgenre())) {
                return true;
            }
        }

        return false;
    }

    public static URL read(String fileName) {
        return MainFrame.class.getClassLoader().getResource(fileName);
    }

    public ArrayList<String> getGenreStrings() {
        Set<String> genreSet = new TreeSet<>();

        for (Genre genre : genres) {
            genreSet.add(genre.getName());
        }

        return new ArrayList<>(genreSet);
    }

    public Set<String> getStartingLetters() {
        Set<String> startingLetters = new TreeSet<>();

        for (Book book : allBooks) {
            startingLetters.add(book.getName().substring(0, 1).toUpperCase());
        }

        return startingLetters;
    }

    public List<Genre> getGenresWBooks() {
        Set<String> stringSet = new TreeSet<>();
        List<Genre> genreList = new ArrayList<>();

        for (Book book : allBooks) {
            stringSet.add(book.getSubgenre().getGenre());
        }
        for (String s : stringSet) {
            genreList.add(new Genre(s));
        }

        return genreList;
    }

    public Set<Integer> getRatsWBooks(int criterium) {
        Set<Integer> ratSet = new TreeSet<>();

        for (Book book : allBooks) {
            switch (criterium) {
                case BY_TENSION:
                    ratSet.add(book.getTensionRat());
                    break;
                case BY_FUN:
                    ratSet.add(book.getFunRat());
                    break;
                case BY_STORY:
                    ratSet.add(book.getStoryRat());
                    break;
                case BY_INTEREST:
                    ratSet.add(book.getInterestRat());
                    break;
                case BY_LANGUAGE_RAT:
                    ratSet.add(book.getLanguageRat());
                    break;
            }
        }

        return ratSet;
    }

    public Map<Integer, Integer> getUsedSecs() {
        Map<Integer, Integer> indexABounds = new HashMap<>();

        for (Book book : allBooks) {
            int totalRat = book.getTotalRat();
            int upperBound = getUpperBound(totalRat);
            int index = totalRat / 10;
            indexABounds.put(index, upperBound);
        }

        return indexABounds;
    }

    private int getUpperBound(int totalRat) {
        int rest = 10 - (totalRat % 10);
        if (totalRat == 100) {
            rest = 0;
        }
        if (totalRat + rest == 100) {
            return 101;
        }

        return totalRat + rest;
    }

    public List<Integer> getReadingYears() {
        Set<Integer> readingYears = new TreeSet<>();

        for (Book book : allBooks) {
            readingYears.add(book.getReadingYear());
        }
        List<Integer> integerList = new ArrayList<>(readingYears);
        Collections.reverse(integerList);

        return integerList;
    }

    public Set<Integer> getReadingMonths(int year) {
        Set<Integer> readingMonths = new TreeSet<>();

        for (Book book : allBooks) {
            if (book.getReadingYear() == year) {
                readingMonths.add(book.getReadingMonth());
            }
        }
        List<Integer> integerList = new ArrayList<>(readingMonths);
        Collections.reverse(integerList);

        return readingMonths;
    }

    private JPanel createBookPanel() {
        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.Y_AXIS));

        return bookPanel;
    }

    private JScrollPane createScrollPane(JPanel panel) {
        JScrollPane bookScrollPane = new JScrollPane();
        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int taskBarHeight = scrnSize.height - winSize.height;
        bookScrollPane.setSize(new Dimension((int) ((1920 - 15) / 1920d * getWidth()), (int) ((1080 - (headerHeight + 105 + taskBarHeight)) / 1080d * getHeight())));
        bookScrollPane.setLocation(0, (int) ((headerHeight + 105) / 1080d * getHeight()));
        bookScrollPane.setViewportView(panel);
        bookScrollPane.getVerticalScrollBar().setUnitIncrement(15);
        bookScrollPane.setHorizontalScrollBar(null);
        bookScrollPane.getVerticalScrollBar().setUI(new WindowsScrollBarUI());
        return bookScrollPane;
    }

    public static ArrayList<String> getYears() {
        ArrayList<String> years = new ArrayList<>();
        for (int i = 2010; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(Integer.toString(i));
        }
        years.add("Unbekannt");

        return years;
    }

    private int getID() {
        return currentID++;
    }
}
