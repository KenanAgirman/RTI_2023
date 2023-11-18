package Model;

public class article {
    private model model;
    private int id;
    private String intitule;
    private float prix;
    private String image;

    public static int numFactures = 0;
    public static int nbArticles = 0;
    private float totalCaddie;
    private int stock;

    public article() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public article(int id, String intitule, float prix, int stock, String image) {
        this.id = id;
        this.intitule = intitule;
        this.prix = prix;
        this.stock = stock;
        this.image = image;

    }

    public String getIntitule() {
        return intitule;
    }

    public float getPrix() {
        return prix;
    }

    public String getImage() {
        return image;
    }

    public int getStock() {
        return stock;
    }

    public float getTotalCaddie() {
        return totalCaddie;
    }

    public void setTotalCaddie(float totalCaddie) {
        this.totalCaddie = totalCaddie;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setNbArticles(int nbArticles) {
        this.nbArticles = nbArticles;
    }

    public int getNbArticles() {
        return nbArticles;
    }

    public static void setNumFactures(int numFactures) {
        article.numFactures = numFactures;
    }

    public static int getNumFactures() {
        return numFactures;
    }
}
