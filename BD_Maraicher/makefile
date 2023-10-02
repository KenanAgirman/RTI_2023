.SILENT:

all:	CreationBD

CreationBD:	CreationBD.cpp
	g++ -o CreationBD CreationBD.cpp -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl

clean:
	rm -f CreationBD
