#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include "TCP.h"
#include "OVESP.h"
int sClient;
void HandlerSIGINT(int s);
void Echange(char* requete, char* reponse);
void SMOP_Logout();
int main(int argc,char* argv[])
{
 if (argc != 3)
 {
 printf("Erreur...\n");
 printf("USAGE : Client ipServeur portServeur\n");
 exit(1);
 }
 // Armement des signaux
 struct sigaction A;
 A.sa_flags = 0;
 sigemptyset(&A.sa_mask);
 A.sa_handler = HandlerSIGINT;
 if (sigaction(SIGINT,&A,NULL) == -1)
 {
 perror("Erreur de sigaction");
 exit(1);
 }
 // Connexion sur le serveur 
 if ((sClient = ClientSocket(argv[1],atoi(argv[2]))) == -1)
 {
 perror("Erreur de ClientSocket");
 exit(1);
 }
 printf("Connecte sur le serveur.\n");
 // Phase de login
 char user[50],password[50];
 printf("user: "); fgets(user,50,stdin);
 user[strlen(user)-1] = 0;
 printf("password: "); fgets(password,50,stdin);
 password[strlen(password)-1] = 0;
 if (!SMOP_Login(user,password))
 exit(1);
 while(1)
 {
 int a,b;
 char op;
 printf("Operation (<CTRL-C> four fin) : ");
 fflush(stdin);
 scanf("%d %c %d",&a,&op,&b); // pas ouf !
 }
}
//***** Fin de connexion ********************************************
void HandlerSIGINT(int s)
{
 printf("\nArret du client.\n");
 SMOP_Logout();
 close(sClient);
 exit(0);
}

//*******************************************************************
void SMOP_Logout()
{
 char requete[200],reponse[200];
 int nbEcrits, nbLus;
 
 // ***** Construction de la requete *********************
 sprintf(requete,"LOGOUT");
 // ***** Envoi requete + réception réponse **************
 Echange(requete,reponse);
 // ***** Parsing de la réponse **************************
 // pas vraiment utile...
}

void Echange(char* requete, char* reponse)
{
 int nbEcrits, nbLus;
 // ***** Envoi de la requete ****************************
 if ((nbEcrits = Send(sClient,requete,strlen(requete))) == -1)
 {
 perror("Erreur de Send");
 close(sClient);
 exit(1);
 }
 // ***** Attente de la reponse **************************
 if ((nbLus = Receive(sClient,reponse)) < 0)
 {
 perror("Erreur de Receive");
 close(sClient);
 exit(1);
 }
 if (nbLus == 0)
 {
 printf("Serveur arrete, pas de reponse reçue...\n");
 close(sClient);
 exit(1);
 }
 reponse[nbLus] = 0;
}