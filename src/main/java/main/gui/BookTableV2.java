package main.gui;

import bookManagement.Book;
import bookManagement.Genre;
import bookManagement.Subgenre;
import calendar.MyCalendar;
import gui.BookPanel;
import gui.EditPanel;
import gui.SeparationPanel;
import main.MainFrame;
import sorting.AlphabetSorter;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BookTableV2 {

    private MainFrame mainFrame;
    private JPanel parentPanel;
    private Map<String, BookPanel> bookPanels;
    private Map<String, EditPanel> editPanels;

    public BookTableV2(MainFrame mainFrame, JPanel parentPanel) {
        this.mainFrame = mainFrame;
        this.parentPanel = parentPanel;
        bookPanels = mainFrame.allBookPanels;
        editPanels = mainFrame.allEditPanels;
    }

    public void createTableByGenre() {
        for (Genre genre : mainFrame.getGenresWBooks()) {
            SeparationPanel gPanel = createBigSepPanel(genre.getName());

            if (mainFrame.isHidden(gPanel.getKey())) {
                continue;
            }

            ArrayList<String> subgenres = mainFrame.bookFilter.getSubgenresOfAGenre(genre);
            List<Subgenre> toRemove = new ArrayList<>();
            for (String subgenreString : subgenres) {
                Subgenre aSubgenre = new Subgenre(genre.getName(), subgenreString);
                if (!mainFrame.hasBooks(aSubgenre)) {
                    toRemove.add(aSubgenre);
                }
            }
            for (Subgenre subgenre : toRemove) {
                subgenres.remove(subgenre.getSubgenre());
            }

            for (String subgenre : subgenres) {
                SeparationPanel sgPanel = createSmallSepPanel(subgenre);
                sgPanel.addRenameSubgenreButton(mainFrame, genre.getName(), subgenre);
                sgPanel.addDeleteGenreButton(mainFrame, new Subgenre(genre.getName(), subgenre));

                if (mainFrame.isHidden(sgPanel.getKey())) {
                    continue;
                }

                Subgenre currentSubgenre = new Subgenre(genre.getName(), subgenre);
                ArrayList<Book> booksOfSubg = mainFrame.bookSorter.sortList(
                        mainFrame.bookFilter.getBooksOfSubgenre(currentSubgenre), mainFrame.sortedBySecond);
                addBooks(booksOfSubg);
            }
        }
    }

    public void createTabelByReadingDate() {
        for (int year : mainFrame.getReadingYears()) {
            String title = year + "";
            if (year == 0) {
                title = "Unbekanntes Jahr";
            }
            SeparationPanel yearPanel = createBigSepPanel(title);

            if (mainFrame.isHidden(yearPanel.getKey())) {
                continue;
            }

            Set<Integer> months = mainFrame.getReadingMonths(year);
            List<Integer> realMonth = new ArrayList<>(months);
            boolean thirteenRemoved = false;
            if (realMonth.contains(13)) {
                realMonth.remove(new Integer(13));
                thirteenRemoved = true;
            }
            Collections.reverse(realMonth);
            if (thirteenRemoved) {
                realMonth.add(13);
            }

            for (int month : realMonth) {
                String monthString = MyCalendar.getMonthStrings().get(month);
                SeparationPanel monthPanel = createSmallSepPanel(monthString);

                if (mainFrame.isHidden(monthPanel.getKey())) {
                    continue;
                }

                ArrayList<Book> books = mainFrame.bookSorter.sortList(
                        mainFrame.bookFilter.getBooksFromMonth(year, month), mainFrame.sortedBySecond);
                addBooks(books);
            }
        }
    }

    public void createTableByTotalRat() {
        Map<Integer, Integer> sections = new TreeMap<>(mainFrame.getUsedSecs());
        ArrayList<Integer> sectionKeys = new ArrayList<>(sections.keySet());
        Collections.reverse(sectionKeys);
        for (int i : sectionKeys) {
            int upperBound = sections.get(i) - 1;
            int lowerBound = upperBound - 9;
            if (upperBound == 100) {
                lowerBound = 90;
            }

            String title = lowerBound + " - " + (upperBound) + " %";
            SeparationPanel ratPanel = createBigSepPanel(title);

            if (mainFrame.isHidden(ratPanel.getKey())) {
                continue;
            }

            ArrayList<Book> books = mainFrame.bookSorter.sortList(
                    mainFrame.bookFilter.getBooksOfRatBetween(lowerBound, upperBound), MainFrame.BY_TOTAL_RAT);
            addBooks(books);
        }
    }

    public void createTableByName() {
        for (String letter : mainFrame.getStartingLetters()) {
            SeparationPanel letterPanel = createBigSepPanel(letter);

            if (mainFrame.isHidden(letterPanel.getKey())) {
                continue;
            }

            ArrayList<Book> books = mainFrame.bookSorter.sortByBookName(
                    mainFrame.bookFilter.getBooksWithStartingLetter(letter));
            addBooks(books);
        }
    }

    public void createTableByAuthorFirst() {
        for (String author : mainFrame.dbReader.getAuthors()) {
            SeparationPanel authorPanel = createBigSepPanel(author);

            if (mainFrame.isHidden(authorPanel.getKey())) {
                continue;
            }

            ArrayList<Book> booksOfAuthor = mainFrame.bookSorter.sortList(
                    mainFrame.bookFilter.getBooksOfAuthor(author), mainFrame.sortedBySecond);
            addBooks(booksOfAuthor);
        }
    }

    public void createTabelByAuthorLast() {
        for (String author : AlphabetSorter.sortByLastWord(mainFrame.dbReader.getAuthors())) {
            SeparationPanel authorPanel = createBigSepPanel(author);

            if (mainFrame.isHidden(authorPanel.getKey())) {
                continue;
            }

            ArrayList<Book> booksOfAuthor = mainFrame.bookSorter.sortList(
                    mainFrame.bookFilter.getBooksOfAuthor(author), mainFrame.sortedBySecond);
            addBooks(booksOfAuthor);
        }
    }

    public void createTableByPubComp() {
        ArrayList<String> pubComps = mainFrame.dbReader.getPubComps();
        AlphabetSorter.sortAlphabetically(pubComps);
        for (String pubComp : pubComps) {
            SeparationPanel pubCompPanel = createBigSepPanel(pubComp);

            if (mainFrame.isHidden(pubCompPanel.getKey())) {
                continue;
            }

            ArrayList<Book> booksOfPubComp = mainFrame.bookSorter.sortList(
                    mainFrame.bookFilter.getBooksOfPubComp(pubComp), mainFrame.sortedBySecond);
            addBooks(booksOfPubComp);
        }
    }

    public void createTableByPagesAs() {
        for (int i = 0; i < mainFrame.pageSections.size() - 1; i++) {
            int upperBound = mainFrame.pageSections.get(i + 1);
            String title = mainFrame.pageSections.get(i) + " - " + (upperBound - 1);
            if (upperBound == Integer.MAX_VALUE) {
                title = mainFrame.pageSections.get(i) + "+";
            }
            SeparationPanel pagePanel = createBigSepPanel(title);

            if (mainFrame.isHidden(pagePanel.getKey())) {
                continue;
            }

            ArrayList<Book> books = mainFrame.bookSorter.sortByPages(
                    mainFrame.bookFilter.getBookOfPagesBetween(mainFrame.pageSections.get(i),
                            mainFrame.pageSections.get(i + 1)), MainFrame.BY_PAGES_ASCENDING);
            addBooks(books);
        }
    }

    public void createTableByPagesDes() {
        for (int i = mainFrame.pageSections.size() - 1; i > 0; i--) {
            int upperBound = mainFrame.pageSections.get(i);
            String title = mainFrame.pageSections.get(i - 1) + " - " + (upperBound - 1);
            if (upperBound == Integer.MAX_VALUE) {
                title = mainFrame.pageSections.get(i - 1) + "+";
            }
            SeparationPanel pagePanel = createBigSepPanel(title);

            if (mainFrame.isHidden(pagePanel.getKey())) {
                continue;
            }

            ArrayList<Book> books = mainFrame.bookSorter.sortByPages(
                    mainFrame.bookFilter.getBookOfPagesBetween(mainFrame.pageSections.get(i - 1),
                            mainFrame.pageSections.get(i)), MainFrame.BY_PAGES_DESCENDING);
            addBooks(books);
        }
    }

    public void createTableByRat(int ratType) {
        ArrayList<Integer> usedRats;
        switch (ratType) {
            case MainFrame.BY_TENSION:
                usedRats = new ArrayList<>(mainFrame.getRatsWBooks(MainFrame.BY_TENSION));
                break;
            case MainFrame.BY_FUN:
                usedRats = new ArrayList<>(mainFrame.getRatsWBooks(MainFrame.BY_FUN));
                break;
            case MainFrame.BY_INTEREST:
                usedRats = new ArrayList<>(mainFrame.getRatsWBooks(MainFrame.BY_INTEREST));
                break;
            case MainFrame.BY_STORY:
                usedRats = new ArrayList<>(mainFrame.getRatsWBooks(MainFrame.BY_STORY));
                break;
            case MainFrame.BY_LANGUAGE_RAT:
                usedRats = new ArrayList<>(mainFrame.getRatsWBooks(MainFrame.BY_LANGUAGE_RAT));
                break;
            default:
                throw new RuntimeException();
        }
        Collections.reverse(usedRats);
        if (usedRats.contains(11)) {
            usedRats.remove(new Integer(11));
            usedRats.add(usedRats.size(), 11);
        }
        for (int ratWBooks : usedRats) {
            String title;
            if (ratWBooks == 11) {
                title = "Irrelevant";
            } else {
                title = Integer.toString(ratWBooks);
            }
            SeparationPanel ratPanel = createBigSepPanel(title);

            if (mainFrame.isHidden(ratPanel.getKey())) {
                continue;
            }

            ArrayList<Book> books = mainFrame.bookSorter.sortList(
                    mainFrame.bookFilter.getBooksOfRat(ratType,
                            ratWBooks), mainFrame.sortedBySecond);
            addBooks(books);
        }
    }

    private void addBooks(ArrayList<Book> books) {
        for (Book book : books) {
            parentPanel.add(bookPanels.get(book.getKey()));
            mainFrame.numBooksAdded++;

            if (mainFrame.editIsOpen(book.getKey())) {
                parentPanel.add(editPanels.get(book.getKey()));
                mainFrame.numExtraPanels++;
            }
        }
        resizePanel(parentPanel);
    }

    private SeparationPanel createSmallSepPanel(String title) {
        SeparationPanel sepPanel = new SeparationPanel(mainFrame, title, mainFrame.smallPanelHeight,
                mainFrame.secondaryColor);

        parentPanel.add(sepPanel);
        mainFrame.numSmallLabelsAdded++;

        return sepPanel;
    }

    private SeparationPanel createBigSepPanel(String title) {
        SeparationPanel sepPanel = new SeparationPanel(mainFrame, title, mainFrame.bigPanelHeight,
                mainFrame.primaryColor);

        parentPanel.add(sepPanel);
        mainFrame.numBigLabelsAdded++;

        return sepPanel;
    }

    private void resizePanel(JPanel panel) {
        panel.setPreferredSize(new Dimension(mainFrame.getWidth() - mainFrame.scrollPane.getVerticalScrollBar().getWidth(),
                calculateCurrentHeight()));
        panel.setMaximumSize(new Dimension(mainFrame.getWidth() - mainFrame.scrollPane.getVerticalScrollBar().getWidth(),
                calculateCurrentHeight()));
    }

    private int calculateCurrentHeight() {
        int bookPanelHeight = 250;
        int extraPanelHeight = 400;
        return (mainFrame.numBigLabelsAdded * mainFrame.bigPanelHeight) + (mainFrame.numSmallLabelsAdded * mainFrame.smallPanelHeight)
                + (mainFrame.numBooksAdded * bookPanelHeight) + (mainFrame.numExtraPanels * extraPanelHeight);
    }

    private int getID() {
        return mainFrame.currentID++;
    }

}
