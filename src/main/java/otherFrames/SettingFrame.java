package otherFrames;

import com.sun.java.swing.plaf.windows.WindowsButtonUI;
import gui.swing.CusJFrame;
import gui.swing.SwingFactory;
import main.MainFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SettingFrame extends CusJFrame {

    private MainFrame mainFrame;
    private SwingFactory swingFactory = new SwingFactory(this);

    private JColorChooser mainColorBox;
    private JColorChooser secondaryColorBox;
    private JColorChooser fontColorBox;
    private JComboBox<String> fontBox;
    private JTextField coverPathField;
    private JCheckBox showRating;
    private JCheckBox showPages;
    private JCheckBox resetScroll;
    private Map<String, Font> textToFont;

    public SettingFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setTitle("Einstellungen");
        setSize(1750, 900);
        getContentPane().setBackground(mainFrame.bgColor);
        setLayout(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        centralize();

        init();
        createDoneButton();

        setVisible(true);
    }

    private void createDoneButton() {
        swingFactory.createButton("Fertig", new Dimension(309, 60), 1400, 775, mainFrame.bgColor,
                new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), 50), mainFrame.fontColor,
                new LineBorder(mainFrame.secondaryColor, 3), e -> {
                    mainFrame.myFont = textToFont.get(fontBox.getSelectedItem().toString());
                    mainFrame.fontColor = fontColorBox.getColor();
                    mainFrame.primaryColor = mainColorBox.getColor();
                    mainFrame.secondaryColor = secondaryColorBox.getColor();
                    mainFrame.coverPath = coverPathField.getText();
                    mainFrame.showRat = showRating.isSelected();
                    mainFrame.showPages = showPages.isSelected();
                    mainFrame.resetScroll = resetScroll.isSelected();

                    mainFrame.updateSettings();
                    mainFrame.init();
                    dispose();
                });
    }

    private void init() {
        createColorGUI();
        createFontGUI();
        createCoverPathGUI();
        createHideOptions();
    }

    private void createHideOptions() {
        JLabel showRatLabel = swingFactory.createLabel("Buchbewertungen anzeigen:", new Dimension(625, 60), 25, fontBox.getY()
                + fontBox.getHeight() + 50, mainFrame.bgColor, new Font(mainFrame.myFont.getName(),
                mainFrame.myFont.getStyle(), 45), mainFrame.fontColor);
        showRating = new JCheckBox("", mainFrame.showRat);
        showRating.setUI(new WindowsButtonUI());
        showRating.setSize(60, 60);
        showRating.setLocation(showRatLabel.getX() + showRatLabel.getWidth() + 25, showRatLabel.getY());

        JLabel showPagesLabel = swingFactory.createLabel("Seitenzahl anzeigen:", new Dimension(540, 60), 900, showRatLabel.getY(),
                mainFrame.bgColor, new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), 45), mainFrame.fontColor);
        showPages = new JCheckBox("", mainFrame.showPages);
        showPages.setUI(new WindowsButtonUI());
        showPages.setSize(60, 60);
        showPages.setLocation(showPagesLabel.getX() + showPagesLabel.getWidth() + 25, showPagesLabel.getY());

        JLabel resetScrollLabel = swingFactory.createLabel("Hoch scrollen wenn Sortierung geändert:", new Dimension(600, 60),
                25, showRatLabel.getY() + showRatLabel.getHeight() + 25, mainFrame.bgColor, new Font(mainFrame.myFont.getName(),
                        mainFrame.myFont.getStyle(), 30), mainFrame.fontColor);
        resetScroll = new JCheckBox("", mainFrame.resetScroll);
        resetScroll.setUI(new WindowsButtonUI());
        resetScroll.setSize(60, 60);
        resetScroll.setLocation(showRating.getX(), resetScrollLabel.getY());

        addAll(showRating, showPages, resetScroll);
    }

    private void createCoverPathGUI() {
        JLabel pathLabel = swingFactory.createLabel("Pfad der Cover:", new Dimension(500, 60), fontBox.getX() +
                        fontBox.getWidth() + 50, mainColorBox.getY() + mainColorBox.getHeight() + 25, mainFrame.bgColor,
                new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), 50), mainFrame.fontColor);

        coverPathField = swingFactory.createTextField(mainFrame.coverPath, new Dimension(1150, 60), pathLabel.getX(), fontBox.getY(),
                mainFrame.primaryColor, new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), 40), mainFrame.fontColor);
    }

    private void createFontGUI() {
        JLabel fontLabel = swingFactory.createLabel("Font:", new Dimension(300, 60), 25, mainColorBox.getY() +
                mainColorBox.getHeight() + 25, mainFrame.bgColor, new Font(mainFrame.myFont.getName(),
                mainFrame.myFont.getStyle(), 50), mainFrame.fontColor);

        textToFont = new TreeMap<>();
        for (String font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            textToFont.put(font, new Font(font, Font.BOLD, mainFrame.myFont.getSize()));
        }
        textToFont.put("Helvetica", new Font("Helvetica", Font.BOLD, mainFrame.myFont.getSize()));

        Map<Font, String> fontToText = new HashMap<>();
        for (Map.Entry<String, Font> entry : textToFont.entrySet()) {
            fontToText.put(entry.getValue(), entry.getKey());
        }

        fontBox = new JComboBox<>();
        for (String fontName : textToFont.keySet()) {
            fontBox.addItem(fontName);
        }
        fontBox.setSelectedItem(fontToText.get(mainFrame.myFont));
        fontBox.setSize(300, 60);
        fontBox.setLocation(25, fontLabel.getY() + fontLabel.getHeight() + 20);
        fontBox.setFont(new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), 30));
        fontBox.setBackground(mainFrame.secondaryColor);
        fontBox.setForeground(mainFrame.fontColor);

        add(fontBox);
    }

    private void createColorGUI() {
        JLabel mainColorLabel = swingFactory.createLabel("Erste Farbe:", new Dimension(300, 60), 25, 0, mainFrame.bgColor,
                new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), 45), mainFrame.fontColor);
        mainColorBox = new JColorChooser();
        mainColorBox.setSize(550, 350);
        mainColorBox.setLocation(25, 70);
        mainColorBox.setColor(mainFrame.primaryColor);
        mainColorBox.setBackground(mainFrame.secondaryColor);
        swingFactory.createLabel("", new Dimension(200, 60), mainColorLabel.getX() + mainColorLabel.getWidth() +
                25, 0, mainColorBox.getColor(), null, null);

        JLabel secondaryColorLabel = swingFactory.createLabel("Zweite Farbe:", new Dimension(300, 60), mainColorBox.getWidth() + 50,
                0, mainFrame.bgColor,
                new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), 45), mainFrame.fontColor);
        secondaryColorBox = new JColorChooser();
        secondaryColorBox.setSize(550, 350);
        secondaryColorBox.setLocation(mainColorBox.getWidth() + 50, 70);
        secondaryColorBox.setColor(mainFrame.secondaryColor);
        secondaryColorBox.setBackground(mainFrame.secondaryColor);
        swingFactory.createLabel("", new Dimension(200, 60), secondaryColorLabel.getX() + secondaryColorLabel.getWidth() + 25,
                0, secondaryColorBox.getColor(), null, null);

        JLabel fontColorLabel = swingFactory.createLabel("Schriftfarbe:", new Dimension(300, 60), secondaryColorBox.getWidth()
                + secondaryColorBox.getX() + 25, 0, mainFrame.bgColor, new Font(mainFrame.myFont.getName(),
                mainFrame.myFont.getStyle(), 45), mainFrame.fontColor);
        fontColorBox = new JColorChooser();
        fontColorBox.setSize(550, 350);
        fontColorBox.setLocation(secondaryColorBox.getWidth() + secondaryColorBox.getX() + 25, 70);
        fontColorBox.setBackground(mainFrame.secondaryColor);
        fontColorBox.setColor(mainFrame.fontColor);
        swingFactory.createLabel("", new Dimension(200, 60), fontColorLabel.getX() + fontColorLabel.getWidth() + 25,
                0, fontColorBox.getColor(), null, null);

        addAll(mainColorBox, secondaryColorBox, fontColorBox);
    }
}
