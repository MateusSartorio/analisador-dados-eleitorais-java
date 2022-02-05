all:
	rm -rf bin/*.class
	javac ./src/*.java -d ./bin/

belo-horizonte: all
	java -cp ./bin/ App belo-horizonte > belo-horizonte.txt
	diff -s ./belo-horizonte.txt ./testes/belo-horizonte/out/output.txt

cariacica: all
	java -cp ./bin/ App cariacica > cariacica.txt
	diff -s ./cariacica.txt ./testes/cariacica/out/output.txt

rio-de-janeiro: all
	java -cp ./bin/ App rio-de-janeiro > rio-de-janeiro.txt
	diff -s ./rio-de-janeiro.txt ./testes/rio-de-janeiro/out/output.txt

sao-paulo: all
	java -cp ./bin/ App são-paulo > sao-paulo.txt
	diff -s ./sao-paulo.txt ./testes/são-paulo/out/output.txt

serra: all
	java -cp ./bin/ App serra > serra.txt
	diff -s ./serra.txt ./testes/serra/out/output.txt

vila-velha: all
	java -cp ./bin/ App vila-velha > vila-velha.txt
	diff -s ./vila-velha.txt ./testes/vila-velha/out/output.txt

vitoria: all
	java -cp ./bin/ App vitória > vitoria.txt
	diff -s ./vitoria.txt ./testes/vitória/out/output.txt

ant:
	ant clean
	ant compile
	ant jar