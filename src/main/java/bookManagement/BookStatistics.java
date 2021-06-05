package bookManagement;

import calendar.Date;
import calendar.MyCalendar;
import main.MainFrame;

import java.util.*;

public class BookStatistics {

    private MainFrame mainFrame;

    public BookStatistics(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public int getNumBooksRead(int year) {
        return mainFrame.bookFilter.getBooksFromYear(year).size();
    }

    public int getNumBooksRead(int year, int month) {
        return mainFrame.bookFilter.getBooksFromMonth(year, month).size();
    }

    public int getNumPagesRead(int year) {
        return getNumPagesRead(mainFrame.bookFilter.getBooksFromYear(year));
    }

    public int getNumPagesRead(int year, int month) {
        return getNumPagesRead(mainFrame.bookFilter.getBooksFromMonth(year, month));
    }

    public int getTotalNumBooksRead() {
        return mainFrame.dbReader.getBooks().size();
    }

    private int getNumBooksWFullDate() {
        int numBooks = 0;
        for (Book book : mainFrame.allBooks) {
            if (!book.getReadingDate().contains("X")) {
                numBooks += 1;
            }
        }

        return numBooks;
    }

    private int getNumBooksWYear() {
        int numBooks = 0;
        for (Book book : mainFrame.allBooks) {
            if (book.getReadingYear() != 0) {
                numBooks += 1;
            }
        }

        return numBooks;
    }

    public int getTotalNumPagesRead() {
        int numPages = 0;
        for (Book book : mainFrame.allBooks) {
            numPages += book.getNumPages();
        }
        return numPages;
    }

    private int getFullYearsSinceFirstBook() {
        return Calendar.getInstance().get(Calendar.YEAR) - getEarliestRYear();
    }

    private double getMonthsSinceFirstBook() {
        int yearsSinceFirstBook = getFullYearsSinceFirstBook();
        int monthDifference = Calendar.getInstance().get(Calendar.MONTH) + 1 - getEarliestRMonth();
        double month = yearsSinceFirstBook * 12 + monthDifference;
        month += getMonthPortion();

        return month;
    }

    private double getMonthPortion() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH) / (double) MyCalendar.getDaysOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
    }

    private double getMonthsWFullDate() {
        Set<Integer> months = new TreeSet<>();
        double numMonth = 0;

        for (Book book : mainFrame.allBooks) {
            if (!book.getReadingDate().contains("X")) {
                months.add(book.getReadingMonth());
            }
        }
        numMonth += months.size();
        if (months.contains(Calendar.getInstance().get(Calendar.MONTH) + 1)) {
            numMonth--;
            numMonth += getMonthPortion();
        }

        return numMonth;
    }

    public double getAverageBooksPerYear() {
        double yearDifference = Calendar.getInstance().get(Calendar.YEAR) - (getEarliestYear());
        yearDifference += getMonthsSinceFirstBook() / 12;

        double avgNum = (double) getNumBooksWYear() / yearDifference;

        avgNum = Math.round(avgNum * 100) / 100.0;
        return avgNum;
    }

    public double getAverageBooksPerMonth() {
        double monthSinceFirstBook = getMonthsWFullDate();

        double avgNum = (double) getNumBooksWFullDate() / monthSinceFirstBook;
        avgNum = Math.round(avgNum * 100) / 100.0;
        return avgNum;
    }

    public double getAveragePagesPerMonth() {
        double monthSinceFirstBook = getMonthsSinceFirstBook();

        double avgNum = (double) getNumPagesRead(getBooksSince(getEarliestRDate())) / monthSinceFirstBook;
        avgNum = Math.round(avgNum * 100) / 100.0;
        return avgNum;
    }

    public double getAveragePagesPerDay() {
        int daysSinceFirstBook = MyCalendar.getDaysSince(new Date(getEarliestRDate(), true));

        double avgNum = (double) getNumPagesRead(getBooksSince(getEarliestRDate())) / daysSinceFirstBook;
        avgNum = Math.round(avgNum * 100) / 100.0;
        return avgNum;
    }

    public double getPercentOfGenre(Genre genre) {
        int totalNumBooks = mainFrame.allBooks.size();
        int numBooksOfGenre = mainFrame.bookFilter.getBooksOfGenre(genre).size();

        double avgNum = (double) numBooksOfGenre / totalNumBooks * 100;
        avgNum = Math.round(avgNum * 100) / 100.0;
        return avgNum;
    }

    public double getPercentOfBooksWithMorePagesThan(int numPages) {
        int totalNumBooks = mainFrame.allBooks.size();
        int numBooksWithMorePages = mainFrame.bookFilter.getBooksWithMorePagesThan(numPages).size();

        double avgNum = (double) numBooksWithMorePages / totalNumBooks * 100;
        avgNum = Math.round(avgNum * 100) / 100.0;
        return avgNum;
    }

    public double getPercentOfLanguage(String language) {
        int totalNumBooks = mainFrame.allBooks.size();
        int numBooksOfLanguage = mainFrame.bookFilter.getBooksOfLanguage(language).size();

        double avgNum = (double) numBooksOfLanguage / totalNumBooks * 100;
        avgNum = Math.round(avgNum * 100) / 100.0;
        return avgNum;
    }

    public int getNumGenres() {
        int numGenres = 0;
        List<String> genres = new ArrayList<>();
        for (Genre genre : mainFrame.dbReader.getGenres()) {
            genres.add(genre.getName());
        }
        for (String genreString : genres) {
            for (String subgenre : mainFrame.bookFilter.getSubgenresOfAGenre(new Genre(genreString))) {
                numGenres += 1;
            }
        }

        return numGenres;
    }

    private ArrayList<Book> getBooksSince(String earliestRDate) {
        StringTokenizer tok = new StringTokenizer(earliestRDate, ".");
        int month = Integer.parseInt(tok.nextToken());
        int year = Integer.parseInt(tok.nextToken());
        ArrayList<Book> books = new ArrayList<>();
        for (Book book : mainFrame.allBooks) {
            int monthOfReading = book.getReadingMonth();
            if (book.getReadingMonth() == 0) {
                monthOfReading = 0;
            }
            if (book.getReadingYear() > year) {
                books.add(book);
            } else if (book.getReadingYear() == year) {
                if (monthOfReading >= month) {
                    books.add(book);
                }
            }
        }

        return books;
    }

    private int getNumPagesRead(ArrayList<Book> books) {
        int numPages = 0;

        for (Book book : books) {
            numPages += book.getNumPages();
        }

        return numPages;
    }

    public Subgenre getFavSubgenre() {
        Map<Subgenre, Integer> booksPerSubgenre = new HashMap<>();
        for (Subgenre subgenre : mainFrame.dbReader.getSubgenres()) {
            booksPerSubgenre.put(subgenre, mainFrame.bookFilter.getBooksOfSubgenre(subgenre).size());
        }
        int maxBooks = 0;
        Subgenre favSubgenre = new Subgenre("Fehler", "Fehler");
        for (Subgenre subgenre : booksPerSubgenre.keySet()) {
            if (booksPerSubgenre.get(subgenre) > maxBooks) {
                maxBooks = booksPerSubgenre.get(subgenre);
                favSubgenre = subgenre;
            }
        }

        return favSubgenre;
    }

    public String getFavAuthor() {
        String favAuthor = "";
        for (String author : mainFrame.dbReader.getAuthors()) {
            if (mainFrame.bookFilter.getBooksOfAuthor(favAuthor).size() < mainFrame.bookFilter.getBooksOfAuthor(author).size()) {
                favAuthor = author;
            }
        }

        return favAuthor;
    }

    public Book getFavBook() {
        Book favBook = new Book();
        favBook.setFunRat(0);
        favBook.setStoryRat(0);
        favBook.setInterestRat(0);
        favBook.setTensionRat(0);
        for (Book book : mainFrame.allBooks) {
            if (book.getTotalRat() > favBook.getTotalRat()) {
                favBook = book;
            } else if (book.getTotalRat() == favBook.getTotalRat()) {
                if (book.getFunRat() > favBook.getFunRat()) {
                    favBook = book;
                } else if (book.getFunRat() == favBook.getFunRat()) {
                    if (book.getNumPages() > favBook.getNumPages()) {
                        favBook = book;
                    }
                }
            }
        }

        return favBook;
    }

    public Subgenre getBestRatSubgenre() {
        Map<Subgenre, Double> ratOfSubgenre = new HashMap<>();
        for (Subgenre subgenre : mainFrame.dbReader.getSubgenres()) {
            double avgRat = getAvgRat(subgenre);
            ratOfSubgenre.put(subgenre, avgRat);
        }

        double maxRat = 0.0;
        Subgenre bestSubgenre = new Subgenre("Fehler", "Fehler");
        for (Subgenre subgenre : ratOfSubgenre.keySet()) {
            if (ratOfSubgenre.get(subgenre) > maxRat) {
                maxRat = ratOfSubgenre.get(subgenre);
                bestSubgenre = subgenre;
            }
        }
        return bestSubgenre;
    }

    public double getAvgRat(Subgenre subgenre) {
        int totalRat = 0;
        for (Book book : mainFrame.bookFilter.getBooksOfSubgenre(subgenre)) {
            totalRat += book.getTotalRat();
        }

        double avgRat = (double) totalRat / mainFrame.bookFilter.getBooksOfSubgenre(subgenre).size();

        avgRat = Math.round(avgRat * 100) / 100.0;
        return avgRat;
    }

    private int getEarliestRMonth() {
        StringTokenizer tokenizer = new StringTokenizer(getEarliestRDate(), ".");
        return Integer.parseInt(tokenizer.nextToken());
    }

    private int getEarliestRYear() {
        StringTokenizer tokenizer = new StringTokenizer(getEarliestRDate(), ".");
        tokenizer.nextToken();
        return Integer.parseInt(tokenizer.nextToken());
    }

    private int getEarliestYear() {
        int year = 9999;
        for (Book book : mainFrame.allBooks) {
            if (!(book.getReadingYear() == 0)) {
                if (book.getReadingYear() < year) {
                    year = book.getReadingYear();
                }
            }
        }

        return year;
    }

    private String getEarliestRDate() {
        String date = "12.9000";
        StringTokenizer tokenizer;
        for (Book book : mainFrame.allBooks) {
            if (!book.getReadingDate().contains("X")) {
                tokenizer = new StringTokenizer(date, ".");
                int currentMonth = Integer.parseInt(tokenizer.nextToken());
                int currentYear = Integer.parseInt(tokenizer.nextToken());
                if (book.getReadingYear() <= currentYear && book.getReadingMonth() < currentMonth) {
                    date = book.getReadingDate();
                }
            }
        }
        //System.out.println("EarliestRDate: " + date);

        return date;
    }
}
