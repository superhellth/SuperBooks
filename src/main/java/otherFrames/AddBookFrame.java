package otherFrames;

import bookManagement.Book;
import bookManagement.Genre;
import bookManagement.Subgenre;
import calendar.MyCalendar;
import gui.swing.CusJFrame;
import gui.swing.SwingFactory;
import main.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class AddBookFrame extends CusJFrame {

    private MainFrame mainFrame;
    private Font smFont;

    private Dimension smLabelSize = new Dimension(175, 30);
    private Dimension smFieldSize;

    private JTextField bookNameField;
    private JTextField authorField;
    private JTextField secondAuthorField;
    private JComboBox<String> genreBox;
    private JComboBox<String> subgenreBox;
    private JTextField pageField;
    private JTextField pubCompField;
    private JComboBox languageBox;
    private JComboBox<String> tensionBox;
    private JComboBox<String> intBox;
    private JComboBox<String> funBox;
    private JComboBox<String> storyBox;
    private JComboBox<String> languageRatBox;
    private JComboBox<String> monthBox;
    private JComboBox<String> yearBox;

    private JButton addButton;

    private JLabel subgenreLabel;
    private JLabel languageLabel;
    private JLabel ratLabel;
    private JLabel tensionLabel;
    private JLabel storyLabel;
    private JLabel funLabel;
    private JLabel moreLabel;
    private JLabel languageRatLabel;
    private JLabel readingDateLabel;
    private JLabel points;
    private JButton recommend;

    private boolean secondAuthor = false;
    private int buffer = 0;
    private boolean isRecommended = false;

    private ImageIcon star1 = createImage("star.png");
    private ImageIcon star2 = createImage("star2.png");

    public AddBookFrame(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        smFont = new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), 30);
        SwingFactory swingFactory = new SwingFactory(this);

        setSize(new Dimension(1500, 625));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Buch hinzufügen");
        setLayout(null);
        getContentPane().setBackground(mainFrame.bgColor);
        centralize();

        createLabel("Name des Buchs:", 60, new Dimension(500, 60), 25, 0);
        createLabel("Autor:", 60, new Dimension(500, 60), 25, 145);

        createLabel("Genre:", 60, new Dimension(200, 60), 2 * 650, 0);
        subgenreLabel = createLabel("Subgenre:", 30, smLabelSize, 25, 285 + buffer);
        languageLabel = createLabel("Sprache:", 30, smLabelSize, 225, 285 + buffer);
        createLabel("Verlag:", 30, smLabelSize, 1100, 205 - smLabelSize.height);
        createLabel("Seitenzahl:", 30, smLabelSize, 1300, 205 - smLabelSize.height);

        bookNameField = createJTextField(60, new Dimension(1225, 60), 25, 60);
        authorField = createJTextField(60, new Dimension(800, 60), 25, 205);
        JButton secondAuthorButton = swingFactory.createButton("1 Autor", new Dimension(200, 60), authorField.getX() +
                authorField.getWidth() + 25, 205, mainFrame.secondaryColor, new Font(mainFrame.myFont.getName(),
                mainFrame.myFont.getStyle(), 40), mainFrame.fontColor, null, null);
        secondAuthorButton.addActionListener(e -> {
            secondAuthor = !secondAuthor;
            if (secondAuthor) {
                buffer = 70;
                secondAuthorField = createJTextField(60, new Dimension(800, 60), 25, 275);
                setSize(getWidth(), getHeight() + 70);
                secondAuthorButton.setText("2 Autoren");
            } else {
                buffer = 0;
                remove(secondAuthorField);
                secondAuthorField = null;
                setSize(getWidth(), getHeight() - 70);
                secondAuthorButton.setText("1 Autor");
            }
            replaceComponents();
        });
        genreBox = createComboBox(mainFrame.getGenreStrings(), new
                Dimension(175, 60), 2 * 650, 60);
        genreBox.addActionListener(e ->
        {
            subgenreBox.removeAllItems();
            for (String subgenre : mainFrame.bookFilter.getSubgenresOfAGenre(new Genre(
                    genreBox.getSelectedItem().toString()))) {
                subgenreBox.addItem(subgenre);
            }
        });


        smFieldSize = new Dimension(175, 60);
        pageField = createJTextField(30, smFieldSize, 2 * 650, authorField.getY());
        pubCompField = createJTextField(30, smFieldSize, pageField.getX() - smFieldSize.width - 25,
                authorField.getY());
        subgenreBox = createComboBox(mainFrame.bookFilter.getSubgenresOfAGenre(
                new Genre(genreBox.getSelectedItem().toString())), smFieldSize, 25, 315 + buffer);
        subgenreBox.setFont(new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), 28));
        languageBox = createComboBox(mainFrame.dbReader.getLanguages(), smFieldSize, 225, 315 + buffer);
        ratLabel = createLabel("Bewertung:", 60, new Dimension(310, 70), 25, 400 + buffer);
        tensionLabel = createLabel("Spannung:", 30, smLabelSize, 25, 480 + buffer);
        moreLabel = createLabel("Mehrwert:", 30, smLabelSize, 225, 480 + buffer);
        funLabel = createLabel("Lesespaß:", 30, smLabelSize, 425, 480 + buffer);
        storyLabel = createLabel("Geschichte:", 30, smLabelSize, 625, 480 + buffer);
        languageRatLabel = createLabel("Sprache:", 30, smLabelSize, 825, 480 + buffer);

        ArrayList<String> oneToTen = new ArrayList<>();
        for (
                int i = 1;
                i < 11; i++) {
            oneToTen.add(Integer.toString(i));
        }
        oneToTen.add("Irrelevant");
        tensionBox = createComboBox(oneToTen, smFieldSize, 25, 510 + buffer);
        intBox = createComboBox(oneToTen, smFieldSize, 225, 510 + buffer);
        funBox = createComboBox(oneToTen, smFieldSize, 425, 510 + buffer);
        storyBox = createComboBox(oneToTen, smFieldSize, 625, 510 + buffer);
        languageRatBox = createComboBox(oneToTen, smFieldSize, 825, 510 + buffer);
        readingDateLabel = createLabel("Gelesen im:", 60, new Dimension(350, 70),
                languageBox.getX() + languageBox.getWidth() + 75, subgenreBox.getY() + buffer);
        monthBox = createComboBox(MyCalendar.getMonthStrings(), new
                        Dimension(300, 75), readingDateLabel.getX() + readingDateLabel.getWidth(),
                subgenreBox.getY() + buffer);
        monthBox.setFont(new Font(monthBox.getFont().getName(), monthBox.getFont().getStyle(), 50));
        monthBox.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH) + 1);
        yearBox = createComboBox(MainFrame.getYears(), new Dimension(300, 75),
                monthBox.getX() + monthBox.getWidth() + 50, subgenreBox.getY() + buffer);
        yearBox.setFont(new Font(yearBox.getFont().getName(), yearBox.getFont().getStyle(), 50));
        yearBox.setSelectedItem(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        points = createLabel(":", 60, new Dimension(20, 75), monthBox.getX() + monthBox.getWidth() + 13,
                languageBox.getY() + buffer);
        recommend = swingFactory.createButton("", new Dimension(ratLabel.getHeight(),
                        ratLabel.getHeight()), ratLabel.getX() + ratLabel.getWidth() + 25, ratLabel.getY(),
                null, null, null, null, e -> {
                    isRecommended = !isRecommended;
                    if (isRecommended) {
                        recommend.setIcon(star2);
                        getRootPane().setBorder(BorderFactory.createMatteBorder(7, 7, 7, 7,
                                new Color(255, 215, 0)));
                    } else {
                        recommend.setIcon(star1);
                        getRootPane().setBorder(null);
                    }
                });
        recommend.setIcon(star1);
        recommend.setOpaque(false);

        createAddButton();

        setResizable(false);

        setVisible(true);
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

    private void replaceComponents() {
        subgenreLabel.setLocation(25, 285 + buffer);
        languageLabel.setLocation(225, 285 + buffer);
        ratLabel.setLocation(25, 400 + buffer);
        tensionLabel.setLocation(25, 480 + buffer);
        storyLabel.setLocation(625, 480 + buffer);
        funLabel.setLocation(425, 480 + buffer);
        moreLabel.setLocation(225, 480 + buffer);
        languageRatLabel.setLocation(825, 480 + buffer);
        subgenreBox.setLocation(25, 315 + buffer);
        pageField.setLocation(2 * 650, authorField.getY());
        pubCompField.setLocation(pageField.getX() - smFieldSize.width - 25,
                authorField.getY());
        languageBox.setLocation(225, 315 + buffer);
        tensionBox.setLocation(25, 510 + buffer);
        intBox.setLocation(225, 510 + buffer);
        funBox.setLocation(425, 510 + buffer);
        storyBox.setLocation(625, 510 + buffer);
        languageRatBox.setLocation(825, 510 + buffer);
        monthBox.setLocation(readingDateLabel.getX() + readingDateLabel.getWidth(), subgenreBox.getY());
        yearBox.setLocation(monthBox.getX() + monthBox.getWidth() + 50, subgenreBox.getY());
        readingDateLabel.setLocation(languageBox.getX() + languageBox.getWidth() + 75, subgenreBox.getY());
        points.setLocation(monthBox.getX() + monthBox.getWidth() + 13,
                languageBox.getY());
        addButton.setLocation(languageRatBox.getX() + languageRatBox.getWidth() + 50, storyBox.getY());
    }

    private JLabel createLabel(String text, int fontSize, Dimension size, int xCord, int yCord) {
        JLabel label = new JLabel(text);

        label.setBackground(mainFrame.bgColor);
        label.setForeground(mainFrame.fontColor);
        label.setFont(new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), fontSize));

        label.setSize(size);
        label.setLocation(xCord, yCord);

        add(label);

        return label;
    }

    private JTextField createJTextField(int fontSize, Dimension size, int xCord, int yCord) {
        JTextField textField = new JTextField();

        textField.setBackground(mainFrame.primaryColor);
        textField.setForeground(mainFrame.fontColor);
        textField.setFont(new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), fontSize));

        textField.setSize(size);
        textField.setLocation(xCord, yCord);

        add(textField);
        return textField;
    }

    private JComboBox<String> createComboBox(ArrayList<String> items, Dimension size, int xCord, int yCord) {
        JComboBox<String> comboBox = new JComboBox<>();
        for (String s : items) {
            comboBox.addItem(s);
        }

        comboBox.setBackground(mainFrame.secondaryColor);
        comboBox.setForeground(mainFrame.fontColor);
        comboBox.setFont(smFont);

        comboBox.setSize(size);
        comboBox.setLocation(xCord, yCord);

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals("5")) {
                comboBox.setSelectedItem("5");
            }
        }

        add(comboBox);
        return comboBox;
    }

    private void createAddButton() {
        addButton = new JButton("Hinzufügen");
        addButton.setForeground(mainFrame.fontColor);
        addButton.setBackground(mainFrame.primaryColor);
        addButton.addActionListener(e -> {
            Book book = new Book();
            try {
                book.setName(bookNameField.getText());
                book.setAuthor(authorField.getText());
                if (secondAuthor) {
                    book.setSecondAuthor(secondAuthorField.getText());
                }
                book.setNumPages(Integer.parseInt(pageField.getText()));
                book.setSubgenre(new Subgenre(genreBox.getSelectedItem().toString(),
                        subgenreBox.getSelectedItem().toString()));
                if (tensionBox.getSelectedItem().toString().equalsIgnoreCase("irrelevant")) {
                    book.setTensionRat(11);
                } else {
                    book.setTensionRat(Integer.parseInt(tensionBox.getSelectedItem().toString()));
                }
                if (intBox.getSelectedItem().toString().equalsIgnoreCase("irrelevant")) {
                    book.setInterestRat(11);
                } else {
                    book.setInterestRat(Integer.parseInt(intBox.getSelectedItem().toString()));
                }
                if (storyBox.getSelectedItem().toString().equalsIgnoreCase("irrelevant")) {
                    book.setStoryRat(11);
                } else {
                    book.setStoryRat(Integer.parseInt(storyBox.getSelectedItem().toString()));
                }
                if (funBox.getSelectedItem().toString().equalsIgnoreCase("irrelevant")) {
                    book.setFunRat(11);
                } else {
                    book.setFunRat(Integer.parseInt(funBox.getSelectedItem().toString()));
                }
                if (languageRatBox.getSelectedItem().toString().equalsIgnoreCase("irrelevant")) {
                    book.setLanguageRat(11);
                } else {
                    book.setLanguageRat(Integer.parseInt(languageRatBox.getSelectedItem().toString()));
                }
                book.setLanguage(languageBox.getSelectedItem().toString());
                book.setPubComp(pubCompField.getText());
                String month;
                if (monthBox.getSelectedItem().toString().equalsIgnoreCase("Unbekannt")) {
                    month = "X";
                } else {
                    month = Integer.toString(monthBox.getSelectedIndex());
                }
                String year;
                if (yearBox.getSelectedItem().toString().equalsIgnoreCase("Unbekannt")) {
                    year = "X";
                } else {
                    year = yearBox.getSelectedItem().toString();
                }
                book.setReadingDate(month + "." + year);
                book.setRecommended(isRecommended);

                AddBookFrame.this.dispose();
            } catch (Exception exeption) {
                JOptionPane.showMessageDialog(mainFrame, "Fehler bei der Eingabe!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }

            mainFrame.dbWriter.addBookToDb(book);
            mainFrame.addABookPanel(book);
            mainFrame.init();
        });
        addButton.setSize(425, 60);
        addButton.setLocation(languageRatBox.getX() + languageRatBox.getWidth() + 50, storyBox.getY() + buffer);
        addButton.setFont(smFont);

        add(addButton);
    }
}
