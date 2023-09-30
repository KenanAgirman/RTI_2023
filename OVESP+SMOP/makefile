.SILENT:

all:	Serveur Client

Serveur:	Serveur.cpp TCP.o OVESP.o
	echo "creation du Serveur"
	g++ Serveur.cpp TCP.o  OVESP.o -o Serveur -Wall -pthread

Client:	Client.cpp TCP.o OVESP.o
	echo "creation du Client"
	g++ Client.cpp TCP.o OVESP.o -o Client -Wall -pthread

TCP.o:	TCP.h TCP.cpp
	echo "Creation du TCP.o"
	g++ TCP.cpp -c -o TCP.o -Wall -pthread #-D DEBUG 

OVESP.o:	OVESP.h OVESP.cpp
	echo "Creation du OVESP.o"
	g++ OVESP.cpp -c -o OVESP.o -Wall  -pthread #-D DEBUG

clean:
	rm *.o