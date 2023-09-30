.SILENT:

all:	Serveur Client

Serveur:	Serveur.cpp TCP.o
	echo "creation du Serveur"
	g++ Serveur.cpp TCP.o -o Serveur -Wall

Client:	Client.cpp TCP.o
	echo "creation du Client"
	g++ Client.cpp TCP.o -o Client -Wall

TCP.o:	TCP.h TCP.cpp
	echo "Creation du TCP.o"
	g++ TCP.cpp -c -o TCP.o -Wall #-D DEBUG

clean:
	rm *.o