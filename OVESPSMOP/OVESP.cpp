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
	 bool check;


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
		  printf("%d\n", check);
	 	 
	 	 if(check==false)
		 {
		    sprintf(reponse,"LOGIN#OK");
		    ajoute(socket);
		    printf("Bienvenu BG\n");
		    return true;
		 } 
		 else
		 {
		 	
		   sprintf(reponse,"LOGIN#ko#Mauvais identifiants !");
		   return false;
		 }
	 	}
	 	else
	 	{
	 		printf("Vous etez un nouveau client\n");
	 	}
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
bool SMOP_Login(const char* user,const char* password,MYSQL* connexion)
{
	printf("JE PASSE DANS LE LOGIN\n");
	if (mysql_query(connexion, "SELECT * FROM utilisateurs WHERE login = ?"))
    {
        fprintf(stderr, "Erreur lors de l'exécution de la requête : %s\n", mysql_error(connexion));
        return false;
    }

    MYSQL_RES *resultat = mysql_store_result(connexion);

    if (resultat == NULL)
    {
        fprintf(stderr, "Erreur lors de la récupération du résultat : %s\n", mysql_error(connexion));
        return false;
    }

    if (mysql_num_rows(resultat) == 1)
    {
        mysql_free_result(resultat);
        return true;
    }
    else
    {
        mysql_free_result(resultat);
        return false;
    }


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