package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge> grafo;
	private Map<Integer,Team> idMap;
	private List<Team> classifica;
	private boolean grafoCreato;
	
	private List<Match> allMatches;
	private Simulator sim;
	
	public Model() {
		this.dao= new PremierLeagueDAO();
		this.idMap= new HashMap<>();
		this.classifica= new ArrayList<>();
		this.grafoCreato=false;
		this.allMatches=new ArrayList<>();
		this.sim= new Simulator();
	}
	
	public void CreaGrafo() {
		this.grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao.loadIdMap(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		dao.setVittorieAllTeamInCasa(idMap);
		dao.setVittorieAllTeamFuoriCasa(idMap);
		dao.setPareggiAllTeamInCasa(idMap);
		dao.setPareggiAllTeamFuoriCasa(idMap);
		
		//ora calcolo classifica:  //forse lista classifica è inutile
		for (Team t : idMap.values()) {
			t.setPunti();
			this.classifica.add(t);
		}
		Collections.sort(classifica);
		for (Team t1 : classifica) {
			for (Team t2 : classifica) {
				double diff= t1.getPunti()-t2.getPunti();
				if (diff!=0) {
					if (diff >0)
						Graphs.addEdgeWithVertices(grafo, t1, t2, diff);
					else
						Graphs.addEdgeWithVertices(grafo, t2, t1, -diff);
				}
			}
		}
		this.grafoCreato=true;
		System.out.println("GRAFO CREATO!!");
		System.out.println("# vertici: "+grafo.vertexSet().size());
		System.out.println("# archi: "+grafo.edgeSet().size());
	}
	
	public List<Team> getAllTeams(){
		return this.dao.listAllTeams();
	}
	
	public List<SquadraPeggiore> SquadreBattute(Team team){
		List<SquadraPeggiore> temp= new ArrayList<>();
		for (DefaultWeightedEdge e : grafo.outgoingEdgesOf(team)) {
				temp.add( new SquadraPeggiore(grafo.getEdgeTarget(e),grafo.getEdgeWeight(e) ));
		}
		Collections.sort(temp);
		return temp;
	}
	
	public List<SquadraMigliore> SquadreCheBattono(Team team){
		List<SquadraMigliore> temp= new ArrayList<>();
		for (DefaultWeightedEdge e : grafo.incomingEdgesOf(team)) {
				temp.add( new SquadraMigliore(grafo.getEdgeSource(e),grafo.getEdgeWeight(e) ));
		}
		Collections.sort(temp);
		return temp;
	}
	
	public int getNumVertici() {
		return grafo.vertexSet().size();
	}
	public int getNumArchi() {
		return grafo.edgeSet().size();
	}

	public boolean isGrafoCreato() {
		return grafoCreato;
	}
	
	public void simula(int n, int x) {
		this.allMatches=dao.listAllMatches();
		this.sim.init(n, x, grafo, allMatches, idMap);
		this.sim.run();
	}
	
	public double reporterForMatch() {
		return this.sim.reporterForMatch();
	}
	
	public int matchUnderSogliaX() {
		return this.sim.matchUnderSogliaX();
	}
	
}
