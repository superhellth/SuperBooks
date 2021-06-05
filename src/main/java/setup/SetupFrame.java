package setup;

import database.DbSetup;
import gui.swing.CusJFrame;
import gui.swing.SwingFactory;
import main.MainFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class SetupFrame extends CusJFrame {

    private SwingFactory swingFactory = new SwingFactory(this);

    private JTextField nameField;
    private JTextField pathField;
    private JTextField coverField;
    private File dbDirectory;
    private File coverDirectory;

    SetupFrame() {
        setTitle("Setup");
        setResizable(false);
        setSize(1000, 705);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        createGui();

        centralize();
        setVisible(true);
    }

    private void createGui() {
        swingFactory.createLabel("<html><div align=center>Erstelle deine eigene Datenbank,<br> in der du alle Bücherspeichern kannst,<br>" +
                        "&nbsp;die du gelesen hast oder noch lesen wirst!</div></html>", new Dimension(getWidth(), 180), 0, 0, Color.GRAY,
                new Font("Times New Roman", Font.BOLD, 50), Color.BLACK);
        JLabel nameLabel = swingFactory.createLabel("Gib deiner Datenbank einen Namen:", new Dimension(950, 55), 25, 190, Color.WHITE,
                new Font("Times New Roman", Font.BOLD, 50), Color.BLACK);
        nameField = swingFactory.createTextField("", new Dimension(950, 55), 25, nameLabel.getY() + 55, Color.WHITE,
                new Font("Times New Roman", Font.BOLD, 50), Color.BLACK);

        JLabel pathLabel = swingFactory.createLabel("Wo soll deine Datenbank liegen?:", new Dimension(950, 55), 25, nameField.getY() +
                nameField.getHeight() + 25, Color.WHITE, new Font("Times New Roman", Font.BOLD, 50), Color.BLACK);
        pathField = swingFactory.createTextField("", new Dimension(750, 55), 225, pathLabel.getY() + pathLabel.getHeight(), Color.WHITE,
                new Font("Times New Roman", Font.BOLD, 35), Color.BLACK);
        swingFactory.createButton("Browse", new Dimension(175, 55), 25, pathField.getY(), Color.WHITE,
                new Font("Times New Roman", Font.BOLD, 40), Color.BLACK, new LineBorder(Color.BLACK, 2), e -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnVal = chooser.showOpenDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        dbDirectory = chooser.getSelectedFile();
                        pathField.setText(dbDirectory.getPath());
                    }
                });

        JLabel coverLabel = swingFactory.createLabel("Wo willst du deine Buchcover speichern?:", new Dimension(950, 55), 25,
                pathField.getY() + pathField.getHeight() + 25, Color.WHITE, new Font("Times New Roman", Font.BOLD, 50),
                Color.BLACK);
        coverField = swingFactory.createTextField("", new Dimension(750, 55), 225, coverLabel.getY() + coverLabel.getHeight(),
                Color.WHITE, new Font("Times New Roman", Font.BOLD, 35), Color.BLACK);
        swingFactory.createButton("Browse", new Dimension(175, 55), 25, coverField.getY(), Color.WHITE,
                new Font("Times New Roman", Font.BOLD, 40), Color.BLACK,new LineBorder(Color.BLACK, 2),  e -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnVal = chooser.showOpenDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        coverDirectory = chooser.getSelectedFile();
                        coverField.setText(coverDirectory.getPath());
                    }
                });

        swingFactory.createButton("Fertig!", new Dimension(250, 50), getWidth() - 250 - 25, pathField.getY() +
                        pathField.getHeight() + 160, Color.WHITE, new Font("Times New Roman", Font.BOLD, 40), Color.BLACK,
                new LineBorder(Color.BLACK, 2), e -> {
                    String name = nameField.getText();
                    String CONNECTION = "jdbc:hsqldb:file:" + dbDirectory + "\\" + name;

                    BufferedWriter writer = null;
                    try {
                        writer = new BufferedWriter(new FileWriter("dbPath.txt"));
                        writer.write(CONNECTION);
                        writer.flush();
                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        try {
                            writer.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    Connection connection = null;
                    try {
                        connection = DriverManager.getConnection(CONNECTION);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    new DbSetup(connection, coverDirectory.getPath());
                    new MainFrame(CONNECTION);
                    dispose();
                });
    }
}
