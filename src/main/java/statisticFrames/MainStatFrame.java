package statisticFrames;

import gui.swing.CusJFrame;
import gui.swing.SwingFactory;
import main.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainStatFrame extends CusJFrame {

    private MainFrame mainFrame;
    private SwingFactory swingFactory = new SwingFactory(this);
    private JButton howMuchButton;
    private JButton whatButton;
    private JButton favButton;
    private int buttonHeight;
    private int buttonWidth;

    public MainStatFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setTitle("Lesestatistiken");
        setSize(1920, 1080);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        buttonWidth = getWidth() / 2 - 50;
        buttonHeight = getHeight() / 2 - 75;

        createHowMuchButton();
        createWhatButton();
        createFavButton();

        setVisible(true);
    }

    private void createHowMuchButton() {
        howMuchButton = swingFactory.createButton("", new Dimension(buttonWidth, buttonHeight), 25, 25,
                mainFrame.primaryColor, null, null, null, e -> {
            new QuantityFrame(mainFrame);
        });
        howMuchButton.setIcon(new ImageIcon(getImageOfSize("Quantität.jpg", howMuchButton.getWidth(), howMuchButton.getHeight())));
    }

    private void createWhatButton() {
        whatButton = swingFactory.createButton("", new Dimension(buttonWidth, buttonHeight), getWidth() / 2 + 25, 25,
                mainFrame.primaryColor, null, null, null, e -> {
            new QualityFrame(mainFrame);
                });
        whatButton.setIcon(new ImageIcon(getImageOfSize("Qualität.jpg", whatButton.getWidth(), whatButton.getHeight())));
    }

    private void createFavButton() {
        favButton = swingFactory.createButton("Lieblingsbuch.jpg", new Dimension(buttonWidth, buttonHeight),
                25, howMuchButton.getY() + howMuchButton.getHeight() + 25, mainFrame.primaryColor, null, null, null, e -> {
            new FavFrame(mainFrame);
                });
        favButton.setIcon(new ImageIcon(getImageOfSize("Lieblingsbuch.jpg", favButton.getWidth(), favButton.getHeight())));
    }

    private BufferedImage getImageOfSize(String name, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, 2);
        Graphics2D g = image.createGraphics();
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(MainFrame.read(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return image;
    }

}
