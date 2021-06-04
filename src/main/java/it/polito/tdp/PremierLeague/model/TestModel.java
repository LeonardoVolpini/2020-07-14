package it.polito.tdp.PremierLeague.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		Model m= new Model();
		m.CreaGrafo();
		Team t1= new Team (54,"Fulham");
		Team t2= new Team(1,"Manchester United");
		//List<Team> r1=m.SquadreBattute(t1);
		//List<Team> r2=m.SquadreCheBattono(t1);
		//System.out.println(r1);
		//System.out.println(r2);
	}

}
