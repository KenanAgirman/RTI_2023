.SILENT:

Serv = Serveur
SocketLib = SocketLib
CLIENTQT = ClientQt
OVESPSMOP = OVESPSMOP
BD = BD_Maraicher

CXXFLAGS  = g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o 

all:	Serveur Client CreationBD

Serveur:	$(Serv)/Serveur.cpp $(OVESPSMOP)/TCP.o $(OVESPSMOP)/OVESP.o
	echo "Creation du Serveur"
	g++ $(Serv)/Serveur.cpp $(OVESPSMOP)/TCP.o OVESPSMOP/OVESP.o -o ServeurFinal -lpthread -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl

OVESPSMOP/OVESP.o:	$(OVESPSMOP)/OVESP.h $(OVESPSMOP)/OVESP.cpp
	echo "Creation du OVESP.o"
	g++ $(OVESPSMOP)/OVESP.cpp -c -o $(OVESPSMOP)/OVESP.o -Wall -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl #-D DEBUG


Client:	$(CLIENTQT)/mainclient.o $(CLIENTQT)/windowclient.o $(CLIENTQT)/moc_windowclient.o
	echo "Creation du Client"
	g++ -Wno-unused-parameter -o Client $(CLIENTQT)/mainclient.o $(CLIENTQT)/windowclient.o $(CLIENTQT)/moc_windowclient.o $(OVESPSMOP)/TCP.o  /usr/lib64/libQt5Widgets.so /usr/lib64/libQt5Gui.so /usr/lib64/libQt5Core.so /usr/lib64/libGL.so -lpthread


$(OVESPSMOP)/TCP.o:	$(OVESPSMOP)/TCP.h $(OVESPSMOP)/TCP.cpp
	echo "Creation du TCP.o"
	g++ $(OVESPSMOP)/TCP.cpp -c -o $(OVESPSMOP)/TCP.o -Wall #-D DEBUG

$(CLIENTQT)/moc_windowclient.o:	$(CLIENTQT)/moc_windowclient.cpp
	echo "Creation du moc_windowclient.o"
	$(CXXFLAGS) $(CLIENTQT)/moc_windowclient.o $(CLIENTQT)/moc_windowclient.cpp

$(CLIENTQT)/windowclient.o:	$(CLIENTQT)/windowclient.cpp
	echo "Creation du windowclient.o"
	$(CXXFLAGS) $(CLIENTQT)/windowclient.o $(CLIENTQT)/windowclient.cpp

$(CLIENTQT)/mainclient.o:	$(CLIENTQT)/mainclient.cpp
	echo "Creation du mainclient.o"
	$(CXXFLAGS) $(CLIENTQT)/mainclient.o $(CLIENTQT)/mainclient.cpp

CreationBD:	$(BD)/CreationBD.cpp
	echo "Creation de CreationBD"
	g++ -o CreationBD $(BD)/CreationBD.cpp -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl

clean:
	rm -f OVESPSMOP/*.o  CreationBD/*.o $(CLIENTQT)/*.o
