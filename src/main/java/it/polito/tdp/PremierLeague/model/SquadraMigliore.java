package it.polito.tdp.PremierLeague.model;

public class SquadraMigliore implements Comparable<SquadraMigliore> {

	private Team t;
	private Double peso;
	public SquadraMigliore(Team t, Double peso) {
		super();
		this.t = t;
		this.peso = peso;
	}
	public Team getT() {
		return t;
	}
	public void setT(Team t) {
		this.t = t;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(SquadraMigliore o) {
		return Double.compare(this.peso, o.peso);
	}
	
	
}
