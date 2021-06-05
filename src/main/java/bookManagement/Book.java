package bookManagement;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Book {

    private String name;
    private String author;
    private String secondAuthor;
    private int numPages;
    private Subgenre subgenre;
    private int tensionRat;
    private int interestRat;
    private int storyRat;
    private int funRat;
    private int languageRat;
    private String language;
    private String pubComp;
    private String readingDate;
    private boolean recommended;
    private String key;

    public void setKey(String key) {
        this.key = key;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setSecondAuthor(String secondAuthor) {
        this.secondAuthor = secondAuthor;
    }
    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }
    public void setSubgenre(Subgenre subgenre) {
        this.subgenre = subgenre;
    }
    public void setTensionRat(int tensionRat) {
        this.tensionRat = tensionRat;
    }
    public void setInterestRat(int interestRat) {
        this.interestRat = interestRat;
    }
    public void setStoryRat(int storyRat) {
        this.storyRat = storyRat;
    }
    public void setFunRat(int funRat) {
        this.funRat = funRat;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public void setPubComp(String pubComp) {
        this.pubComp = pubComp;
    }
    public void setReadingDate(String readingDate) {
        this.readingDate = readingDate;
    }
    public void setLanguageRat(int languageRat) {
        this.languageRat = languageRat;
    }
    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public String getKey() {
        return key;
    }
    public String getName() {
        return name;
    }
    public String getAuthor() {
        return author;
    }
    public String getSecondAuthor() {
        return secondAuthor;
    }
    public int getNumPages() {
        return numPages;
    }
    public Subgenre getSubgenre() {
        return subgenre;
    }
    public int getTensionRat() {
        return tensionRat;
    }
    public int getInterestRat() {
        return interestRat;
    }
    public int getStoryRat() {
        return storyRat;
    }
    public int getFunRat() {
        return funRat;
    }
    public String getLanguage() {
        return language;
    }
    public String getPubComp() {
        return pubComp;
    }
    public String getReadingDate() {
        return readingDate;
    }
    public int getLanguageRat() {
        return languageRat;
    }
    public int getReadingYear() {
        StringTokenizer tokenizer = new StringTokenizer(readingDate, ".");
        tokenizer.nextToken();
        String year = tokenizer.nextToken();
        if (year.equalsIgnoreCase("X")) {
            return 0;
        }
        return Integer.parseInt(year);
    }
    public int getReadingMonth() {
        StringTokenizer tokenizer = new StringTokenizer(readingDate, ".");
        String month = tokenizer.nextToken();
        if (month.equalsIgnoreCase("X")) {
            return 0;
        }
        return Integer.parseInt(month);
    }
    public String getAuthorLastName() {
        return author.substring(author.lastIndexOf(" "));
    }
    public int getTotalRat() {
        int relevantSecs = 5;
        ArrayList<Integer> rats = new ArrayList<>();
        rats.add(tensionRat);
        rats.add(funRat);
        rats.add(storyRat);
        rats.add(interestRat);
        rats.add(languageRat);
        int totalPoints = 0;
        for (int oneRat : rats) {
            if (oneRat == 11) {
                relevantSecs--;
            } else {
                totalPoints += oneRat;
            }
        }
        double ratD = (double) totalPoints / ((double) relevantSecs * 10);
        double ratDouble = Math.round(ratD * 100) / 100.0;
        return (int) Math.round(ratDouble * 100);
    }
    public boolean isRecommended() {
        return recommended;
    }
}
