package bookManagement;

import main.MainFrame;

import java.util.ArrayList;
import java.util.Comparator;

public class BookSorter {

    public ArrayList<Book> sortList(ArrayList<Book> list, int sortBy) {
        BookSorter bookSorter = new BookSorter();
        switch (sortBy) {
            default:
                throw new RuntimeException();
            case 1:
                list = bookSorter.sortByBookName(list);
                break;
            case 2:
                list = bookSorter.sortByAuthor(list, MainFrame.BY_AUTHOR_FIRST_NAME);
                break;
            case 3:
                list = bookSorter.sortByAuthor(list, MainFrame.BY_AUTHOR_LAST_NAME);
                break;
            case 5:
                list = bookSorter.sortByPages(list, MainFrame.BY_PAGES_ASCENDING);
                break;
            case 6:
                list = bookSorter.sortByPages(list, MainFrame.BY_PAGES_DESCENDING);
                break;
            case 7:
                list = bookSorter.sortByRat(list, MainFrame.BY_TENSION);
                break;
            case 8:
                list = bookSorter.sortByRat(list, MainFrame.BY_STORY);
                break;
            case 9:
                list = bookSorter.sortByRat(list, MainFrame.BY_FUN);
                break;
            case 10:
                list = bookSorter.sortByRat(list, MainFrame.BY_INTEREST);
                break;
            case 12:
                list = bookSorter.sortByReadingDate(list);
                break;
            case 11:
                list = bookSorter.sortByRat(list, MainFrame.BY_TOTAL_RAT);
                break;
        }

        return list;
    }

    public ArrayList<Book> sortByPages(ArrayList<Book> books, int scending) {
        ArrayList<Book> sortedList = new ArrayList<>(books);
        Comparator<Book> stringComparator = (book1, book2) -> {
            int result = book1.getNumPages() - book2.getNumPages();
            if (scending == 6) {
                result = -result;
            }
            return result;
        };

        sortedList.sort(stringComparator);

        return sortedList;
    }

    public ArrayList<Book> sortByAuthor(ArrayList<Book> books, int nameKind) {
        ArrayList<Book> sortedList = new ArrayList<>(books);
        Comparator<Book> stringComparator = (book1, book2) -> {
            if (nameKind == 2) {
                return book1.getAuthor().compareTo(book2.getAuthor());
            }
            return book1.getAuthorLastName().compareTo(book2.getAuthorLastName());
        };

        sortedList.sort(stringComparator);

        return sortedList;
    }

    public ArrayList<Book> sortByBookName(ArrayList<Book> books) {
        ArrayList<Book> sortedList = new ArrayList<>(books);
        Comparator<Book> nameComparator = Comparator.comparing(book1 -> book1.getName().toLowerCase().replaceAll(" ", ""));

        sortedList.sort(nameComparator);

        return sortedList;
    }

    public ArrayList<Book> sortByReadingDate(ArrayList<Book> books) {
        Comparator<Book> dateComparator = (book1, book2) -> {
            int result = book1.getReadingYear() - book2.getReadingYear();
            if (result == 0) {
                result = book1.getReadingMonth() - book2.getReadingMonth();
            }

            return -result;
        };

        books.sort(dateComparator);
        return books;
    }

    public ArrayList<Book> sortByRat(ArrayList<Book> books, int ratAspect) {
        Comparator<Book> stringComparator = (book1, book2) -> {
            int result = 0;
            switch (ratAspect) {
                case 7:
                    result = book1.getTensionRat() - book2.getTensionRat();
                    break;
                case 8:
                    result = book1.getInterestRat() - book2.getInterestRat();
                    break;
                case 9:
                    result = book1.getFunRat() - book2.getFunRat();
                    break;
                case 10:
                    result = book1.getStoryRat() - book2.getStoryRat();
                    break;
                case 11:
                    result = book1.getTotalRat() - book2.getTotalRat();
                    break;
            }
            return -result;
        };

        books.sort(stringComparator);

        return books;
    }

}
