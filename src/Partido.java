import java.util.ArrayList;
import java.util.List;

public class Partido {
    private int numero_partido;
    private int votos_legenda;
    private String nome_Partido;
    private String sigla_partido;
    private List<Candidato> lista_candidatos;
	private int votos_nominais_totais;
	private int candidatos_eleitos;

    public Partido(int numero_partido, int votos_legenda, String nome_Partido, String sigla_partido) {
		this.numero_partido = numero_partido;
        this.votos_legenda = votos_legenda;
		this.nome_Partido = nome_Partido;
		this.sigla_partido = sigla_partido;
		this.lista_candidatos = new ArrayList<Candidato>();
		this.votos_nominais_totais = 0;
		this.candidatos_eleitos = 0;
	}

	public int getNumero_partido() {
		return numero_partido;
	}

	public void setNumero_partido(int numero_partido) {
		this.numero_partido = numero_partido;
	}

    public int getVotos_legenda() {
		return votos_legenda;
	}

	public void setVotos_legenda(int votos_legenda) {
		this.votos_legenda = votos_legenda;
	}
	
    public String getNome_Partido() {
		return nome_Partido;
	}

	public void setNome_Partido(String nome_Partido) {
		this.nome_Partido = nome_Partido;
	}
	
    public String getSigla_partido() {
		return sigla_partido;
	}

    public void setSigla_partido(String sigla_partido) {
		this.sigla_partido = sigla_partido;
	}
	
    public List<Candidato> getLista_candidatos() {
		return lista_candidatos;
	}
	public void setLista_candidatos(List<Candidato> lista_candidatos) {
		this.lista_candidatos = lista_candidatos;
	}

    public void adicionaCandidato(Candidato c){
        this.lista_candidatos.add(c);
        c.setPartido(this);
    }

    public void removeCandidato(Candidato c){
        this.lista_candidatos.remove(c);
    }

	public int getVotos_nominais() {
		return this.votos_nominais_totais;
	}

	public void addVotos_nominais(int valor) {
		this.votos_nominais_totais += valor;
	}

	public int getCandidatos_eleitos() {
		return this.candidatos_eleitos;
	}

	public void addCandidato_eleitos() {
		this.candidatos_eleitos++;
	}

    @Override
    public String toString(){
        return this.sigla_partido.toUpperCase() + " - " + this.numero_partido + ", ";
    }

}
