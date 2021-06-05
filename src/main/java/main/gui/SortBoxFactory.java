package main.gui;

import main.MainFrame;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Objects;

public class SortBoxFactory {

    private MainFrame mainFrame;
    private JComboBox<String> firstCrit;
    private JComboBox<String> secondCrit;
    private ArrayList<String> firstCrits = new ArrayList<>();
    private ArrayList<String> secondCrits = new ArrayList<>();

    public SortBoxFactory(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        firstCrits.add("Genre");
        firstCrits.add("Lesedatum");
        firstCrits.add("Bewertung insgesamt");
        firstCrits.add("Name des Buches");
        firstCrits.add("Vorname des Autors");
        firstCrits.add("Nachname des Autors");
        firstCrits.add("Verlag");
        firstCrits.add("Seitenzahl steigend");
        firstCrits.add("Seitenzahl fallend");
        firstCrits.add("Spannung");
        firstCrits.add("Lesespaß");
        firstCrits.add("Geschichte");
        firstCrits.add("Mehrwert");
        firstCrits.add("Sprachbewertung");

        secondCrits.add("Lesedatum");
        secondCrits.add("Bewertung insgesamt");
        secondCrits.add("Name des Buches");
        secondCrits.add("Vorname des Autors");
        secondCrits.add("Nachname des Autors");
        secondCrits.add("Seitenzahl steigend");
        secondCrits.add("Seitenzahl fallend");
        secondCrits.add("Spannung");
        secondCrits.add("Lesespaß");
        secondCrits.add("Geschichte");
        secondCrits.add("Mehrwert");
        secondCrits.add("Sprachbewertung");
    }

    public JComboBox<String> createFirstCrit() {
        firstCrit = new JComboBox<>();

        firstCrit.setBackground(mainFrame.secondaryColor);
        firstCrit.setForeground(mainFrame.fontColor);
        firstCrit.setFont(mainFrame.myFont);

        firstCrit.setSize((int) (375 / 1920d * mainFrame.getWidth()), (int) (80 / 1080d * mainFrame.getHeight()));
        firstCrit.setLocation((int) (850 / 1920d * mainFrame.getWidth()),
                (int) ((mainFrame.headerHeight + 10) / 1080d * mainFrame.getHeight()));

        fillComboBox(firstCrit, firstCrits);

        firstCrit.setSelectedItem("Genre");

        firstCrit.addItemListener(e -> {
            mainFrame.isHidden.clear();
            mainFrame.editIsOpen.clear();

            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }
            mainFrame.skipInit = true;

            switch (Objects.requireNonNull(firstCrit.getSelectedItem()).toString()) {
                case "Genre":
                    mainFrame.sortedByFirst = MainFrame.BY_GENRE;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.setVisible(true);
                    break;

                case "Name des Buches":
                    mainFrame.sortedByFirst = MainFrame.BY_NAME;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.setVisible(false);
                    break;

                case "Vorname des Autors":
                    mainFrame.sortedByFirst = MainFrame.BY_AUTHOR_FIRST_NAME;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.removeItem("Vorname des Autors");
                    secondCrit.removeItem("Nachname des Autors");
                    secondCrit.setVisible(true);
                    break;

                case "Nachname des Autors":
                    mainFrame.sortedByFirst = MainFrame.BY_AUTHOR_LAST_NAME;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.removeItem("Vorname des Autors");
                    secondCrit.removeItem("Nachname des Autors");
                    secondCrit.setVisible(true);
                    break;

                case "Seitenzahl steigend":
                    mainFrame.sortedByFirst = MainFrame.BY_PAGES_ASCENDING;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.setVisible(false);
                    break;

                case "Seitenzahl fallend":
                    mainFrame.sortedByFirst = MainFrame.BY_PAGES_DESCENDING;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.setVisible(false);
                    break;

                case "Spannung":
                    mainFrame.sortedByFirst = MainFrame.BY_TENSION;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.removeItem("Spannung");
                    secondCrit.setVisible(true);
                    break;

                case "Geschichte":
                    mainFrame.sortedByFirst = MainFrame.BY_STORY;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.removeItem("Geschichte");
                    secondCrit.setVisible(true);
                    break;

                case "Lesespaß":
                    mainFrame.sortedByFirst = MainFrame.BY_FUN;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.removeItem("Lesespaß");
                    secondCrit.setVisible(true);
                    break;

                case "Mehrwert":
                    mainFrame.sortedByFirst = MainFrame.BY_INTEREST;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.removeItem("Mehrwert");
                    secondCrit.setVisible(true);
                    break;

                case "Bewertung insgesamt":
                    mainFrame.sortedByFirst = MainFrame.BY_TOTAL_RAT;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.setVisible(false);
                    break;

                case "Lesedatum":
                    mainFrame.sortedByFirst = MainFrame.BY_READING_DATE;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.removeItem("Lesedatum");
                    secondCrit.setVisible(true);
                    break;

                case "Verlag":
                    mainFrame.sortedByFirst = MainFrame.BY_PUB_COMP;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.setVisible(true);
                    break;

                case "Sprachbewertung":
                    mainFrame.sortedByFirst = MainFrame.BY_LANGUAGE_RAT;
                    fillComboBox(secondCrit, secondCrits);
                    secondCrit.removeItem("Sprachbewertung");
                    secondCrit.setVisible(true);
                    break;
            }

            mainFrame.skipInit = false;
            mainFrame.init();
        });
        return firstCrit;
    }

    public JComboBox<String> createSecondCrit() {
        secondCrit = new JComboBox<>();

        secondCrit.setBackground(mainFrame.secondaryColor);
        secondCrit.setForeground(mainFrame.fontColor);
        secondCrit.setFont(mainFrame.myFont);

        secondCrit.setSize((int) (375 / 1920d * mainFrame.getWidth()), (int) (80 / 1080d * mainFrame.getHeight()));
        secondCrit.setLocation((int) ((850 + 375 + 25) / 1920d * mainFrame.getWidth()),
                (int) ((mainFrame.headerHeight + 10) / 1080d * mainFrame.getHeight()));

        fillComboBox(secondCrit, secondCrits);

        secondCrit.setSelectedItem("Name des Buches");

        secondCrit.addItemListener(e -> {
            mainFrame.isHidden.clear();
            mainFrame.editIsOpen.clear();

            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }
            mainFrame.skipInit = true;

            if (secondCrit.getSelectedItem() != null) {
                switch (secondCrit.getSelectedItem().toString()) {
                    case "Name des Buches":
                        mainFrame.sortedBySecond = MainFrame.BY_NAME;
                        break;

                    case "Vorname des Autors":
                        mainFrame.sortedBySecond = MainFrame.BY_AUTHOR_FIRST_NAME;
                        break;

                    case "Nachname des Autors":
                        mainFrame.sortedBySecond = MainFrame.BY_AUTHOR_LAST_NAME;
                        break;

                    case "Seitenzahl steigend":
                        mainFrame.sortedBySecond = MainFrame.BY_PAGES_ASCENDING;
                        break;

                    case "Seitenzahl fallend":
                        mainFrame.sortedBySecond = MainFrame.BY_PAGES_DESCENDING;
                        break;

                    case "Spannung":
                        mainFrame.sortedBySecond = MainFrame.BY_TENSION;
                        break;

                    case "Lesespaß":
                        mainFrame.sortedBySecond = MainFrame.BY_FUN;
                        break;

                    case "Mehrwert":
                        mainFrame.sortedBySecond = MainFrame.BY_INTEREST;
                        break;

                    case "Geschichte":
                        mainFrame.sortedBySecond = MainFrame.BY_STORY;
                        break;

                    case "Bewertung insgesamt":
                        mainFrame.sortedBySecond = MainFrame.BY_TOTAL_RAT;
                        break;

                    case "Lesedatum":
                        mainFrame.sortedBySecond = MainFrame.BY_READING_DATE;
                        break;

                    case "Sprachbewertung":
                        mainFrame.sortedBySecond = MainFrame.BY_LANGUAGE_RAT;
                        break;
                }
            }
            mainFrame.skipInit = false;
            mainFrame.init();
        });
        return secondCrit;
    }

    private void fillComboBox(JComboBox<String> boxToFill, ArrayList<String> filler) {
        boxToFill.removeAllItems();
        for (String str : filler) {
            boxToFill.addItem(str);
        }
    }
}
