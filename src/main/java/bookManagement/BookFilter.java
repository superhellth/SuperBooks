package bookManagement;

import main.MainFrame;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class BookFilter {

    private ArrayList<Book> booksToFilter;
    private MainFrame mainFrame;

    public BookFilter(MainFrame mainFrame, ArrayList<Book> booksToFilter) {
        this.mainFrame = mainFrame;
        this.booksToFilter = booksToFilter;
    }

    public void setBooksToFilter(ArrayList<Book> booksToFilter) {
        this.booksToFilter = booksToFilter;
    }

    public ArrayList<Book> getBooksOfAuthor(String author) {
        ArrayList<Book> booksOfAuthor = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                booksOfAuthor.add(book);
            }
        }

        return booksOfAuthor;
    }

    public ArrayList<String> getSubgenresOfAGenre(Genre headgenre) {
        Set<String> subgenreSet = new TreeSet<>();

        for (Subgenre subgenre : mainFrame.dbReader.getSubgenres()) {
            if (subgenre.getGenre().equalsIgnoreCase(headgenre.getName())) {
                subgenreSet.add(subgenre.getSubgenre());
            }
        }

        return new ArrayList<>(subgenreSet);
    }

    public ArrayList<Book> getBookOfPagesBetween(int lowerBound, int upperBound) {
        ArrayList<Book> booksWithPages = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getNumPages() >= lowerBound && book.getNumPages() < upperBound) {
                booksWithPages.add(book);
            }
        }

        return booksWithPages;
    }

    public ArrayList<Book> getBooksOfRatBetween(int lowerBound, int upperBound) {
        ArrayList<Book> booksWithRat = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getTotalRat() >= lowerBound && book.getTotalRat() <= upperBound) {
                if (book.getTotalRat() == 100 && upperBound == 109) {
                    continue;
                }
                booksWithRat.add(book);
            }
        }

        return booksWithRat;
    }

    public ArrayList<Book> getBooksOfRat(int ratType, int ratValue) {
        ArrayList<Book> booksOfRat = new ArrayList<>();

        for (Book book : booksToFilter) {
            switch (ratType) {
                case 7:
                    if (book.getTensionRat() == ratValue) {
                        booksOfRat.add(book);
                    }
                    break;
                case 8:
                if (book.getStoryRat() == ratValue) {
                    booksOfRat.add(book);
                }
                break;
                case 9:
                    if (book.getFunRat() == ratValue) {
                        booksOfRat.add(book);
                    }
                    break;
                case 10:
                    if (book.getInterestRat() == ratValue) {
                        booksOfRat.add(book);
                    }
                    break;
                case 14:
                    if (book.getLanguageRat() == ratValue) {
                        booksOfRat.add(book);
                    }
            }
        }

        return booksOfRat;
    }

    public ArrayList<Book> getBooksWithMorePagesThan(int numPages) {
        ArrayList<Book> booksWithMorePages = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getNumPages() > numPages) {
                booksWithMorePages.add(book);
            }
        }

        return booksWithMorePages;
    }

    public ArrayList<Book> getBooksOfLanguage(String language) {
        ArrayList<Book> booksOfLanguage = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getLanguage().equalsIgnoreCase(language)) {
                booksOfLanguage.add(book);
            }
        }

        return booksOfLanguage;
    }

    public ArrayList<Book> getBooksOfPubComp(String pubComp) {
        ArrayList<Book> booksOfPubComp = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getPubComp().equalsIgnoreCase(pubComp)) {
                booksOfPubComp.add(book);
            }
        }

        return booksOfPubComp;
    }

    public ArrayList<Book> getBooksWithStartingLetter(String letter) {
        ArrayList<Book> booksWithStartingLetter = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getName().toLowerCase().startsWith(letter.toLowerCase())) {
                booksWithStartingLetter.add(book);
            }
        }

        return booksWithStartingLetter;
    }

    public ArrayList<Book> getBooksOfSubgenre(Subgenre subgenre) {
        ArrayList<Book> booksOfSubgenre = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getSubgenre().getGenre().equalsIgnoreCase(subgenre.getGenre())
                    && book.getSubgenre().getSubgenre().equalsIgnoreCase(subgenre.getSubgenre())) {
                booksOfSubgenre.add(book);
            }
        }

        return booksOfSubgenre;
    }
    public ArrayList<Book> getBooksOfGenre(Genre genre) {
        ArrayList<Book> booksOfGenre = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getSubgenre().getGenre().equalsIgnoreCase(genre.getName())) {
                booksOfGenre.add(book);
            }
        }

        return booksOfGenre;
    }

    public ArrayList<Book> getBooksFromMonth(int year, int month) {
        ArrayList<Book> booksOfMonth = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getReadingMonth() == month && book.getReadingYear() == year) {
                booksOfMonth.add(book);
            }
        }

        return booksOfMonth;
    }

    public ArrayList<Book> getBooksFromYear(int year) {
        ArrayList<Book> booksOfYear = new ArrayList<>();

        for (Book book : booksToFilter) {
            if (book.getReadingYear() == year) {
                booksOfYear.add(book);
            }
        }

        return booksOfYear;
    }

}
