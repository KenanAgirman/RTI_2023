#include "windowclient.h"
#include "ui_windowclient.h"
#include <QMessageBox>
#include <string>
#include "../OVESPSMOP/TCP.h"
using namespace std;

extern WindowClient *w;
bool logged = false;
int sClientClient;
#define MAXCADDIE 10

typedef struct
{
  int   id;
  char  intitule[20];
  float prix;
  int   stock;  
  char  image[20];
} ARTICLE;

ARTICLE ArtcileCourant;
ARTICLE Caddie[MAXCADDIE];
int nbArticles = 0;
float totalCaddie = 0.0;
#define REPERTOIRE_IMAGES "ClientQt/images/"

WindowClient::WindowClient(int sClient,QWidget *parent) : QMainWindow(parent), ui(new Ui::WindowClient)
{
    ui->setupUi(this);

    // Configuration de la table du panier (ne pas modifer)
    ui->tableWidgetPanier->setColumnCount(3);
    ui->tableWidgetPanier->setRowCount(0);
    QStringList labelsTablePanier;
    labelsTablePanier << "Article" << "Prix à l'unité" << "Quantité";
    ui->tableWidgetPanier->setHorizontalHeaderLabels(labelsTablePanier);
    ui->tableWidgetPanier->setSelectionMode(QAbstractItemView::SingleSelection);
    ui->tableWidgetPanier->setSelectionBehavior(QAbstractItemView::SelectRows);
    ui->tableWidgetPanier->horizontalHeader()->setVisible(true);
    ui->tableWidgetPanier->horizontalHeader()->setDefaultSectionSize(160);
    ui->tableWidgetPanier->horizontalHeader()->setStretchLastSection(true);
    ui->tableWidgetPanier->verticalHeader()->setVisible(false);
    ui->tableWidgetPanier->horizontalHeader()->setStyleSheet("background-color: lightyellow");

    ui->pushButtonPayer->setText("Confirmer achat");

 
    int i = 0;

    while(i<MAXCADDIE)
    {
      printf("CADIE =%d\n",Caddie[i].id);
      Caddie[i].id = 0;
      i++;
    }

    sClientClient = sClient;
}

WindowClient::~WindowClient()
{
    delete ui;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles : ne pas modifier /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setNom(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditNom->clear();
    return;
  }
  ui->lineEditNom->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
const char* WindowClient::getNom()
{
  strcpy(nom,ui->lineEditNom->text().toStdString().c_str());
  return nom;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setMotDePasse(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditMotDePasse->clear();
    return;
  }
  ui->lineEditMotDePasse->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
const char* WindowClient::getMotDePasse()
{
  strcpy(motDePasse,ui->lineEditMotDePasse->text().toStdString().c_str());
  return motDePasse;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setPublicite(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditPublicite->clear();
    return;
  }
  ui->lineEditPublicite->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setImage(const char* image)
{
  // Met à jour l'image
  char cheminComplet[80];
  sprintf(cheminComplet,"%s%s",REPERTOIRE_IMAGES,image);
  QLabel* label = new QLabel();
  label->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
  label->setScaledContents(true);
  QPixmap *pixmap_img = new QPixmap(cheminComplet);
  label->setPixmap(*pixmap_img);
  label->resize(label->pixmap()->size());
  ui->scrollArea->setWidget(label);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::isNouveauClientChecked()
{
  if (ui->checkBoxNouveauClient->isChecked()) return 1;
  return 0;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setArticle(const char* intitule,float prix,int stock,const char* image)
{
  ui->lineEditArticle->setText(intitule);
  if (prix >= 0.0)
  {
    char Prix[20];
    sprintf(Prix,"%.2f",prix);
    ui->lineEditPrixUnitaire->setText(Prix);
  }
  else ui->lineEditPrixUnitaire->clear();
  if (stock >= 0)
  {
    char Stock[20];
    sprintf(Stock,"%d",stock);
    ui->lineEditStock->setText(Stock);
  }
  else ui->lineEditStock->clear();
  setImage(image);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::getQuantite()
{
  return ui->spinBoxQuantite->value();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setTotal(float total)
{
  if (total >= 0.0)
  {
    char Total[20];
    sprintf(Total,"%.2f",total);
    ui->lineEditTotal->setText(Total);
  }
  else ui->lineEditTotal->clear();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::loginOK()
{
  ui->pushButtonLogin->setEnabled(false);
  ui->pushButtonLogout->setEnabled(true);
  ui->lineEditNom->setReadOnly(true);
  ui->lineEditMotDePasse->setReadOnly(true);
  ui->checkBoxNouveauClient->setEnabled(false);

  ui->spinBoxQuantite->setEnabled(true);
  ui->pushButtonPrecedent->setEnabled(true);
  ui->pushButtonSuivant->setEnabled(true);
  ui->pushButtonAcheter->setEnabled(true);
  ui->pushButtonSupprimer->setEnabled(true);
  ui->pushButtonViderPanier->setEnabled(true);
  ui->pushButtonPayer->setEnabled(true);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::logoutOK()
{
  ui->pushButtonLogin->setEnabled(true);
  ui->pushButtonLogout->setEnabled(false);
  ui->lineEditNom->setReadOnly(false);
  ui->lineEditMotDePasse->setReadOnly(false);
  ui->checkBoxNouveauClient->setEnabled(true);

  ui->spinBoxQuantite->setEnabled(false);
  ui->pushButtonPrecedent->setEnabled(false);
  ui->pushButtonSuivant->setEnabled(false);
  ui->pushButtonAcheter->setEnabled(false);
  ui->pushButtonSupprimer->setEnabled(false);
  ui->pushButtonViderPanier->setEnabled(false);
  ui->pushButtonPayer->setEnabled(false);

  setNom("");
  setMotDePasse("");
  ui->checkBoxNouveauClient->setCheckState(Qt::CheckState::Unchecked);

  setArticle("",-1.0,-1,"");

  w->videTablePanier();
  w->setTotal(-1.0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles Table du panier (ne pas modifier) /////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::ajouteArticleTablePanier(const char* article,float prix,int quantite)
{
    char Prix[20],Quantite[20];

    sprintf(Prix,"%.2f",prix);
    sprintf(Quantite,"%d",quantite);

    // Ajout possible
    int nbLignes = ui->tableWidgetPanier->rowCount();
    nbLignes++;
    ui->tableWidgetPanier->setRowCount(nbLignes);
    ui->tableWidgetPanier->setRowHeight(nbLignes-1,10);

    QTableWidgetItem *item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(article);
    ui->tableWidgetPanier->setItem(nbLignes-1,0,item);

    item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(Prix);
    ui->tableWidgetPanier->setItem(nbLignes-1,1,item);

    item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(Quantite);
    ui->tableWidgetPanier->setItem(nbLignes-1,2,item);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::videTablePanier()
{
    ui->tableWidgetPanier->setRowCount(0);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::getIndiceArticleSelectionne()
{
    QModelIndexList liste = ui->tableWidgetPanier->selectionModel()->selectedRows();
    if (liste.size() == 0) return -1;
    QModelIndex index = liste.at(0);
    int indice = index.row();
    return indice;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions permettant d'afficher des boites de dialogue (ne pas modifier ////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::dialogueMessage(const char* titre,const char* message)
{
   QMessageBox::information(this,titre,message);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::dialogueErreur(const char* titre,const char* message)
{
   QMessageBox::critical(this,titre,message);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// CLIC SUR LA CROIX DE LA FENETRE /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::closeEvent(QCloseEvent *event)
{

  exit(0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions clics sur les boutons ////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonLogin_clicked()
{
    const char* nom = getNom();
    const char* password = getMotDePasse();
    char requete[200], reponse[200], reponseConnecte[10];

    if (strlen(nom) == 0)
    {
        dialogueErreur("Login", "Erreur le champ login est vide");
        return;
    }
    else if (strlen(password) == 0)
    {
        dialogueErreur("password", "Erreur le champ password est vide");
        return;
    }

    printf("Voici le nom = %s\n", nom);
    printf("Voici le mot de passe = %s\n", password);

    sprintf(requete, "LOGIN#%s#%s#%d\n", nom, password, isNouveauClientChecked());
    printf("REQUETE  requete requete requete requete = %s\n", requete);

    char response[200];
    SendReceive(requete, sClientClient, response, sizeof(response));

    char *ptr = strtok(response, "#");
    printf("ptr = %s\n", ptr);

    if (strcmp(ptr, "LOGIN") == 0)
    {
        strcpy(reponseConnecte, strtok(NULL, "#"));
        printf("ptr = %s\n", ptr);
        printf("SE = %s\n", reponseConnecte);

        if (strcmp(reponseConnecte, "ok") == 0)
        {
            getArticle(1);
            if (isNouveauClientChecked() == 1)
            {
                dialogueMessage("Login", "ok");
            }

            printf("SE1 = %s\n", reponseConnecte);
            dialogueMessage("Login", "Connexion établie");

            loginOK();
            setPublicite("TROP CHAUD BEAUGOSSE");
        }
        else
        {
            setPublicite("PAS BON\n");
        }
    }
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonLogout_clicked()
{
  char requete[200],reponse[200],reponseConnecte[100];

  int nbEcrits, nbLus;

  sprintf(requete, "LOGOUT");

  if((nbEcrits = Send(sClientClient,requete,strlen(requete))) == -1)
  {
    perror("Erreur de Send");
     exit(1);
  }
  printf("NbEcrits = %d\n",nbEcrits);
  printf("Ecrit = --%s--\n",requete);
  

  if((nbLus = Receive(sClientClient,reponse)) < 0)
  {
    perror("Erreur de Receive");
    exit(1);
  }
        
  printf("NbLus = %d\n",nbLus);
  reponse[nbLus] = 0;
  printf("Lu = --%s--\n",reponse);
  
  char *ptr = strtok(reponse,"#");
  printf("ptr = %s\n",ptr);
  
  if (strcmp(ptr,"LOGOUT") == 0) 
  {
    strcpy(reponseConnecte,strtok(NULL,"#"));
    printf("ptr = %s\n",ptr);
    printf("SE = %s\n",reponseConnecte);

    logoutOK();
    setPublicite("AU REVOIR");
    
  }
         
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonSuivant_clicked()
{
  getArticle(ArtcileCourant.id+1);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonPrecedent_clicked()
{
  getArticle(ArtcileCourant.id-1);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonAcheter_clicked()
{
  char requete[200],reponse[200],reponseConnecte[100];

  int nbEcrits, nbLus;
  int quantite = getQuantite();

  int i = 0;

  while(Caddie[i].id != ArtcileCourant.id && i < MAXCADDIE)
  {
            i++;
  } 

  if(nbArticles == MAXCADDIE && i == MAXCADDIE)
  {
    dialogueMessage("Achat", "votre panier est plein, attention!");
    exit(1);
  }

  if(quantite==0)
  {
    dialogueMessage("Fruits","ERREUR");
  }
  else
  {
    sprintf(requete,"ACHAT#%d#%d", ArtcileCourant.id, getQuantite());

    if((nbEcrits = Send(sClientClient,requete,strlen(requete))) == -1)
    {
      perror("Erreur de Send");
      exit(1);
    }
    printf("NbEcrits = %d\n",nbEcrits);
    printf("Ecrit = --%s--\n",requete);


   if((nbLus = Receive(sClientClient,reponse)) < 0)
   {
     perror("Erreur de Receive");
     exit(1);
   }
        
   printf("NbLus = %d\n",nbLus);
   reponse[nbLus] = 0;
   printf("Lu = --%s--\n",reponse);
  

   char *ptr = strtok(reponse,"#");
   printf("ptr = %s\n",ptr);
  
   if(strcmp(ptr,"ACHAT") == 0) 
   {
    
    strcpy(reponseConnecte,strtok(NULL,"#"));
    printf("ptr = %s\n",ptr);
    printf("SE = %s\n",reponseConnecte);

    int id = atoi(strtok(NULL,"#"));

    printf("id = %d\n",id);

    if(id==0)
    {
      dialogueMessage("Stock","Insuffisante");
    }
    else
    {
      int qt = atoi(strtok(NULL,"#"));
      printf("id = %d\n",id);

      ArtcileCourant.id = id;
      ArtcileCourant.stock = qt - getQuantite();
      ArtcileCourant.prix = atof(strtok(NULL,"#"));

      setArticle(ArtcileCourant.intitule, ArtcileCourant.prix, ArtcileCourant.stock , ArtcileCourant.image);
      printf("id = %d\n",id);

      if(i == MAXCADDIE)
      {
          printf("id = %d\n",id);

          i = 0;
          printf("J'ajoute un article \n");

          printf("AVICAD =%d\n",i);
          printf("MAXCADDIE =%d\n",MAXCADDIE);
          while(Caddie[i].id != 0 && i < MAXCADDIE) i++;
          printf("APICAD =%d\n",i);

          Caddie[i].id = ArtcileCourant.id;
          strcpy(Caddie[i].intitule, ArtcileCourant.intitule);
          Caddie[i].prix = ArtcileCourant.prix;
          Caddie[i].stock = getQuantite();
          strcpy(Caddie[i].image, ArtcileCourant.image);
          
          ajouteArticleTablePanier(Caddie[i].intitule, Caddie[i].prix, Caddie[i].stock);
          
          totalCaddie = totalCaddie + (Caddie[i].stock*Caddie[i].prix);

          setTotal(totalCaddie);

          nbArticles++;
      }
      else
      {
          printf("avant i = %d\n",i);
          printf("nbArticles nbArticles =%d\n",nbArticles);
          videTablePanier();

          Caddie[i].stock = Caddie[i].stock + getQuantite();
          totalCaddie = 0.0;
          setTotal(-1.0);
          printf("iciciciciciciejiduaznedxn\n");
          i = 0;

          while(i < nbArticles)
          {
            printf("kenankenneeknakenzn\n");
            printf("nbArticles nbArticles =%d\n",nbArticles);
           printf("apres i = %d\n",i);

            ajouteArticleTablePanier(Caddie[i].intitule, Caddie[i].prix, Caddie[i].stock);
            totalCaddie = totalCaddie + (Caddie[i].stock*Caddie[i].prix);

            i++;
          }

          setTotal(totalCaddie);
      }
    }

        
   }

  }

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonSupprimer_clicked() {

 char requete[200], reponse[200];
 int nbEcrits, nbLus;

  int indice = getIndiceArticleSelectionne();

  printf("INDICE = %d\n", indice);

  if (indice == -1)
  {
        dialogueMessage("Erreur", "Erreur");
  }
  else
  {
        sprintf(requete, "CANCEL#%d#%d", Caddie[indice].id, Caddie[indice].stock);

        if ((nbEcrits = Send(sClientClient, requete, strlen(requete))) == -1)
        {
            perror("Erreur de Send");
            exit(1);
        }
        printf("NbEcrits = %d\n", nbEcrits);
        printf("Ecrit = --%s--\n", requete);

        if ((nbLus = Receive(sClientClient, reponse)) < 0)
        {
            perror("Erreur de Receive");
            exit(1);
        }

        printf("NbLus = %d\n", nbLus);
        reponse[nbLus] = 0;
        printf("Lu = --%s--\n", reponse);

        char *ptr = strtok(reponse, "#");

        if (strcmp(ptr, "CANCEL") == 0)
        {
            int newID;
            newID = atoi(strtok(NULL, "#"));

            printf("newID =newID %d\n",newID);

            if (newID != -1)
            {
                if (ArtcileCourant.id == newID)
                {
                    ArtcileCourant.stock = atoi(strtok(NULL, "#"));
                    setArticle(ArtcileCourant.intitule, ArtcileCourant.prix, ArtcileCourant.stock, ArtcileCourant.image);
                }

                if (indice < nbArticles - 1)
                {
                    for (int i = indice; i < nbArticles - 1; i++)
                    {
                        Caddie[i] = Caddie[i + 1];
                    }
                }

                Caddie[nbArticles - 1].id = 0;
                nbArticles--;

                videTablePanier();

                totalCaddie = 0.0;
                setTotal(-1.0);

                for (int i = 0; i < nbArticles; i++)
                {
                    ajouteArticleTablePanier(Caddie[i].intitule, Caddie[i].prix, Caddie[i].stock);
                    totalCaddie += Caddie[i].stock * Caddie[i].prix;
                }

                setTotal(totalCaddie);
            }
            else
            {
                dialogueErreur("Cancel", "Erreur");
            }
        }
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonViderPanier_clicked()
{
  char requete[200],reponse[100];
  sprintf(requete,"CANCELALL#%d", nbArticles);
  int nbEcrits,nbLus;

  for (int i = 0; i < 10 && Caddie[i].id != 0; i++)
  {
      sprintf(requete + strlen(requete), "#%d&%d", Caddie[i].id, Caddie[i].stock);
  }

  if((nbEcrits = Send(sClientClient, requete, strlen(requete))) == -1)
  {
    perror("Erreur de Send");
    exit(1);
  }
  
  printf("NbEcrits = %d\n", nbEcrits);
  printf("Ecrit = --%s--\n", requete);

  if((nbLus = Receive(sClientClient, reponse)) < 0)
  {
    perror("Erreur de Receive");
     exit(1);
  }

  printf("NbLus = %d\n", nbLus);
  reponse[nbLus] = 0;
  printf("Lu = --%s--\n", reponse);

  char *ptr = strtok(reponse,"#");


  if (strcmp(ptr,"CANCELALL") == 0)
  {
    printf("leilielallala\n");
      videTablePanier();

      totalCaddie = 0.0;
      setTotal(-1.0);
      for(int i = 0; i<MAXCADDIE; i++)
      {
        printf("je suis passé icic;<;w;\n");
          Caddie[i].id = 0;
          Caddie[i].intitule[0] = '\0';
          Caddie[i].prix = 0.0;
          Caddie[i].stock = 0;
          Caddie[i].image[0] = '\0';

         setArticle(ArtcileCourant.intitule, ArtcileCourant.prix, ArtcileCourant.stock , ArtcileCourant.image);

      }
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonPayer_clicked()
{
    char requete[200],reponse[200];
    int nbEcrits,nbLus;

    sprintf(requete,"CONFIRMER#%d", nbArticles);


    if((nbEcrits = Send(sClientClient,requete,strlen(requete))) == -1)
    {
      perror("Erreur de Send");
      exit(1);
    }

 
    printf("NbEcrits = %d\n",nbEcrits);
    printf("Ecrit = --%s--\n",requete);


    if((nbLus = Receive(sClientClient,reponse)) < 0)
    {
      perror("Erreur de Receive");
      exit(1);
    }
  
    printf("NbLus = %d\n",nbLus);
    reponse[nbLus] = 0;
    printf("Lu = --%s--\n",reponse);

    char *ptr = strtok(reponse,"#");

    if (strcmp(ptr,"CONFIRMER") == 0) 
    {
        videTablePanier();

        totalCaddie = 0.0;
        setTotal(-1.0);


        dialogueMessage("facture","payer");
    }
}
void WindowClient::getArticle(int id)
{
  char requete[200],reponse[200];

  int nbEcrits, nbLus;

  sprintf(requete,"CONSULT#%d",id);

  SendReceive(requete, sClientClient, reponse, sizeof(reponse));

  printf("NbLus = %d\n",nbLus);
  printf("Lu = --%s--\n",reponse);

  char *ptr = strtok(reponse,"#");

  if (strcmp(ptr,"CONSULT") == 0) 
  {
    int idBD;
    idBD = atoi(strtok(NULL,"#"));

    char intitule[20], image[20];
    float prix;
    int stock;
    printf("idBD = %d\n",idBD);

    strcpy(intitule,strtok(NULL,"#"));
    stock = atoi(strtok(NULL,"#"));
    prix = atof(strtok(NULL,"#"));
    strcpy(image,strtok(NULL,"#"));

    setArticle(intitule, prix, stock , image);

    ArtcileCourant.id = idBD;
    ArtcileCourant.stock = stock;
    ArtcileCourant.prix = prix;
    strcpy(ArtcileCourant.intitule, intitule);
    strcpy(ArtcileCourant.image, image);

  }
}
void WindowClient::SendReceive(char* request, int socket, char* response, int responseSize)
{
    int nbEcrits = Send(socket, request, strlen(request));
    if (nbEcrits == -1)
    {
        perror("Erreur de Send");
        exit(1);
    }

    int nbLus = Receive(socket, response);
    if (nbLus < 0)
    {
        perror("Erreur de Receive");
        exit(1);
    }

    response[nbLus] = '\0';
}