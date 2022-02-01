import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class App {
    public static void main(String[] args) {

        Map<Integer,Partido> mapPartidos = lePartidos("./testes/" + args[0] + "/in/partidos.csv");

        List<Candidato> listaCandidatos = leCandidatos("./testes/" + args[0] + "/in/candidatos.csv", mapPartidos);

        geraRelatorio(listaCandidatos, mapPartidos);
        
        
        //imprimeLista(mapPartidos.get(90).getLista_candidatos());
        // imprimeLista(listaCandidatos);

        
    }

    static Map<Integer,Partido> lePartidos(String destino) {

        Map<Integer,Partido> mapPartidos = new HashMap<Integer,Partido>();

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
                mapPartidos.put(numero_partido, partido);
            }
            //Collections.sort(mapPartidos);

        }
        catch (FileNotFoundException e) {
            System.out.println("Arquivo nao encontrado");
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println("Problema ao abrir o arquivo");
            System.exit(1);
        }

        return mapPartidos;
    }

    static List<Candidato> leCandidatos(String destino, Map<Integer,Partido> mapPartidos) {

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
                mapPartidos.get(numero_partido).adicionaCandidato(candidato);
                if(destino_voto.equals("Válido")){
                    mapPartidos.get(numero_partido).addVotos_nominais(votos_nominais);
                }
                listaCandidatos.add(candidato);

                if(situacao.equals("Eleito")) {
                    mapPartidos.get(numero_partido).addCandidato_eleitos();
                }

            }
            Collections.sort(listaCandidatos);
            // Collections.sort(mapPartidos.keySet());
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

    static void geraRelatorio(List<Candidato> listaCandidatos, Map<Integer,Partido> mapPartido) {

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

        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");
        imprimeMapa(lista4);

        List<Partido> listaPartidos = new LinkedList<Partido>();

        for(int j : mapPartido.keySet())
            listaPartidos.add(mapPartido.get(j));

        System.out.println("\nVotação dos partidos e número de candidatos eleitos:");
        imprimeRelatorio6(listaPartidos);

        System.out.println("\nVotação dos partidos (apenas votos de legenda):");
        imprimeRelatorio7(listaPartidos);

        System.out.println("\nPrimeiro e último colocados de cada partido:");
        imprimeRelatorio8(listaPartidos);

        int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
        Candidato c = listaCandidatos.get(0);
        // for(Candidato c: listaCandidatos) {

            String[] dataAtual = new Date().toString().split(" ");
            String[] dataNascimento = c.getData_nasc().toString().split(" ");
            
            for(String s: dataAtual)
                System.out.print(s + ", ");
            
            System.out.println();

            for(String s: dataNascimento)
                System.out.print(s + ", ");
            
            System.out.println();

            // System.out.println(dataAtual);
            // System.out.println(dataNascimento);
        // }


    }

    static void imprimeRelatorio6(List<Partido> listaPartidos) {
        Collections.sort(listaPartidos);
        int i = 1;
        for(Partido p: listaPartidos)
            System.out.println(i++ + " - " + p + (p.getVotos_nominais() + p.getVotos_legenda()) + " votos (" + p.getVotos_nominais() + " nominais e " + p.getVotos_legenda() + " de legenda), " + p.getCandidatos_eleitos() + " candidatos eleitos");
    }

    static void imprimeRelatorio7(List<Partido> lista) {
        Collections.sort(lista, new Partido.ComparadorVotoLegenda()); 

        int i = 1;
        String numero;
        for(Partido p: lista) {
            DecimalFormat numberFormat = new DecimalFormat("###.##");

            numero = numberFormat.format( 100 * p.getVotos_legenda() / (double) (p.getVotos_legenda() + p.getVotos_nominais())) + "% do total do partido";
            if(numero.contains("NaN")){
                numero = "proporção não calculada, 0 voto no partido";
            }   

            System.out.println(i++ + " - " + p + p.getVotos_legenda() + " votos de legenda (" + numero + ")");
        }
            
    }

    static void imprimeRelatorio8(List<Partido> lista) {
        Collections.sort(lista,new Partido.ComparadorMaisVotado());
        int i = 1;
        for(Partido p: lista) {
            List<Candidato> tempList = p.getLista_candidatos();
            Collections.sort(tempList);
            Candidato primeiro = tempList.get(0);
            Candidato ultimo = tempList.get(tempList.size() - 1);

            if(primeiro.getDestino_voto().equals("Válido") || ultimo.getDestino_voto().equals("Válido")) {
                System.out.print(i++ + " - " + p);
                System.out.print(primeiro.getNome_urna() + " (" + primeiro.getNumero() + ", " + primeiro.getVotos_nominais() + " votos) / ");
                System.out.println(ultimo.getNome_urna() + " (" + ultimo.getNumero() + ", " + ultimo.getVotos_nominais() + " votos)");
            }
                   
        }
    }
}