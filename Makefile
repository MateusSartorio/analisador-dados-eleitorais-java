all:
	rm -rf bin/*.class
	javac ./src/*.java -d ./bin/

belo-horizonte: all
	java -cp ./bin/ App belo-horizonte

cariacica: all
	java -cp ./bin/ App cariacica

rio-de-janeiro: all
	java -cp ./bin/ App rio-de-janeiro

sao-paulo: all
	java -cp ./bin/ App são-paulo

serra: all
	java -cp ./bin/ App serra

vila-velha: all
	java -cp ./bin/ App vila-velha

vitoria: all
	java -cp ./bin/ App vitória