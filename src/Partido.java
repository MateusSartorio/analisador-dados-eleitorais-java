import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Partido implements Comparable<Partido> {
    private int numero_partido;
    private int votos_legenda;
    private String nome_Partido;
    private String sigla_partido;
    private List<Candidato> lista_candidatos;
	private int votos_nominais_totais;
	private int candidatos_eleitos;
	private Candidato candidato_mais_votado;

    public Partido(int numero_partido, int votos_legenda, String nome_Partido, String sigla_partido) {
		this.numero_partido = numero_partido;
        this.votos_legenda = votos_legenda;
		this.nome_Partido = nome_Partido;
		this.sigla_partido = sigla_partido;
		this.lista_candidatos = new ArrayList<Candidato>();
		this.votos_nominais_totais = 0;
		this.candidatos_eleitos = 0;
		this.setCandidato_mais_votado(null);
	}

	public Candidato getCandidato_mais_votado() {
		return candidato_mais_votado;
	}

	public void setCandidato_mais_votado(Candidato candidato_mais_votado) {
		this.candidato_mais_votado = candidato_mais_votado;
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

		if(this.candidato_mais_votado == null) {
			this.candidato_mais_votado = c;
		}
		else if(c.getVotos_nominais() > this.candidato_mais_votado.getVotos_nominais()) {
			this.candidato_mais_votado = c;
		}
		else if(c.getVotos_nominais() == this.candidato_mais_votado.getVotos_nominais()) {
			if(c.getNumero() < this.candidato_mais_votado.getNumero()) {
				this.candidato_mais_votado = c;
			}
			else if(c.getData_nasc().compareTo(this.candidato_mais_votado.getData_nasc()) < 0) {
				this.candidato_mais_votado = c;
			}
		}
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
        return this.sigla_partido + " - " + this.numero_partido + ", ";
    }

	public int compareTo(Partido p){
		int diff = (p.getVotos_nominais()+p.getVotos_legenda())-(this.votos_nominais_totais+this.votos_legenda);
        if (diff == 0){
            if(p.getNumero_partido() < this.numero_partido){
                return 1;
            }else{
                return -1;
            }
        }
        return diff;
	}

	public static class ComparadorVotoLegenda implements Comparator<Partido>{
		public int compare(Partido p1, Partido p2){
			int diff1 = p2.votos_legenda - p1.votos_legenda;
			if (diff1 == 0){
				int diff2 = p2.votos_nominais_totais - p1.votos_nominais_totais;
				if(diff2 == 0){
					if(p2.numero_partido < p1.numero_partido){
						return 1;
					}else{
						return -1;
					}
				}
				return diff2;
			}
			
			return diff1;
		}
	}

	public static class ComparadorMaisVotado implements Comparator<Partido>{
		public int compare(Partido p1, Partido p2){
			int diff = p2.getCandidato_mais_votado().getVotos_nominais() - p1.getCandidato_mais_votado().getVotos_nominais();
				if(diff == 0){
					if (p2.getCandidato_mais_votado().getData_nasc().compareTo(p1.getCandidato_mais_votado().getData_nasc()) > 0){
						return 1;
					}else{
						return -1;
					}
				}
			return diff;
		}
	}

}
