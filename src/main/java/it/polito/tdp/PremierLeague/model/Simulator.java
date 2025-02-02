package it.polito.tdp.PremierLeague.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Event.EventType;

import org.jgrapht.Graph;

public class Simulator {

	//coda degli eventi
	private PriorityQueue<Event> queue;
	private Graph<Team, DefaultWeightedEdge> grafo;
	private Map<Integer,Team> idMap;
	
	//mondo degli eventi
	private List<Match> allMatches;
	private Map<Team,Integer> reporterPerTeam;
	private int totReport;
	
	//parametri in input
	private int numReporter;
	private int soglia;
	
	//parametri in output
	private int numMatchUnderX;
	private double reporPerMatch;
	
	
	public void init(int n, int x, Graph<Team,DefaultWeightedEdge> g, List<Match> list, Map<Integer,Team> map) {
		this.queue= new PriorityQueue<>();
		this.grafo=g;
		this.numReporter=n;
		this.soglia=x;
		this.idMap= new HashMap<>(map);
		this.totReport=0;
		
		this.allMatches= new ArrayList<>(list);
		this.reporterPerTeam=new HashMap<>();
		for (Team t : grafo.vertexSet()) {
			this.reporterPerTeam.put(t, numReporter);
		}
		
		this.numMatchUnderX=0;
		this.reporPerMatch=0.0;
		
		int inseriti=0;
		while (inseriti!=this.allMatches.size()) {
			
			Match m = allMatches.get(inseriti);
			Event e = new Event (m.getDate(),EventType.PREMATCH,m,null);
			this.queue.add(e);
			inseriti++;
		}
	}
	
	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		LocalDateTime data= e.getData();
		Match match = e.getMatch();
		Team t1= idMap.get(match.getTeamHomeID());
		Team t2= idMap.get(match.getTeamAwayID());
		
		switch (e.getType()) {
		case PREMATCH:
			int repor=this.reporterPerTeam.get(t1)+this.reporterPerTeam.get(t2); //reporter di questo match
			this.totReport += repor;
			if (repor<this.soglia) 
				this.numMatchUnderX++;
			if (match.getResultOfTeamHome()==1) {
				queue.add( new Event(data,EventType.VITTORIA,match,t1) ); //vittoria per t1
				queue.add( new Event(data,EventType.SCONFITTA,match,t2) ); //sconfitta per t2
			}
			else if (match.getResultOfTeamHome()==-1) {
				queue.add( new Event(data,EventType.SCONFITTA,match,t1) ); //sconfitta per t1
				queue.add( new Event(data,EventType.VITTORIA,match,t2) ); //vittoria per t2
			}
			break;
		case VITTORIA:
			Team teamV= e.getTeam();
			if(this.reporterPerTeam.get(teamV)!=0) {
				int prob= (int)(Math.random()*100);
				if (prob<50) {
					//int rep= (int)(Math.random()*this.reporterPerTeam.get(t1)); //scelgo un reporter a caso
					queue.add( new Event(data,EventType.PROMOZIONE,match,teamV) );
				}
			}
					/*for (int i=1; i<=this.reporterPerTeam.get(t2) ; i++) {
						int prob= (int)(Math.random()*100);
						if (prob<50) {
							queue.add( new Event(data,EventType.PROMOZIONE,match) );
						}
					}*/
			break;
		case SCONFITTA:
			Team teamS= e.getTeam();
			if(this.reporterPerTeam.get(teamS)!=0) {
				int prob= (int)(Math.random()*100);
				if (prob<20) {
					queue.add( new Event(data,EventType.BOCCIATURA,match,teamS) );
				}
			}
					/*for (int i=1; i<=this.reporterPerTeam.get(t2) ; i++) {
						int prob= (int)(Math.random()*100);
						if (prob<20) {
							queue.add( new Event(data,EventType.BOCCIATURA,match) );
						}
					}*/
			break;
		case PROMOZIONE:
			Team teamP= e.getTeam();
			List<SquadraMigliore> migliori= new ArrayList<>(this.SquadreCheBattono(teamP));
			if (!migliori.isEmpty()) {
				int sq= (int)(Math.random()*migliori.size());
				this.reporterPerTeam.put(teamP, this.reporterPerTeam.get(teamP)-1); //diminusico i reporter del team che ha vinto
				Team daMigliorare = migliori.get(sq).getT();
				this.reporterPerTeam.put(daMigliorare, this.reporterPerTeam.get(daMigliorare)+1); //aumento i reporter di un team a caso tra i migliori
			}
			break;
		case BOCCIATURA:
			Team teamB= e.getTeam();
			List<SquadraPeggiore> peggiori= new ArrayList<>(this.SquadreBattute(teamB));
			if (!peggiori.isEmpty()) {
				int sq= (int)(Math.random()*peggiori.size());
				int nRep= (int)(Math.random()*this.reporterPerTeam.get(teamB));
				this.reporterPerTeam.put(teamB, this.reporterPerTeam.get(teamB)-nRep); //diminusico i reporter del team che ha perso
				Team daMigliorare = peggiori.get(sq).getT();
				this.reporterPerTeam.put(daMigliorare, this.reporterPerTeam.get(daMigliorare)+nRep); //aumento i reporter di un team a caso tra i peggiori
			}
			break;
		}
	}
	
	private List<SquadraPeggiore> SquadreBattute(Team team){
		List<SquadraPeggiore> temp= new ArrayList<>();
		for (DefaultWeightedEdge e : grafo.outgoingEdgesOf(team)) {
				temp.add( new SquadraPeggiore(grafo.getEdgeTarget(e),grafo.getEdgeWeight(e) ));
		}
		Collections.sort(temp);
		return temp;
	}
	
	private List<SquadraMigliore> SquadreCheBattono(Team team){
		List<SquadraMigliore> temp= new ArrayList<>();
		for (DefaultWeightedEdge e : grafo.incomingEdgesOf(team)) {
				temp.add( new SquadraMigliore(grafo.getEdgeSource(e),grafo.getEdgeWeight(e) ));
		}
		Collections.sort(temp);
		return temp;
	}
	
	public double reporterForMatch() {
		this.reporPerMatch= ((double)this.totReport)/((double)this.allMatches.size());
		return this.reporPerMatch;
	}
	
	public int matchUnderSogliaX() {
		return this.numMatchUnderX;
	}
	
	
}
