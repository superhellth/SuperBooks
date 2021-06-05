package gui.label;

import bookManagement.Book;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CoverLabel extends JLabel {

    public CoverLabel(Color bgColor, Color fontColor, Color primaryColor, Color secondaryColor, String coverPath, Font myFont, Book book) {
        setSize(125, 200);

        BufferedImage coverImage = getBufferedImage(coverPath, book);


        setIcon(new ImageIcon(coverImage));

    }

    private BufferedImage getBufferedImage(String coverPath, Book book) {
        BufferedImage coverImage = new BufferedImage(125, 200, 2);
        Graphics2D g = coverImage.createGraphics();
        BufferedImage originalImage = null;

        try {
            originalImage = ImageIO.read(new File(coverPath + "\\" + book.getName().replaceAll("\\?", "") + ".jpg"));
        } catch (IOException ignored) {
        }
        g.drawImage(originalImage, 0, 0, 125, 200, null);
        g.dispose();
        return coverImage;
    }
}
