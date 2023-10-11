#ifndef SMOP_H
#define SMOP_H
#define NB_MAX_CLIENTS 100

bool SMOP(char* requete, char* reponse,int socket,MYSQL* connexion);

bool SMOP_Login(const char* user,const char* password,MYSQL* connexion);

int nouveauClientDansBD(const char* user,const char* password,MYSQL* connexion);

void SMOP_Consult(int id,MYSQL* connexion,char* rep);

void SMOP_ACHAT(int id,MYSQL* connexion,char* rep,int qauntite);

void SMOP_Close();

#endif