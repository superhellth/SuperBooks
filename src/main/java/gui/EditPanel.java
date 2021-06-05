package gui;

import bookManagement.Book;
import bookManagement.Genre;
import bookManagement.Subgenre;
import calendar.MyCalendar;
import gui.swing.CusJPanel;
import gui.swing.SwingFactory;
import main.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class EditPanel extends CusJPanel {

    private SwingFactory swingFactory;
    private boolean editAuthorIsOpen = false;
    private JButton editSecondAuthor;
    private JLabel nameLabel;
    private JTextField nameField;
    private JButton editName;
    private JLabel secondAuthorLabel;
    private JTextField secondAuthorField;
    private JLabel authorLabel;
    private JTextField authorField;
    private JButton editAuthor;
    private JLabel pubCompLabel;
    private JTextField pubCompField;
    private JButton editPubComp;
    private JLabel languageLabel;
    private JComboBox<String> languageBox;
    private JLabel headGenreLabel;
    private JComboBox<String> hGenreBox;
    private JLabel subgenreLabel;
    private JComboBox<String> subgenreBox;
    private JLabel pageLabel;
    private JTextField pageField;
    private JButton editPage;
    private JLabel rateLabel;
    private JLabel tenLabel;
    private JComboBox<String> tenBox;
    private JLabel moreLabel;
    private JComboBox<String> moreBox;
    private JLabel storyLabel;
    private JComboBox<String> storyBox;
    private JLabel funLabel;
    private JComboBox<String> funBox;
    private JLabel languageRatLabel;
    private JComboBox<String> languageRatBox;
    private JLabel readingDateLabel;
    private JComboBox<String> monthBox;
    private JLabel splitDate;
    private JComboBox<String> yearBox;
    private JButton doneEditing;
    private String fontName;
    private int fontstyle;
    private int buffer;
    private MainFrame mainFrame;
    private Book book;
    private JButton isRecommended;
    private ImageIcon star1 = createImage("star.png");
    private ImageIcon star2 = createImage("star2.png");

    public EditPanel(MainFrame mainFrame, Book book) {
        swingFactory = new SwingFactory(this);
        fontName = mainFrame.myFont.getName();
        fontstyle = mainFrame.myFont.getStyle();
        this.mainFrame = mainFrame;
        this.book = book;

        //set basic gui
        setBasicGui(mainFrame);

        //fill arrayLists
        ArrayList<String> genreStrings = new ArrayList<>();
        for (Genre genre : mainFrame.genres) {
            genreStrings.add(genre.getName());
        }
        ArrayList<String> oneToTen = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            oneToTen.add(Integer.toString(i));
        }
        oneToTen.add("Irrelevant");

        buffer = 0;
        if (book.getSecondAuthor() != null) {
            buffer = 80;
        }

        createTitleEdit(mainFrame, book);

        createAuthorEdit(mainFrame, book);

        createPubCompEdit(mainFrame, book);

        createPageEdit(mainFrame, book);

        createLanguageEdit(mainFrame, book);

        createGenreEdit(mainFrame, book, genreStrings);

        createSubgenreEdit(mainFrame, book);

        createRatingEdit(mainFrame, book, oneToTen);

        createDateEdit(mainFrame, book);

        if (secondAuthorField != null) {
            repositionComponents();
        }

        //create 'done' button
        doneEditing = swingFactory.createButton("Fertig", new Dimension(309, 60), 1559, 427, mainFrame.secondaryColor,
                new Font(fontName, fontstyle, 50), mainFrame.fontColor, null, e -> {
                    String month = Integer.toString(monthBox.getSelectedIndex());
                    if (Integer.parseInt(month) == 0) {
                        month = "X";
                    }
                    String year = yearBox.getSelectedItem().toString();
                    if (yearBox.getSelectedItem().toString().equalsIgnoreCase("Unbekannt")) {
                        year = "X";
                    }
                    String readingDate = month + "." + year;

                    Book newBook = new Book();
                    newBook.setName(nameField.getText());
                    newBook.setAuthor(authorField.getText());
                    if (secondAuthorField != null) {
                        if (!secondAuthorField.getText().equalsIgnoreCase("")) {
                            newBook.setSecondAuthor(secondAuthorField.getText());
                        }
                    }
                    newBook.setNumPages(Integer.parseInt(pageField.getText()));
                    newBook.setReadingDate(readingDate);
                    newBook.setPubComp(pubCompField.getText());
                    newBook.setLanguage(languageBox.getSelectedItem().toString());
                    if (tenBox.getSelectedItem().toString().equalsIgnoreCase("irrelevant")) {
                        newBook.setTensionRat(11);
                    } else {
                        newBook.setTensionRat(Integer.parseInt(tenBox.getSelectedItem().toString()));
                    }
                    if (moreBox.getSelectedItem().toString().equalsIgnoreCase("irrelevant")) {
                        newBook.setInterestRat(11);
                    } else {
                        newBook.setInterestRat(Integer.parseInt(moreBox.getSelectedItem().toString()));
                    }
                    if (storyBox.getSelectedItem().toString().equalsIgnoreCase("irrelevant")) {
                        newBook.setStoryRat(11);
                    } else {
                        newBook.setStoryRat(Integer.parseInt(storyBox.getSelectedItem().toString()));
                    }
                    if (funBox.getSelectedItem().toString().equalsIgnoreCase("irrelevant")) {
                        newBook.setFunRat(11);
                    } else {
                        newBook.setFunRat(Integer.parseInt(funBox.getSelectedItem().toString()));
                    }
                    if (languageRatBox.getSelectedItem().toString().equalsIgnoreCase("irrelevant")) {
                        newBook.setLanguageRat(11);
                    } else {
                        newBook.setLanguageRat(Integer.parseInt(languageRatBox.getSelectedItem().toString()));
                    }
                    newBook.setSubgenre(new Subgenre(hGenreBox.getSelectedItem().toString(), subgenreBox.getSelectedItem().toString()));
                    newBook.setKey(book.getKey());
                    newBook.setRecommended(book.isRecommended());

                    mainFrame.dbWriter.updateBook(book, newBook);
                    mainFrame.editIsOpen.put(book.getKey(), !mainFrame.editIsOpen(book.getKey()));
                    mainFrame.updateABookPanel(book, newBook);

                    mainFrame.init();
                });

        isRecommended = swingFactory.createButton("", new Dimension(doneEditing.getHeight(), doneEditing.getHeight()),
                doneEditing.getX() - doneEditing.getHeight() - 25, doneEditing.getY(), null, null, null, null, e -> {
                    book.setRecommended(!book.isRecommended());
                    if (book.isRecommended()) {
                        isRecommended.setIcon(star2);
                    } else {
                        isRecommended.setIcon(star1);
                    }
                });
        isRecommended.setOpaque(false);
        if (book.isRecommended()) {
            isRecommended.setIcon(star2);
        } else {
            isRecommended.setIcon(star1);
        }

        add(nameLabel, nameField, editName, authorLabel, authorField, editAuthor, pageLabel, pageField, editPage, headGenreLabel,
                hGenreBox, pubCompLabel, pubCompField, editPubComp, subgenreLabel, subgenreBox, rateLabel, tenLabel, tenBox, moreLabel,
                moreBox, storyLabel, storyBox, funLabel, funBox, languageLabel, languageBox, readingDateLabel, monthBox, splitDate,
                yearBox, doneEditing);
    }

    private void createDateEdit(MainFrame mainFrame, Book book) {
        readingDateLabel = swingFactory.createLabel("Lesedatum:", new Dimension(410, 55), 25, 375, mainFrame.bgColor,
                new Font(fontName, fontstyle, 60), mainFrame.fontColor);
        monthBox = swingFactory.createComboBox(MyCalendar.getMonthStrings(), new Dimension(300, 60), 475, 377,
                mainFrame.primaryColor, new Font(fontName, fontstyle, 40), mainFrame.fontColor);
        if (book.getReadingMonth() == 0) {
            monthBox.setSelectedItem("Unbekannt");
        } else {
            monthBox.setSelectedIndex(book.getReadingMonth());
        }
        splitDate = swingFactory.createLabel(":", new Dimension(20, 55), monthBox.getX() + monthBox.getWidth() + 65, 375,
                mainFrame.bgColor, new Font(fontName, fontstyle, 60), mainFrame.fontColor);
        yearBox = swingFactory.createComboBox(MainFrame.getYears(), new Dimension(300, 60), 875, 377,
                mainFrame.primaryColor, new Font(fontName, fontstyle, 40), mainFrame.fontColor);
        if (book.getReadingYear() == 0) {
            yearBox.setSelectedItem("Unbekannt");
        } else {
            yearBox.setSelectedItem(Integer.toString(book.getReadingYear()));
        }
    }

    private void createRatingEdit(MainFrame mainFrame, Book book, ArrayList<String> oneToTen) {
        rateLabel = swingFactory.createLabel("Bewertungen:", new Dimension(325, 70), 25, 280, mainFrame.bgColor,
                new Font(fontName, fontstyle, 55), mainFrame.fontColor);
        tenLabel = swingFactory.createLabel("Spannung:", new Dimension(150, 40), authorField.getX(), 252, mainFrame.bgColor,
                new Font(fontName, fontstyle, 30), mainFrame.fontColor);
        tenBox = swingFactory.createComboBox(oneToTen, new Dimension(150, 60), authorField.getX(), 292,
                mainFrame.primaryColor, new Font(fontName, fontstyle, 30), mainFrame.fontColor);
        tenBox.setSelectedIndex(book.getTensionRat() - 1);
        moreLabel = swingFactory.createLabel("Mehrwert:", new Dimension(150, 40), tenBox.getX() + tenBox.getWidth() + 25, 252, mainFrame.bgColor,
                new Font(fontName, fontstyle, 30), mainFrame.fontColor);
        moreBox = swingFactory.createComboBox(oneToTen, new Dimension(150, 60), tenBox.getX() + tenBox.getWidth() + 25, 292,
                mainFrame.primaryColor, new Font(fontName, fontstyle, 30), mainFrame.fontColor);
        moreBox.setSelectedIndex(book.getInterestRat() - 1);
        storyLabel = swingFactory.createLabel("Geschichte:", new Dimension(150, 40), moreBox.getX() + moreBox.getWidth() + 25, 252, mainFrame.bgColor,
                new Font(fontName, fontstyle, 30), mainFrame.fontColor);
        storyBox = swingFactory.createComboBox(oneToTen, new Dimension(150, 60), moreBox.getX() + moreBox.getWidth() + 25, 292,
                mainFrame.primaryColor, new Font(fontName, fontstyle, 30), mainFrame.fontColor);
        storyBox.setSelectedIndex(book.getStoryRat() - 1);
        funLabel = swingFactory.createLabel("Lesespaß:", new Dimension(150, 40), storyBox.getX() + storyBox.getWidth() + 25, 252, mainFrame.bgColor,
                new Font(fontName, fontstyle, 30), mainFrame.fontColor);
        funBox = swingFactory.createComboBox(oneToTen, new Dimension(150, 60), storyBox.getX() + storyBox.getWidth() + 25, 292,
                mainFrame.primaryColor, new Font(fontName, fontstyle, 30), mainFrame.fontColor);
        funBox.setSelectedIndex(book.getFunRat() - 1);
        languageRatLabel = swingFactory.createLabel("Sprache:", new Dimension(150, 40), funBox.getX() + funBox.getWidth() + 25, 252, mainFrame.bgColor,
                new Font(fontName, fontstyle, 30), mainFrame.fontColor);
        languageRatBox = swingFactory.createComboBox(oneToTen, new Dimension(150, 60), funBox.getX() + funBox.getWidth() + 25, 292,
                mainFrame.primaryColor, new Font(fontName, fontstyle, 30), mainFrame.fontColor);
        languageRatBox.setSelectedIndex(book.getLanguageRat() - 1);
    }

    private void createSubgenreEdit(MainFrame mainFrame, Book book) {
        subgenreLabel = swingFactory.createLabel("Subgenre:", new Dimension(300, 50),
                hGenreBox.getX() + hGenreBox.getWidth() + 100, headGenreLabel.getY(), mainFrame.bgColor, new Font(fontName, fontstyle, 60),
                mainFrame.fontColor);
        subgenreBox = swingFactory.createComboBox(mainFrame.bookFilter.getSubgenresOfAGenre(
                new Genre(hGenreBox.getSelectedItem().toString())), new Dimension(225, 60),
                subgenreLabel.getX() + subgenreLabel.getWidth() + 25, headGenreLabel.getY(), mainFrame.primaryColor,
                new Font(fontName, fontstyle, 40), mainFrame.fontColor);
        subgenreBox.setSelectedItem(book.getSubgenre().getSubgenre());
        hGenreBox.addActionListener(e -> {
            subgenreBox.removeAllItems();
            for (String subgenre : mainFrame.bookFilter.getSubgenresOfAGenre(new Genre(hGenreBox.getSelectedItem().toString()))) {
                subgenreBox.addItem(subgenre);
            }
        });
    }

    private void createGenreEdit(MainFrame mainFrame, Book book, ArrayList<String> genreStrings) {
        headGenreLabel = swingFactory.createLabel("Genre:", new Dimension(250, 50),
                25, authorLabel.getY() + authorLabel.getHeight() + 25, mainFrame.bgColor, new Font(fontName, fontstyle, 60),
                mainFrame.fontColor);
        hGenreBox = swingFactory.createComboBox(genreStrings, new Dimension(200, 60),
                authorField.getX(), headGenreLabel.getY(), mainFrame.primaryColor, new Font(fontName, fontstyle, 40),
                mainFrame.fontColor);
        hGenreBox.setSelectedItem(book.getSubgenre().getGenre());
    }

    private void createLanguageEdit(MainFrame mainFrame, Book book) {
        languageLabel = swingFactory.createLabel("Sprache:", new Dimension(175, 70), editAuthor.getX() + editAuthor.getWidth() + 50,
                pageField.getY() + pageField.getHeight() + 10, mainFrame.bgColor, new Font(fontName, fontstyle, 50), mainFrame.fontColor);
        languageBox = swingFactory.createComboBox(mainFrame.dbReader.getLanguages(), new Dimension(375, 70),
                pubCompField.getX(), languageLabel.getY(), mainFrame.primaryColor, new Font(fontName, fontstyle, 40), mainFrame.fontColor);
        languageBox.setSelectedItem(book.getLanguage());
    }

    private void createPageEdit(MainFrame mainFrame, Book book) {
        pageLabel = swingFactory.createLabel("Seiten:", new Dimension(175, 70), editAuthor.getX() + editAuthor.getWidth() + 50,
                authorField.getY() + authorField.getHeight() + 25, mainFrame.bgColor, new Font(fontName, fontstyle, 50), mainFrame.fontColor);
        pageField = swingFactory.createTextField(Integer.toString(book.getNumPages()), new Dimension(150, 70), pubCompField.getX(), pageLabel.getY(),
                mainFrame.primaryColor, new Font(fontName, fontstyle, 60), mainFrame.fontColor);
        pageField.setEnabled(false);
        editPage = swingFactory.createButtonWithIcon("edit.png", new Dimension(64, 64),
                pageField.getX() + pageField.getWidth() + 10, pageField.getY(), mainFrame.primaryColor, null,
                e -> pageField.setEnabled(!pageField.isEnabled()));
    }

    private void createPubCompEdit(MainFrame mainFrame, Book book) {
        pubCompLabel = swingFactory.createLabel("Verlag:", new Dimension(175, 70), editAuthor.getX() + editAuthor.getWidth() + 35, authorLabel.getY(), mainFrame.bgColor,
                new Font(fontName, fontstyle, 60), mainFrame.fontColor);
        pubCompField = swingFactory.createTextField(book.getPubComp(), new Dimension(300, 70),
                pubCompLabel.getX() + pubCompLabel.getWidth() + 25, authorField.getY(), mainFrame.primaryColor,
                new Font(fontName, fontstyle, 50), mainFrame.fontColor);
        pubCompField.setEnabled(false);
        editPubComp = swingFactory.createButtonWithIcon("edit.png", new Dimension(64, 64),
                pubCompField.getX() + pubCompField.getWidth() + 10, editAuthor.getY(), mainFrame.bgColor, null,
                e -> pubCompField.setEnabled(!pubCompField.isEnabled()));
    }

    private void createAuthorEdit(MainFrame mainFrame, Book book) {
        authorLabel = swingFactory.createLabel("Autor:", new Dimension(300, 70), 25, 90, mainFrame.bgColor,
                new Font(fontName, fontstyle, 60), mainFrame.fontColor);
        if (book.getSecondAuthor() != null) {
            authorLabel.setText("1. Autor");
        }
        authorField = swingFactory.createTextField(book.getAuthor(), new Dimension(850, 70), 350, 90, mainFrame.primaryColor,
                new Font(fontName, fontstyle, 60), mainFrame.fontColor);
        authorField.setEnabled(false);
        editAuthor = swingFactory.createButtonWithIcon("edit.png", new Dimension(64, 64),
                authorField.getX() + authorField.getWidth() + 10, 93, mainFrame.bgColor, null,
                e -> activateEditAuthor());

        if (book.getSecondAuthor() != null) {
            createSecondAuthorGui();
        }
    }

    private void createTitleEdit(MainFrame mainFrame, Book book) {
        nameLabel = swingFactory.createLabel("Buchname:", new Dimension(300, 70), 25, 10, mainFrame.bgColor,
                new Font(fontName, fontstyle, 60), mainFrame.fontColor);
        nameField = swingFactory.createTextField(book.getName(), new Dimension(1458, 70), 350, 10, mainFrame.primaryColor,
                new Font(fontName, fontstyle, 60), mainFrame.fontColor);
        nameField.setEnabled(false);
        editName = swingFactory.createButtonWithIcon("edit.png", new Dimension(64, 64),
                nameField.getX() + nameField.getWidth() + 10, 13, mainFrame.bgColor, null,
                e -> nameField.setEnabled(!nameField.isEnabled()));
    }

    private void setBasicGui(MainFrame mainFrame) {
        setLayout(null);
        setPreferredSize(new Dimension(getWidth(), 280));
        setBackground(mainFrame.bgColor);
        setBorder(new BevelBorder(BevelBorder.LOWERED));
    }

    private void repositionComponents() {
        rateLabel.setLocation(25, 280 + buffer);
        tenLabel.setLocation(authorField.getX(), 252 + buffer);
        tenBox.setLocation(authorField.getX(), 292 + buffer);
        moreLabel.setLocation(tenBox.getX() + tenBox.getWidth() + 25, 252 + buffer);
        moreBox.setLocation(tenBox.getX() + tenBox.getWidth() + 25, 292 + buffer);
        storyLabel.setLocation(moreBox.getX() + moreBox.getWidth() + 25, 252 + buffer);
        storyBox.setLocation(moreBox.getX() + moreBox.getWidth() + 25, 292 + buffer);
        funLabel.setLocation(storyBox.getX() + storyBox.getWidth() + 25, 252 + buffer);
        funBox.setLocation(storyBox.getX() + storyBox.getWidth() + 25, 292 + buffer);
        languageRatLabel.setLocation(funBox.getX() + funBox.getWidth() + 25, 252 + buffer);
        languageRatBox.setLocation(funBox.getX() + funBox.getWidth() + 25, 292 + buffer);
        headGenreLabel.setLocation(25, authorLabel.getY() + authorLabel.getHeight() + 25 + buffer);
        hGenreBox.setLocation(authorField.getX(), headGenreLabel.getY());
        subgenreLabel.setLocation(hGenreBox.getX() + hGenreBox.getWidth() + 100, headGenreLabel.getY());
        subgenreBox.setLocation(subgenreLabel.getX() + subgenreLabel.getWidth() + 25, headGenreLabel.getY());
        readingDateLabel.setLocation(25, 375 + buffer);
        monthBox.setLocation(475, 377 + buffer);
        splitDate.setLocation(monthBox.getX() + monthBox.getWidth() + 65, 375 + buffer);
        yearBox.setLocation(875, 377 + buffer);
    }

    private void activateEditAuthor() {
        editAuthorIsOpen = !editAuthorIsOpen;
        authorField.setEnabled(!authorField.isEnabled());
        if (editAuthorIsOpen) {
            buffer = 80;
            authorLabel.setText("1. Autor:");
            createSecondAuthorGui();
            repositionComponents();
            repaint();
        } else if (book.getSecondAuthor() == null) {
            buffer = 0;
            authorLabel.setText("Autor:");
            this.remove(secondAuthorLabel);
            this.remove(secondAuthorField);
            this.remove(editSecondAuthor);
            secondAuthorLabel = null;
            secondAuthorField = null;
            editSecondAuthor = null;
            repositionComponents();
            repaint();
        }
    }

    private void createSecondAuthorGui() {
        secondAuthorLabel = swingFactory.createLabel("2. Autor:", new Dimension(300, 70), 25, 90 + buffer, mainFrame.bgColor,
                new Font(fontName, fontstyle, 60), mainFrame.fontColor);
        secondAuthorField = swingFactory.createTextField(book.getSecondAuthor(), new Dimension(850, 70), 350, 90 + buffer, mainFrame.primaryColor,
                new Font(fontName, fontstyle, 60), mainFrame.fontColor);
        secondAuthorField.setEnabled(false);
        editSecondAuthor = swingFactory.createButtonWithIcon("edit.png", new Dimension(64, 64),
                secondAuthorField.getX() + secondAuthorField.getWidth() + 10, 93 + buffer, mainFrame.bgColor, null,
                e -> secondAuthorField.setEnabled(!secondAuthorField.isEnabled()));
    }

    private ImageIcon createImage(String imageName) {
        BufferedImage coverImage = new BufferedImage(75, 75, 2);
        Graphics2D g = coverImage.createGraphics();
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(read(imageName));
        } catch (IOException ignored) {
        }
        g.drawImage(originalImage, 0, 0, 75, 75, null);
        g.dispose();

        return new ImageIcon(coverImage);
    }

    private static URL read(String fileName) {
        return SwingFactory.class.getClassLoader().getResource(fileName);
    }
}
