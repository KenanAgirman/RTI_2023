package Model;

public class Article {

    private int id;
    private String nom;
    private int quantite;
    private float UnitPrice;
    private String image;
    public Article(){

    }
    public Article(int id, String nom, int quantite, float unitPrice, String image) {
        this.id = id;
        this.nom = nom;
        this.quantite = quantite;
        UnitPrice = unitPrice;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public float getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
