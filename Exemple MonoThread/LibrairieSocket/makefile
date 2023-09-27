.SILENT:

CXX = g++

CXXFLAGS = -Wall

all: Serveur Client

Serveur: Serveur.cpp TCP.o
	@echo "Compilation du serveur"
	$(CXX) $^ -o $@ $(CXXFLAGS)

Client: Client.cpp TCP.o
	@echo "Compilation du client"
	$(CXX) $^ -o $@ $(CXXFLAGS)

TCP.o: TCP.h TCP.cpp
	@echo "Compilation de TCP.o"
	$(CXX) -c TCP.cpp -o $@ $(CXXFLAGS)

clean:
	rm *.o