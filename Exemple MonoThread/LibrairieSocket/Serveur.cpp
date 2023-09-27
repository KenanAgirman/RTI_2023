#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include "TCP.h"

int sEcoute;
int sService;

void HandlerSIGINT(int s);
void TraitementConnexion();

int main(int argc, char* argv[])
{
    if (argc != 2)
    {
        printf("Erreur...\n");
        printf("USAGE : Serveur portServeur\n");
        exit(1);
    }

    // Armement des signaux
    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;

    if (sigaction(SIGINT, &A, NULL) == -1)
    {
        perror("Erreur de sigaction");
        exit(1);
    }

    // Creation de la socket d'écoute
    if ((sEcoute = ServerSocket(atoi(argv[1]))) == -1)
    {
        perror("Erreur de ServeurSocket");
        exit(1);
    }

    // Mise en boucle du serveur
    printf("Demarrage du serveur.\n");

    while (1)
    {
        printf("Attente d'une connexion...\n");

        if ((sService = Accept(sEcoute, NULL)) == -1)
        {
            perror("Erreur de Accept");
            close(sEcoute);
            exit(1);
        }

        printf("Connexion acceptee !\n");

        // Traitement de la connexion
        TraitementConnexion();
    }
}

void HandlerSIGINT(int s)
{
    printf("\nArret du serveur.\n");
    close(sEcoute);
    close(sService);
    exit(0);
}

void TraitementConnexion()
{
    char requete[200], reponse[200];
    int nbLus, nbEcrits;

    while (1)
    {
        printf("\tAttente requete...\n");

        // ***** Reception Requete ******************
        if ((nbLus = Receive(sService, requete)) < 0)
        {
            perror("Erreur de Receive");
            HandlerSIGINT(0);
        }

        // ***** Fin de connexion ? *****************
        if (nbLus == 0)
        {
            printf("\tFin de connexion du client.\n");
            close(sService);
            return;
        }

        requete[nbLus] = 0;
        printf("\tRequete recue = %s\n", requete);

        // ***** Traitement de la requete ***********
        sprintf(reponse, "[SERVEUR] %s", requete);

        // ***** Envoi de la reponse ****************
        if ((nbEcrits = Send(sService, reponse, strlen(reponse))) < 0)
        {
            perror("Erreur de Send");
            HandlerSIGINT(0);
        }

        printf("\tReponse envoyee = %s\n", reponse);
    }
}
