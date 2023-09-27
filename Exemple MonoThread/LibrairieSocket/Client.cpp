#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include "TCP.h"

int sClient;

void HandlerSIGINT(int s);

int main(int argc, char* argv[])
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

    if (sigaction(SIGINT, &A, NULL) == -1)
    {
        perror("Erreur de sigaction");
        exit(1);
    }

    // Connexion sur le serveur
    if ((sClient = ClientSocket(argv[1], atoi(argv[2]))) == -1)
    {
        perror("Erreur de ClientSocket");
        exit(1);
    }

    printf("Connecte sur le serveur.\n");
    char requete[200], reponse[200];
    int nbEcrits, nbLus;

    while (1)
    {
        printf("Requete a envoyer (<CTRL-C> pour fin) : ");
        fgets(requete, 200, stdin);
        requete[strlen(requete) - 1] = 0; // pour retirer le '\n'

        // ***** Envoi de la requete ******************
        if ((nbEcrits = Send(sClient, requete, strlen(requete))) == -1)
        {
            perror("Erreur de Send");
            HandlerSIGINT(0);
        }

        printf("Requete envoyee = %s\n", requete);

        // ***** Attente de la reponse ****************
        if ((nbLus = Receive(sClient, reponse)) < 0)
        {
            perror("Erreur de Receive");
            HandlerSIGINT(0);
        }

        if (nbLus == 0)
        {
            printf("Serveur arrete, pas de reponse recue...");
            HandlerSIGINT(0);
        }

        reponse[nbLus] = 0;
        printf("Reponse recue = %s\n", reponse);
    }
}

void HandlerSIGINT(int s)
{
    printf("\nArret du client.\n");
    close(sClient);
    exit(0);
}
