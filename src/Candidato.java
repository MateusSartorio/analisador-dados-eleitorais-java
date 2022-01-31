import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Candidato implements Comparable<Candidato> {
    private int numero;
    private int votos_nominais;
    private String situacao;
    private String nome;
    private String nome_urna;
    private char sexo;
    private Date data_nasc;
    private String destino_voto;
    private int numero_partido;
    private Partido partido;


    public Candidato(int numero, int votos_nominais, String situacao, String nome, String nome_urna, char sexo, String data_nasc, String destino_voto, int numero_partido) {
        this.numero = numero;
        this.votos_nominais = votos_nominais;
        this.situacao = situacao;
        this.nome = nome;
        this.nome_urna = nome_urna;
        this.sexo = sexo;
        try {
            this.data_nasc = new SimpleDateFormat("dd/MM/yyyy").parse(data_nasc);
        }
        catch (ParseException e) {
            System.out.println("Deu ruim a data");
            System.exit(1);
        }
        this.destino_voto = destino_voto;
        this.numero_partido = numero_partido;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getVotos_nominais() {
        return votos_nominais;
    }

    public void setVotos_nominais(int votos_nominais) {
        this.votos_nominais = votos_nominais;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome_urna() {
        return nome_urna;
    }

    public void setNome_urna(String nome_urna) {
        this.nome_urna = nome_urna;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public Date getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(Date data_nasc) {
        this.data_nasc = data_nasc;
    }

    public String getDestino_voto() {
        return destino_voto;
    }

    public void setDestino_voto(String destino_voto) {
        this.destino_voto = destino_voto;
    }

    public int getNumero_partido() {
        return numero_partido;
    }

    public void setNumero_partido(int numero_partido) {
        this.numero_partido = numero_partido;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }
    
    @Override
    public String toString() {
        return this.nome.toUpperCase() + " / " + this.nome_urna.toUpperCase() + " (" + this.partido.getNome_Partido().toUpperCase() + ", " + this.votos_nominais + " votos)";
    }

    public int compareTo(Candidato c){
        int diff = c.votos_nominais - this.votos_nominais;
        if (diff == 0){
            if(c.getData_nasc().compareTo(this.data_nasc) < 0){
                return 1;
            }else{
                return -1;
            }
        }
        return diff;
    }
}