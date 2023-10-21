#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <signal.h>
#include <mysql.h>


#include "OVESP.h"

//***** Etat du protocole : liste des clients loggés ****************
int clients[NB_MAX_CLIENTS];
int nbClients = 0;

int isNouveau =0;


int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

bool SMOP(char *requete, char *reponse, int socket,MYSQL* connexion)
{
	printf("REQUETE =%s/%s/%d\n",requete,reponse,socket);
	// ***** Récupération nom de la requete *****************

	char *ptr = strtok(requete,"#");

	// ***** Récupération nom de la requete *****************

	if(strcmp(ptr,"LOGIN") == 0) 
	{
	 char user[50], password[50];
	 int nouveauClient;
	 bool check,check2;


	 strcpy(user,strtok(NULL,"#"));

	 strcpy(password,strtok(NULL,"#"));

     nouveauClient = atoi(strtok(NULL,"#"));
	 
	 printf("\t[THREAD %p] LOGIN de %s\n",pthread_self(),user);

	 if(estPresent(socket) >= 0) // client déjà loggé
	 {
	   sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
	   return false;
	 }
	 else
	 {
	 	if(nouveauClient==0)
	 	{
	 	  check = SMOP_Login(user,password,connexion);
		  printf("check check check =%d\n", check);
	 	 
	 	 if(check==true)
		 {
		    sprintf(reponse,"LOGIN#ok");
		    ajoute(socket);
		    printf("Bienvenu BG\n");
		 } 
		 else
		 {
		 	
		   sprintf(reponse,"LOGIN#ko");
		   printf("je susi ciciciciicicic\n");
		 }
	 	}
	 	else
	 	{
	 		printf("Vous etez un nouveau client\n");
	 		check2 = nouveauClientDansBD(user,password,connexion);

	 		if(check2==false)
	 		{
	 			sprintf(reponse,"LOGIN#ok");
                ajoute(socket);

	 		}
	 		else
	 		{
	 			sprintf(reponse,"LOGIN#ko#utilisateursko");

	 		}
	 	}
	 }

	}
	else
	{
		if (strcmp(ptr,"CONSULT") == 0)
		 {
		        int idid;
		        idid = atoi(strtok(NULL,"#"));

		        SMOP_Consult(idid, connexion,reponse);
		   }

		    if (strcmp(ptr,"CANCEL") == 0)
		    {
		        int idid,Quantite;

		        idid = atoi(strtok(NULL,"#"));
		        Quantite = atoi(strtok(NULL,"#"));
		        SMOP_CANCEL(idid, connexion,reponse,Quantite);
		    }
                if (strcmp(ptr, "CANCELALL") == 0)
                {
                    char req[200];
                    int totalArticles = atoi(strtok(NULL, "#"));
                    printf("Total d'articles = %d\n", totalArticles);

                    snprintf(req, sizeof(req), "CANCELALL#%d", totalArticles);

                    for (int i = 0; i < totalArticles; i++)
                    {
                        int id = atoi(strtok(NULL, "#"));
                        int stock = atoi(strtok(NULL, "#"));

                        snprintf(req + strlen(req), sizeof(req) - strlen(req), "#%d#%d", id, stock);

                        printf("Article %d - ID: %d, Stock: %d\n", i + 1, id, stock);
                    }

                    printf("SERVERUCLIENT = %s\n", req);

                    SMOP_Cancel_All(req, totalArticles, reponse, connexion);
                }
	}

    if (strcmp(ptr,"ACHAT") == 0)
    {
        int idid,Quantite;
        idid = atoi(strtok(NULL,"#"));
		Quantite = atoi(strtok(NULL,"#"));
	
		printf("ACHAT ACHAT %d/t + qauntite %d\t",idid,Quantite);
        SMOP_ACHAT(idid, connexion,reponse,Quantite);
    }
    else
    {
    	if(strcmp(ptr,"CONFIRMER")==0)
	    {
	    	int idid, Quantite;
			char delim[100] = "";
			idid = atoi(strtok(NULL, "#"));

			SMOP_CONFIRM(requete, idid, reponse, connexion);
		}
	    
    }

	// ***** LOGOUT *****************************************
	if(strcmp(ptr,"LOGOUT") == 0)
	{
	  printf("\t[THREAD %p] LOGOUT\n",pthread_self());
	  retire(socket);
	  sprintf(reponse,"LOGOUT#ok");
	  return false;
	}
	return true;
}


//***** Traitement des requetes *************************************
bool SMOP_Login(const char* user, const char* password, MYSQL* connexion)
{
    printf("JE PASSE DANS LE LOGIN\n");
    
    char requete[100];

    MYSQL_ROW row;
    MYSQL_RES* resultat;

    sprintf(requete,"select * from clients where login = '%s';",user);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n",mysql_error(connexion));
        exit(1);
    }

    if ((resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n",mysql_error(connexion));
        exit(1);
    }

    if((row = mysql_fetch_row(resultat)) != NULL)
    {
        if(strcmp(password, row[1]) == 0)
        {
        	return true;
        }
        else
        {
        	return false;
        }
    }

    return false;
}

int nouveauClientDansBD(const char* user, const char* password, MYSQL* connexion)
{
    printf("JE PASSE DANS NOUVEAU\n");
    
    char requete[100];

    MYSQL_ROW row;
    MYSQL_RES* resultat;


    sprintf(requete,"select * from clients where login = '%s';", user);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n",mysql_error(connexion));
        exit(1);
    }

    printf("Requete SELECT réussie sur login.\n");

    // Affichage du Result Set

    if ((resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n",mysql_error(connexion));
        exit(1);
    }

    if((row = mysql_fetch_row(resultat)) != NULL)
    {
        return true;
    }
    else
    {
        sprintf(requete,"insert into clients (login, password) values ('%s', '%s')",user, password);


        if (mysql_query(connexion,requete) != 0)
        {
            fprintf(stderr, "Erreur de mysql_query: %s\n",mysql_error(connexion));
            exit(1);
        }

        printf("Requete INSERT réussie sur login.\n");

        return false;
    }
}

void SMOP_Consult(int id, MYSQL* connexion, char* rep)
{
    printf("JE CONSULTE\n");

    char requete[100];
    MYSQL_ROW row;
    MYSQL_RES* resultat;

    sprintf(requete, "SELECT * FROM articles WHERE id = %d;", id);

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    if ((resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    while ((row = mysql_fetch_row(resultat)) != NULL)
    {
        int id = atoi(row[0]);
        const char* intitule = row[1];
        float prix = atof(row[2]);
        int stock = atoi(row[3]);
        const char* image = row[4];

        sprintf(rep, "CONSULT#%d#%s#%f#%d#%s", id,intitule, prix, stock, image);

        printf("ID: %d\n", id);
        printf("Intitulé: %s\n", intitule);
        printf("Prix: %.2f\n", prix);
        printf("Stock: %d\n", stock);
        printf("Image: %s\n", image);
    }

    mysql_free_result(resultat);
}

void SMOP_ACHAT(int id, MYSQL* connexion, char* rep, int quantite)
{
    printf("J'achète\n");

    char requete[200];
    MYSQL_RES* resultat;
    MYSQL_ROW row;
    int stockBD;

    sprintf(requete, "SELECT * FROM articles WHERE id = %d", id);

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        sprintf(rep, "ACHAT#-1");
        return;
    }

    printf("Requête SELECT réussie sur ACHAT.\n");

    resultat = mysql_store_result(connexion);

    if (resultat)
    {
		  while ((row = mysql_fetch_row(resultat)))
		  {
		  	if(atoi(row[3]) < quantite)
		  	{
		  	  sprintf(rep, "ACHAT#0");
		  	}
		  	else
		  	{
		  		printf("atoi = = %d\n",atoi(row[3]));
				stockBD = atoi(row[3])-quantite;
    			sprintf(requete, "UPDATE articles SET stock = %d WHERE id = %d", stockBD,id);

    			if (mysql_query(connexion, requete) != 0)
				{
					fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
					sprintf(rep, "ACHAT#-1");	    
				}

				sprintf(rep,"ACHAT#%s#%s#%s#%s#%d", row[0], row[1],row[3], row[2],stockBD);

		  	}
		  }

        mysql_free_result(resultat);
    }
    else
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        sprintf(rep, "ACHAT#-1");
    }
}


void SMOP_CANCEL(int id, MYSQL *connexion, char *rep, int quantite)
{
    printf("J'annule\n");

    char requete[200];
    MYSQL_RES *resultat;
    MYSQL_ROW row;

    sprintf(requete, "SELECT * FROM articles WHERE id = %d;", id);

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        sprintf(rep, "CANCEL#-1");
        return;
    }

    printf("Requête SELECT réussie sur Cancel.\n");

    if ((resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        sprintf(rep, "CANCEL#-1");
        return;
    }

    if ((row = mysql_fetch_row(resultat)) != NULL)
    {
        int newStock = atoi(row[3]) + quantite;

        sprintf(requete, "UPDATE articles SET stock = %d WHERE id = %d;", newStock, id);

        if (mysql_query(connexion, requete) != 0)
        {
            fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
            sprintf(rep, "CANCEL#-1");
        }
        else
        {
            printf("Requête UPDATE réussie.\n");
            sprintf(rep, "CANCEL#%d#%d", id, newStock);
        }
    }
    else
    {
        sprintf(rep, "CANCEL#-1");
    }
}

void SMOP_Cancel_All(char *requete, int nbArti, char *rep, MYSQL *connexion)
{
    printf("J'annule TOUT\n");
    printf("Requête CANCELALL = %s\n", requete);

    // Utilisez strtok pour extraire les éléments de la chaîne
    char *token = strtok(requete, "#");
    
    // Vérifiez le premier token (CANCELALL)
    if (strcmp(token, "CANCELALL") != 0) {
        printf("Requête non valide : %s\n", requete);
        return;  // Quittez la fonction si la requête n'est pas valide
    }

    // Passez au prochain token pour obtenir le nombre total d'articles
    token = strtok(NULL, "#");
    int totalArticles = atoi(token);
    printf("Total d'articles = %d\n", totalArticles);

    // Boucle pour extraire chaque article
    for (int i = 0; i < totalArticles; i++) {
        // ID de l'article
        token = strtok(NULL, "#");
        int id = atoi(token);

        // Quantité de l'article
        token = strtok(NULL, "#");
        int quantite = atoi(token);

        printf("Article %d - ID: %d, Quantité: %d\n", i + 1, id, quantite);

        // Construisez la requête SQL pour mettre à jour le stock de l'article
        char sqlQuery[200];
        snprintf(sqlQuery, sizeof(sqlQuery), "UPDATE articles SET stock = stock + %d WHERE id = %d", quantite, id);

        // Exécutez la requête SQL
        if (mysql_query(connexion, sqlQuery) != 0) {
            fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
            sprintf(rep, "CANCELALL#-1");
            return;
        }
    }
}



void SMOP_CONFIRM(char *requete,int nbArti, char* rep, MYSQL* connexion)
{
	sprintf(rep, "CONFIRMER#1");
}


//***** Gestion de l'état du protocole ******************************
int estPresent(int socket)
{
 int indice = -1;
 pthread_mutex_lock(&mutexClients);

 for(int i=0 ; i<nbClients ; i++)
 if (clients[i] == socket)
 { 
 	indice = i; break; 
 }

 pthread_mutex_unlock(&mutexClients);
 return indice;
}
void ajoute(int socket)
{
 pthread_mutex_lock(&mutexClients);
 clients[nbClients] = socket;
 nbClients++;
 pthread_mutex_unlock(&mutexClients);
}

void retire(int socket)
{
 int pos = estPresent(socket);
 if (pos == -1) 
 {
 	return;
 }

 pthread_mutex_lock(&mutexClients);
 for (int i=pos ; i<=nbClients-2 ; i++)
 {
 	clients[i] = clients[i+1];
 }
 nbClients--;

 pthread_mutex_unlock(&mutexClients);
}

//***** Fin prématurée **********************************************
void SMOP_Close()
{

 pthread_mutex_lock(&mutexClients);
 for (int i=0 ; i<nbClients ; i++)
 {
 	close(clients[i]);
 }
 pthread_mutex_unlock(&mutexClients);

}