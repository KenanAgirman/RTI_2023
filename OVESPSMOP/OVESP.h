#ifndef SMOP_H
#define SMOP_H
#define NB_MAX_CLIENTS 100

bool SMOP(char* requete, char* reponse,int socket,MYSQL* connexion);

bool SMOP_Login(const char* user,const char* password,MYSQL* connexion);

int nouveauClientDansBD(const char* user,const char* password,MYSQL* connexion);

void SMOP_Consult(int id,MYSQL* connexion,char* rep);

void SMOP_ACHAT(int id,MYSQL* connexion,char* rep,int qauntite);

void SMOP_CANCEL(int id,MYSQL* connexion,char* rep,int qauntite);

void SMOP_Cancel_All(char *requete,int nbArti, char* rep, MYSQL* connexion);

void SMOP_CONFIRM(char *requete,int nbArti, char* rep, MYSQL* connexion);

void SMOP_Close();

void SMOP_Facture(const char* user,int numFacture,float total,char* rep,MYSQL* connexion);

void SMOP_Vente(const char* user, int idFacture, int idArticle, int quantite, char* rep, MYSQL* connexion);

void SMOP_Logout(int socket);
#endif