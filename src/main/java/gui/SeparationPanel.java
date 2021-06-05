package gui;

import bookManagement.Subgenre;
import gui.button.DeleteGenreButton;
import gui.button.HideButton;
import gui.label.SeparationLabel;
import gui.swing.SwingFactory;
import main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SeparationPanel extends JPanel {

    private Color bg;
    private Color fontColor;
    private Font myFont;
    private int height;
    private JLabel label;
    private JButton hideButton;

    private String key;

    private SwingFactory swingFactory;

    public SeparationPanel(MainFrame mainFrame, String text, int height, Color bg) {
        this.bg = bg;
        fontColor = mainFrame.fontColor;
        myFont = mainFrame.myFont;
        this.height = height;
        swingFactory = new SwingFactory(this);

        //set basic gui
        setBasicGui(mainFrame, height, bg);

        //create label
        label =  new SeparationLabel(text, bg, fontColor, myFont, height);
        add(label);

        //create hide button
        hideButton = new HideButton(new ImageIcon(MainFrame.read("hide.png")), bg, height);
        hideButton.addActionListener(e -> {
            mainFrame.isHidden.put(key, !mainFrame.isHidden(key));
            System.out.println(mainFrame.isHidden.size());
            mainFrame.init();
        });

        createKey();
        add(hideButton);
    }

    private void setBasicGui(MainFrame mainFrame, int height, Color bg) {
        setVisible(true);
        setLayout(null);
        setBackground(bg);
        setMinimumSize(new Dimension(mainFrame.getWidth(), height));
        setPreferredSize(new Dimension(mainFrame.getWidth(), height));
        setMaximumSize(new Dimension(mainFrame.getWidth(), height));
    }

    public void addDeleteGenreButton(MainFrame mainFrame, Subgenre subgenre) {
        JButton button = new DeleteGenreButton(new ImageIcon(MainFrame.read("trash.png")), bg);
        button.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(this, "Sind Sie sicher dass sie das Genre " + subgenre.getSubgenre() +
                    " löschen wollen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                mainFrame.dbWriter.deleteGenre(subgenre);
                mainFrame.init();
            }
        });

        add(button);
    }

    private void createKey() {
        key = label.getText() + getHeight() + bg.toString();
    }

    public void addRenameSubgenreButton(MainFrame mainFrame, String genre, String subgenre) {
        JButton editButton = new JButton(new ImageIcon(MainFrame.read("editNew.png")));

        editButton.setBackground(bg);
        editButton.setSize(64, 64);
        editButton.setLocation(35 + 64, (height - hideButton.getHeight()) / 2);
        editButton.setBorder(null);
        editButton.addActionListener(e -> {
            String oldName = label.getText();
            remove(label);

            JTextField textField = swingFactory.createTextField(oldName, new Dimension(800, height), 560, 0,
                    mainFrame.bgColor, new Font(myFont.getName(), myFont.getStyle(), 60), mainFrame.fontColor);
            textField.setHorizontalAlignment(SwingConstants.CENTER);
            textField.setBackground(bg);
            textField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e1) {
                    if (e1.getKeyCode() == 10) {
                        String newName = textField.getText();

                        remove(textField);
                        label.setText(newName);
                        add(label);

                        mainFrame.dbWriter.renameGenre(new Subgenre(genre, subgenre), newName);

                        mainFrame.init();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });

            add(textField);
        });
        add(editButton);
    }

    public String getKey() {
        return key;
    }
}
