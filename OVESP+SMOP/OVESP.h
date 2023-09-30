#ifndef SMOP_H
#define SMOP_H
#define NB_MAX_CLIENTS 100

bool SMOP(char* requete, char* reponse,int socket);

bool SMOP_Login(const char* user,const char* password);

void SMOP_Close();

#endif