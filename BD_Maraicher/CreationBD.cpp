#include <stdio.h>
#include <stdlib.h>
#include <mysql.h>
#include <time.h>
#include <string.h>

typedef struct
{
  int   id;
  char  intitule[20];
  float prix;
  int   stock;  
  char  image[20];
} ARTICLE;

ARTICLE Elm[] = 
{
  {-1,"carottes",2.16f,9,"carottes.jpg"},
  {-1,"cerises",9.75f,8,"cerises.jpg"},
  {-1,"artichaut",1.62f,15,"artichaut.jpg"},
  {-1,"bananes",2.6f,8,"bananes.jpg"},
  {-1,"champignons",10.25f,4,"champignons.jpg"},
  {-1,"concombre",1.17f,5,"concombre.jpg"},
  {-1,"courgette",1.17f,14,"courgette.jpg"},
  {-1,"haricots",10.82f,7,"haricots.jpg"},
  {-1,"laitue",1.62f,10,"laitue.jpg"},
  {-1,"oranges",3.78f,23,"oranges.jpg"},
  {-1,"oignons",2.12f,4,"oignons.jpg"},
  {-1,"nectarines",10.38f,6,"nectarines.jpg"},
  {-1,"peches",8.48f,11,"peches.jpg"},
  {-1,"poivron",1.29f,13,"poivron.jpg"},
  {-1,"pommes de terre",2.17f,25,"pommesDeTerre.jpg"},
  {-1,"pommes",4.00f,26,"pommes.jpg"},
  {-1,"citrons",4.44f,11,"citrons.jpg"},
  {-1,"ail",1.08f,14,"ail.jpg"},
  {-1,"aubergine",1.62f,17,"aubergine.jpg"},
  {-1,"echalotes",6.48f,13,"echalotes.jpg"},
  {-1,"tomates",5.49f,22,"tomates.jpg"}
};

int main(int argc, char *argv[]) {
    // Connexion à la base de données MySQL
    printf("Connexion à la base de données...\n");
    MYSQL *connexion = mysql_init(NULL);
    if (mysql_real_connect(connexion, "localhost", "Student", "PassStudent1_", "PourStudent", 0, NULL, 0) == NULL) {
        fprintf(stderr, "Échec de la connexion à la base de données : %s\n", mysql_error(connexion));
        return 1;
    }

    // Création de la table "articles"
    printf("Création de la table articles...\n");
    mysql_query(connexion, "DROP TABLE IF EXISTS articles;"); // Suppression de la table si elle existe
    mysql_query(connexion, "CREATE TABLE articles (id INT(4) AUTO_INCREMENT PRIMARY KEY, intitule VARCHAR(20), prix FLOAT(4), stock INT(4), image VARCHAR(20));");

    // Ajout de tuples dans la table "articles"
    printf("Ajout de 21 articles dans la table articles...\n");
    char requete[256];
    for (int i = 0; i < 21; i++) {
        sprintf(requete, "INSERT INTO articles (intitule, prix, stock, image) VALUES ('%s', %f, %d, '%s');", Elm[i].intitule, Elm[i].prix, Elm[i].stock, Elm[i].image);
        mysql_query(connexion, requete);
    }

    // Création de la table "clients"
    printf("Création de la table clients...\n");
    mysql_query(connexion, "DROP TABLE IF EXISTS clients;"); // Suppression de la table si elle existe
    mysql_query(connexion, "CREATE TABLE clients (id INT(4) AUTO_INCREMENT PRIMARY KEY, login VARCHAR(30), password VARCHAR(30));");

    // Ajout d'un client (par exemple, 'bob' avec le mot de passe 'bob')
    printf("Ajout d'un client...\n");
    // mysql_query(connexion, "INSERT INTO clients (login, password) VALUES ('bob', 'bob');");

    // Création de la table "factures"
    printf("Creation de la table factures...\n");
    mysql_query(connexion,"drop table factures;"); // au cas ou elle existerait deja
    mysql_query(connexion, "CREATE TABLE factures (idFacture INT(4) AUTO_INCREMENT PRIMARY KEY, idClient INT(4), dateFacture DATE, montant FLOAT, paye INT);");

    // Fermeture de la connexion à la base de données
    mysql_close(connexion);

    printf("Tables créées avec succès.\n");

    return 0;
}