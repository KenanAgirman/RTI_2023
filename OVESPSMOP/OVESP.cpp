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
    if (strcmp(ptr,"CONSULT") == 0)
    {
        int idid;
        idid = atoi(strtok(NULL,"#"));

        SMOP_Consult(idid, connexion,reponse);
    }

    if (strcmp(ptr,"ACHAT") == 0)
    {
        int idid,Quantite;
        idid = atoi(strtok(NULL,"#"));
		Quantite = atoi(strtok(NULL,"#"));

        SMOP_ACHAT(idid, connexion,reponse,Quantite);
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

    sprintf(requete,"select * from utilisateurs where login = '%s';",user);

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


    sprintf(requete,"select * from utilisateurs where login = '%s';", user);

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
        sprintf(requete,"insert into utilisateurs (login, MDP) values ('%s', '%s')",user, password);


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

        sprintf(rep, "CONSULT#%d#%s#%.2f#%d#%s", id,intitule, prix, stock, image);

        printf("ID: %d\n", id);
        printf("Intitulé: %s\n", intitule);
        printf("Prix: %.2f\n", prix);
        printf("Stock: %d\n", stock);
        printf("Image: %s\n", image);
    }

    mysql_free_result(resultat);
}

void SMOP_ACHAT(int id,MYSQL* connexion,char* rep,int quantite)
{
 printf("J'achète\n");

    char requete[200];
    MYSQL_RES* resultat;
    MYSQL_ROW tuple;

    char table[20];
    strcpy(table, "articles");

    sprintf(requete, "select * from %s where id = %d;", table, id);

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        sprintf(rep, "ACHAT#%d#-1", id); // Erreur SQL, retourne -1
        return;
    }

    printf("Requête SELECT réussie sur Achat.\n");

    if ((resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        sprintf(rep, "ACHAT#%d#-1", id); // Erreur de stockage des résultats, retourne -1
        return;
    }

    if ((tuple = mysql_fetch_row(resultat)) != NULL)
    {
        int stock = atoi(tuple[3]);

        if (stock < quantite)
        {
            sprintf(rep, "ACHAT#%d#0", id); // Stock insuffisant, retourne 0
        }
        else
        {
            int newStock = stock - quantite;

            sprintf(requete, "update %s set stock = %d where id = %d;", table, newStock, id);

            if (mysql_query(connexion, requete) != 0)
            {
                fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
                sprintf(rep, "ACHAT#%d#-1", id); // Erreur SQL lors de la mise à jour du stock, retourne -1
            }
            else
            {
                printf("Requête UPDATE réussie.\n");
                sprintf(rep, "ACHAT#%d#%d#%s#%.2f", id, quantite, tuple[1], atof(tuple[2])); // Achat réussi, retourne les détails de l'article
            }
        }
    }
    else
    {
        sprintf(rep, "ACHAT#-1"); // Article non trouvé, retourne -1
    }

    mysql_free_result(resultat);
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