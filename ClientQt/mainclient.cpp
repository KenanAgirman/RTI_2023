#include "windowclient.h"
#include "../OVESP+SMOP/TCP.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>

#include <QApplication>

void HandlerSIGINT(int);

WindowClient *w;
int sClient;

int main(int argc, char *argv[])
{
    if(argc != 3)
    {
      printf("Erreur...\n");
      printf("USAGE : ServeurTest portServeur\n");
      exit(1);
    }

    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;
    
    if((sClient = ServerSocket(atoi(argv[1]))) == -1)
    {
      perror("Erreur de ServeurSocket");
      exit(1);
    }

    printf("socket client = %d\n",sClient);
    QApplication a(argc, argv);
    w = new WindowClient();
    w->show();
    return a.exec();
}
void HandlerSIGINT(int n)
{
    printf("\nArret du client.\n");
    close(sClient);
    exit(0);
}