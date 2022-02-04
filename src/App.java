import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class App {
    public static void main(String[] args) {

        Map<Integer,Partido> mapPartidos = lePartidos("./testes/" + args[0] + "/in/partidos.csv");

        List<Candidato> listaCandidatos = leCandidatos("./testes/" + args[0] + "/in/candidatos.csv", mapPartidos);

        geraRelatorio(listaCandidatos, mapPartidos);        
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

                int numero = Integer.parseInt(data[0].trim());
                int votos_nominais = Integer.parseInt(data[1].trim());
                String situacao = data[2].trim();
                String nome = data[3].trim();
                String nome_urna = data[4].trim();
                char sexo = data[5].trim().charAt(0);
                String data_nasc = data[6].trim();
                String destino_voto = data[7].trim();
                int numero_partido = Integer.parseInt(data[8].trim());

                Candidato candidato = new Candidato(numero, votos_nominais, situacao, nome, nome_urna, sexo, data_nasc, destino_voto, numero_partido);

                if(destino_voto.equals("Válido")){
                    mapPartidos.get(numero_partido).adicionaCandidato(candidato);
                    mapPartidos.get(numero_partido).addVotos_nominais(votos_nominais);
                    listaCandidatos.add(candidato);
                }
                //listaCandidatos.add(candidato);

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
        catch(NumberFormatException e) {
            System.out.println("Problema na leitura de inteiros");
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
        Integer[] keyLista = mapa.keySet().toArray(new Integer[mapa.size()]);
        Arrays.sort(keyLista);
        for(Integer item: keyLista) {
            System.out.println(item + " - " + mapa.get(item));
        }
    }

    static void geraRelatorio(List<Candidato> listaCandidatos, Map<Integer,Partido> mapPartido) {

        //Relatorio 1
        //Percorre toda a lista de candidatos para contar o total de eleitos, que eh igual ao total de vagas
        List<Candidato> listaCandidatosEleitos = new LinkedList<Candidato>();

        int numeroVagas = 0;
        for(Candidato temp: listaCandidatos) {
            if(temp.getSituacao().equals("Eleito")) {
                listaCandidatosEleitos.add(temp);
                numeroVagas++;
            }
                
        }

        System.out.println("Número de vagas: " + numeroVagas + "\n");


        //Percorre toda a lista de candidatos e gera as listas para os relatorios de 2 a 5 (listas 1 a 4 respectivamente)
        List<Candidato> lista1 = new LinkedList<Candidato>();
        List<Candidato> lista2 = new LinkedList<Candidato>();
        Map<Integer,Candidato> lista3 = new HashMap<Integer,Candidato>();
        Map<Integer,Candidato> lista4 = new HashMap<Integer,Candidato>();
        
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

        //Relatorio 2
        System.out.println("Vereadores eleitos:");
        imprimeLista(lista1);
        System.out.println();

        //Relatorio 3
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        imprimeLista(lista2);
        System.out.println();

        //Relatorio 4
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");
        imprimeMapa(lista3);
        System.out.println();

        //Relatorio 5
        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");
        imprimeMapa(lista4);
        System.out.println();

        //Cria uma lista de partidos a partir do mapa da partidos
        List<Partido> listaPartidos = new LinkedList<Partido>();
        for(int j : mapPartido.keySet())
            listaPartidos.add(mapPartido.get(j));

        //Relatorio 6
        imprimeRelatorio6(listaPartidos);

        //Relatorio 7
        imprimeRelatorio7(listaPartidos);

        //Relatorio 8
        imprimeRelatorio8(listaPartidos);

        //Relatorio 9 e 10
        imprimeRelatorio9e10(listaCandidatosEleitos);

        //Relatorio 11
        imprimeRelatorio11(listaPartidos);
    }

    static void imprimeRelatorio6(List<Partido> listaPartidos) {
        Collections.sort(listaPartidos);
        int i = 1;
        System.out.println("Votação dos partidos e número de candidatos eleitos:");
        for(Partido p: listaPartidos)
            System.out.println(i++ + " - " + p + ((p.getVotos_nominais() + p.getVotos_legenda()) > 1 ?  (p.getVotos_nominais() + p.getVotos_legenda()) + " votos (" : (p.getVotos_nominais() + p.getVotos_legenda()) + " voto (") + (p.getVotos_nominais() > 1 ? p.getVotos_nominais() + " nominais e " : p.getVotos_nominais() + " nominal e ") + p.getVotos_legenda() + " de legenda), " + (p.getCandidatos_eleitos() > 1 ? p.getCandidatos_eleitos() + " candidatos eleitos" : p.getCandidatos_eleitos() + " candidato eleito"));
    }

    static void imprimeRelatorio7(List<Partido> lista) {
        Collections.sort(lista, new Partido.ComparadorVotoLegenda()); 

        int i = 1;
        String numero;
        System.out.println("\nVotação dos partidos (apenas votos de legenda):");
        for(Partido p: lista) {
            DecimalFormat numberFormat = new DecimalFormat("##0.00");

            numero = numberFormat.format( 100 * p.getVotos_legenda() / (double) (p.getVotos_legenda() + p.getVotos_nominais())) + "% do total do partido";
            if(numero.contains("NaN")){
                numero = " (proporção não calculada, 0 voto no partido";
            }   

            System.out.println(i++ + " - " + p + (p.getVotos_legenda() > 1 ? p.getVotos_legenda() + " votos de legenda (" : p.getVotos_legenda() + " voto de legenda") + numero + ")");
        }
            
    }

    static void imprimeRelatorio8(List<Partido> lista) {
        Collections.sort(lista,new Partido.ComparadorMaisVotado());
        int i = 1;
        System.out.println("\nPrimeiro e último colocados de cada partido:");
        for(Partido p: lista) {
            try {
                List<Candidato> tempList = p.getLista_candidatos();
                Collections.sort(tempList);
                Candidato primeiro = tempList.get(0);
                Candidato ultimo = tempList.get(tempList.size() - 1);

                if(primeiro.getDestino_voto().equals("Válido") || ultimo.getDestino_voto().equals("Válido")) {
                    System.out.print(i++ + " - " + p);
                    System.out.print(primeiro.getNome_urna() + " (" + primeiro.getNumero() + ", " + (primeiro.getVotos_nominais() > 1 ? primeiro.getVotos_nominais() + " votos" : primeiro.getVotos_nominais() + " voto") + ") / ");
                    System.out.println(ultimo.getNome_urna() + " (" + ultimo.getNumero() + ", " + (ultimo.getVotos_nominais() > 1 ? ultimo.getVotos_nominais() + " votos)" : ultimo.getVotos_nominais() + " voto)"));
                }
            }
            catch(NullPointerException e) {
                return;
            }
            catch(IndexOutOfBoundsException e) {
                return;
            }
            catch(Exception e) {
                return;
            }
        }
    }

    static void imprimeRelatorio9e10(List<Candidato> listaCandidatosEleitos) {
        int q1 = 0, q2 = 0, q3 = 0, q4 = 0, q5 = 0;
        int M = 0, F = 0;
        int total = listaCandidatosEleitos.size();
        int idadeTemp = 0;

        for(Candidato c: listaCandidatosEleitos) {
            idadeTemp = c.getIdade();
            
            if(idadeTemp < 30)
                q1++;
            else if(idadeTemp >= 30 && idadeTemp < 40)
                q2++;
            else if(idadeTemp >= 40 && idadeTemp < 50)
                q3++;
            else if(idadeTemp >= 50 && idadeTemp < 60)
                q4++;
            else
                q5++;

            if(c.getSexo() == 'F') F++;
            if(c.getSexo() == 'M') M++;
        }

        //Formato para as porcentagens dos relatorios
        DecimalFormat numberFormat = new DecimalFormat("##0.00");

        //Porcentagens para idades
        String n1 = numberFormat.format( 100 * q1 / (double) total) + "%";
        String n2 = numberFormat.format( 100 * q2 / (double) total) + "%";
        String n3 = numberFormat.format( 100 * q3 / (double) total) + "%";
        String n4 = numberFormat.format( 100 * q4 / (double) total) + "%";
        String n5 = numberFormat.format( 100 * q5 / (double) total) + "%";

        //Porcentagens para sexo
        String nf = numberFormat.format( 100 * F / (double) total) + "%";
        String nm = numberFormat.format( 100 * M / (double) total) + "%";

        System.out.println("\nEleitos, por faixa etária (na data da eleição):");
        System.out.println("      Idade < 30: " + q1 + " (" + n1 + ")\n30 <= Idade < 40: " + q2 + " (" + n2 + ")\n40 <= Idade < 50: " + q3 + " (" + n3 + ")\n50 <= Idade < 60: " + q4 + " (" + n4 + ")\n60 <= Idade     : " + q5 + " (" + n5 + ")\n");

        System.out.println("Eleitos, por sexo:");
        System.out.println("Feminino:  " + F + " (" + nf + ")\nMasculino: " + M + " (" + nm + ")\n");
    }
    
    static void imprimeRelatorio11(List<Partido> listaPartidos){
        int somaNominais = 0, somaLegenda = 0, somaTotal;
        for(Partido p : listaPartidos){
            somaNominais += p.getVotos_nominais();
            somaLegenda += p.getVotos_legenda();
        }
        somaTotal = somaNominais + somaLegenda;

        //Formato para as porcentagens do relatorio
        DecimalFormat numberFormat = new DecimalFormat("##0.##");

        //Porcentagens para as impressoes
        String n1 = numberFormat.format( 100 * somaNominais / (double) somaTotal) + "%";
        String n2 = numberFormat.format( 100 * somaLegenda / (double) somaTotal) + "%";

        System.out.println("Total de votos válidos:    " + somaTotal);
        System.out.println("Total de votos nominais:   " + somaNominais + " (" + n1 + ")");
        System.out.println("Total de votos de legenda: " + somaLegenda + " (" + n2 + ")\n");
    }
}