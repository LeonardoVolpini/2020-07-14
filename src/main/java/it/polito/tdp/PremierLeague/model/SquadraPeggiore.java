package it.polito.tdp.PremierLeague.model;

public class SquadraPeggiore implements Comparable<SquadraPeggiore>{

	private Team t;
	private Double peso;
	public SquadraPeggiore(Team t, Double peso) {
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
	
	public int compareTo(SquadraPeggiore o) {
		return Double.compare(this.peso, o.peso);
	}
	
}
