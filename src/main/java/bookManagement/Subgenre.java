package bookManagement;

public class Subgenre {

    private String genre;
    private String subgenre;

    public Subgenre(String genre, String subgenre) {
        this.genre = genre;
        this.subgenre = subgenre;
    }

    public String getGenre() {
        return genre;
    }

    public String getSubgenre() {
        return subgenre;
    }
}
