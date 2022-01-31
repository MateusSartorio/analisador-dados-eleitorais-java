import java.io.*;
import java.util.*;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class App {
    public static void main(String[] args) {

        Map<Integer,Partido> listaPartidos = lePartidos("./testes/" + args[0] + "/in/partidos.csv");

        List<Candidato> listaCandidatos = leCandidatos("./testes/" + args[0] + "/in/candidatos.csv", listaPartidos);

        geraRelatorio(listaCandidatos);
        
        
        //imprimeLista(listaPartidos.get(90).getLista_candidatos());
        // imprimeLista(listaCandidatos);

        
    }

    static Map<Integer,Partido> lePartidos(String destino) {

        Map<Integer,Partido> listaPartidos = new HashMap<Integer,Partido>();

        try(FileInputStream arquivo = new FileInputStream(destino)) {
            InputStreamReader arquivoR = new InputStreamReader(arquivo, "UTF-8");

            BufferedReader bufferArquivo = new BufferedReader(arquivoR);
            bufferArquivo.readLine();            

            String linha;

            while((linha = bufferArquivo.readLine()) != null) {
                String[] data = linha.split(",");

                int numero_partido = Integer.parseInt(data[0]);
                int votos_legenda = Integer.parseInt(data[1]);
                String nome_partido = data[2];
                String sigla_partido = data[3];

                Partido partido = new Partido(numero_partido, votos_legenda, nome_partido, sigla_partido);
                listaPartidos.put(numero_partido, partido);
            }
            //Collections.sort(listaPartidos);

        }
        catch (FileNotFoundException e) {
            System.out.println("Arquivo nao encontrado");
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println("Problema ao abrir o arquivo");
            System.exit(1);
        }

        return listaPartidos;
    }

    static List<Candidato> leCandidatos(String destino, Map<Integer,Partido> listaPartidos) {

        List<Candidato> listaCandidatos = new LinkedList<Candidato>();

        try(FileInputStream arquivo = new FileInputStream(destino)) {
            InputStreamReader arquivoR = new InputStreamReader(arquivo, "UTF-8");

            BufferedReader bufferArquivo = new BufferedReader(arquivoR);
            bufferArquivo.readLine();            

            String linha;

            while((linha = bufferArquivo.readLine()) != null) {
                String[] data = linha.split(",");

                int numero = Integer.parseInt(data[0]);
                int votos_nominais = Integer.parseInt(data[1]);
                String situacao = data[2];
                String nome = data[3];
                String nome_urna = data[4];
                char sexo = data[5].charAt(0);
                String data_nasc = data[6];
                String destino_voto = data[7];
                int numero_partido = Integer.parseInt(data[8]);

                Candidato candidato = new Candidato(numero, votos_nominais, situacao, nome, nome_urna, sexo, data_nasc, destino_voto, numero_partido);
                listaPartidos.get(numero_partido).adicionaCandidato(candidato);
                listaCandidatos.add(candidato);

            }
            Collections.sort(listaCandidatos);
        }
        catch (FileNotFoundException e) {
            System.out.println("Arquivo nao encontrado");
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println("Problema ao abrir o arquivo");
            System.exit(1);
        }

        return listaCandidatos;
    }

    static <T> void imprimeLista(List<T> lista) {
        int i = 1;
        for(T elemento: lista) {
            System.out.print(i++ + " - ");
            System.out.println(elemento);
        }
            
    }

    static <T> void imprimeMapa(Map<Integer, T> mapa) {
        for(Integer item: mapa.keySet()) {
            System.out.println(item + " - " + mapa.get(item));
        }
    }

    static void geraRelatorio(List<Candidato> listaCandidatos) {

        int numeroVagas = 0;
        for(Candidato temp: listaCandidatos) {
            if(temp.getSituacao().equals("Eleito"))
                numeroVagas++;
        }

        System.out.println("Número de vagas: " + numeroVagas + "\n");

        List<Candidato> lista1 = new LinkedList<Candidato>();
        List<Candidato> lista2 = new LinkedList<Candidato>();
        Map<Integer,Candidato> lista3 = new HashMap<Integer,Candidato>();
        Map<Integer, Candidato> lista4 = new HashMap<Integer,Candidato>();

        int i = 1;
        for(Candidato temp: listaCandidatos) {

            if(temp.getSituacao().equals("Eleito"))
                lista1.add(temp);
            
            if(i <= numeroVagas)
                lista2.add(temp);
            
            if(i <= numeroVagas && ( temp.getSituacao().equals("Suplente") || temp.getSituacao().equals("Não eleito") ))
                lista3.put(i,temp);
            
            if(i > numeroVagas && temp.getSituacao().equals("Eleito"))
                lista4.put(i,temp);
            
            i++;
        }

        System.out.println("Vereadores eleitos:");
        imprimeLista(lista1);
        System.out.println();

        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        imprimeLista(lista2);
        System.out.println();

        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");
        imprimeMapa(lista3);
        System.out.println();

        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
        imprimeMapa(lista4);

        


    }
}