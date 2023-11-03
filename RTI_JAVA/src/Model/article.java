package Model;

public class article {
    private model model;
    private int id;
    private String intitule;
    private float prix;
    private String image;

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
}
