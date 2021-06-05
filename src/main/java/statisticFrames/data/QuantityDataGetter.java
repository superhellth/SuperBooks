package statisticFrames.data;

import bookManagement.Book;
import bookManagement.BookStatistics;
import calendar.MyCalendar;
import main.MainFrame;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.*;

public class QuantityDataGetter {


    private BookStatistics bookStatistics;
    private int startingYear;
    private int endingYear;
    private ArrayList<Book> books;
    private MainFrame mainFrame;
    private String rowKey;
    private boolean yearOrMonth;
    private boolean bookOrPage;
    private boolean hideZeroMonths;

    public QuantityDataGetter(MainFrame mainFrame, String rowKey, boolean yearOrMonth, boolean bookOrPage, int startingYear, int endingYear,
                              boolean hideZeroMonths) {
        this.mainFrame = mainFrame;
        this.rowKey = rowKey;
        this.yearOrMonth = yearOrMonth;
        this.bookOrPage = bookOrPage;
        this.startingYear = startingYear;
        this.endingYear = endingYear;
        books = mainFrame.allBooks;
        bookStatistics = new BookStatistics(mainFrame);
        this.hideZeroMonths = !hideZeroMonths;
    }

    public DefaultCategoryDataset getData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<String> xValues = getXValues();
        List<Integer> yValues = getYValues();

        for (int i = 0; i < xValues.size(); i++) {
            dataset.addValue(yValues.get(i), rowKey, xValues.get(i));
        }

        return dataset;
    }

    private List<String> getXValues() {
        List<String> xValues = new ArrayList<>();
        List<Integer> relevantYears = getYearsBetween(startingYear, endingYear);

        if (yearOrMonth) {
            for (int year : relevantYears) {
                if (year == 0) {
                    xValues.add("Früher");
                } else {
                    xValues.add(year + "");
                }
            }
        } else {
            for (int year : relevantYears) {
                for (int month : getRelevantMonthIn(year)) {
                    String yearString;
                    if (year != 0) {
                        yearString = Integer.toString(year);
                        yearString = yearString.substring(2, 4);
                    } else {
                        yearString = "Früher";
                    }
                    xValues.add(MyCalendar.getMonthStrings().get(month).substring(0, 3) + " " + yearString);
                }
            }
        }

        return xValues;
    }

    private List<Integer> getYValues() {
        List<Integer> yValues = new ArrayList<>();

        if (yearOrMonth) {
            for (int year : getYearsBetween(startingYear, endingYear)) {
                if (bookOrPage) {
                    yValues.add(bookStatistics.getNumBooksRead(year));
                } else {
                    yValues.add(bookStatistics.getNumPagesRead(year));
                }
            }
        } else {
            for (int year : getYearsBetween(startingYear, endingYear)) {
                for (int month : getRelevantMonthIn(year)) {
                    if (bookOrPage) {
                        yValues.add(bookStatistics.getNumBooksRead(year, month));
                    } else {
                        yValues.add(bookStatistics.getNumPagesRead(year, month));
                    }
                }
            }
        }

        return yValues;
    }

    private List<Integer> getRelevantMonthIn(List<Integer> relevantYears) {
        List<Integer> months = new ArrayList<>();

        for (int year : relevantYears) {
            if (hideZeroMonths) {
                months.addAll(mainFrame.getReadingMonths(year));
            } else {
                for (int i = 1; i < 13; i++) {
                    months.add(i);
                }
            }
        }

        return months;
    }

    private List<Integer> getRelevantMonthIn(int relevantYear) {
        List<Integer> months = new ArrayList<>();

        if (hideZeroMonths) {
            months.addAll(mainFrame.getReadingMonths(relevantYear));
        } else {
            for (int i = 1; i < 13; i++) {
                months.add(i);
            }
        }

        return months;
    }

    private List<Integer> getYearsBetween(int yearA, int yearB) {
        Set<Integer> yearSet = new TreeSet<>();

        int lowerYear;
        int higherYear;
        if (yearA < yearB) {
            lowerYear = yearA;
            higherYear = yearB;
        } else if (yearA == yearB) {
            lowerYear = yearA;
            higherYear = yearA;
        } else {
            lowerYear = yearB;
            higherYear = yearA;
        }

        do {
            if (mainFrame.getReadingYears().contains(lowerYear)) {
                yearSet.add(lowerYear);
            }
            lowerYear += 1;
        } while (lowerYear <= higherYear);

        List<Integer> yearList = new ArrayList<>(yearSet);

        if (yearA > yearB) {
            Collections.reverse(yearList);
        }

        return yearList;
    }
}

