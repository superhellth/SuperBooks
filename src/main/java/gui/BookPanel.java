package gui;

import bookManagement.Book;
import gui.button.DeleteButton;
import gui.button.EditButton;
import gui.label.*;
import main.MainFrame;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class BookPanel extends JPanel {

    public BookPanel(MainFrame mainFrame, Book book) {
        Color bgColor = mainFrame.bgColor;
        Color fontColor = mainFrame.fontColor;
        Color primaryColor = mainFrame.primaryColor;
        Color secondaryColor = mainFrame.secondaryColor;
        String coverPath = mainFrame.coverPath;
        Font myFont = mainFrame.myFont;

        int offSetRight = 250;

        //set basic gui
        setBasicGui(mainFrame, bgColor);

        //create cover label
        CoverLabel cover = new CoverLabel(bgColor, fontColor, primaryColor, secondaryColor, coverPath, myFont, book);
        add(cover);
        cover.setLocation(50, 25);
        cover.setHorizontalAlignment(SwingConstants.CENTER);

        //create title label
        JLabel name = new TitleLabel(book.getName(), fontColor, myFont);
        add(name);
        name.setLocation(offSetRight, 35);

        //create author label
        JLabel author = new AuthorLabel(book.getAuthor(), bgColor, fontColor, myFont);
        add(author);
        author.setLocation(offSetRight, name.getHeight() + 35);

        //create rating and page label
        Font checkFont = new Font(myFont.getName(), myFont.getStyle(), 45);
        if (mainFrame.showRat) {
            JLabel ratingLabel = new RatingLabel(book.getTotalRat(), bgColor, secondaryColor, checkFont);
            add(ratingLabel);
            ratingLabel.setLocation(offSetRight, author.getHeight() + author.getY());
        }
        if (mainFrame.showPages) {
            JLabel pageLabel = new PageLabel(book.getNumPages(), bgColor, secondaryColor, checkFont);
            add(pageLabel);
            pageLabel.setLocation(1100, author.getHeight() + author.getY());
        }

        //create edit button
        Font buttonFont = new Font(myFont.getName(), myFont.getStyle(), 40);
        JButton editButton = new EditButton(new ImageIcon(BookPanel.class.getClassLoader().getResource("edit.png")),
                fontColor, bgColor, buttonFont);
        add(editButton);
        editButton.setLocation(1811, 175);
        editButton.addActionListener(e -> {
            mainFrame.editIsOpen.put(book.getKey(), !mainFrame.editIsOpen(book.getKey()));
            mainFrame.allEditPanels.put(book.getKey(), new EditPanel(mainFrame, book));
            mainFrame.init();
        });
        editButton.setBorder(null);

        //create delete button
        JButton deleteButton = new DeleteButton(new ImageIcon(MainFrame.read("trash.png")), bgColor, buttonFont, primaryColor);
        add(deleteButton);
        deleteButton.setLocation(1811, 25);
        deleteButton.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(mainFrame, "Wollen sie " + book.getName() +
                            " wirklich entfernen?",
                    "Bestätigung", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                mainFrame.dbWriter.deleteBook(book);
                mainFrame.deleteABookPanel(book);
                mainFrame.init();
            }
        });

        if (book.isRecommended()) {
            setBorder(BorderFactory.createMatteBorder(5, 5, 5, 40,
                    new Color(255, 215, 0)));
        }
    }

    private void setBasicGui(MainFrame mainFrame, Color bgColor) {
        setLayout(null);
        setSize(mainFrame.getWidth(), 250);
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setBackground(bgColor);
    }

}
