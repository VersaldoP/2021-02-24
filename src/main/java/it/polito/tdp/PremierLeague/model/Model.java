package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	
	private PremierLeagueDAO dao;
	private DefaultDirectedWeightedGraph<Player,DefaultWeightedEdge> grafo;
	private Map<Integer,Player> idMap;
	private List<Adiacenze> archi;
	
	
	
	public Model() {
		dao = new PremierLeagueDAO();
		
	}

	public List<Match> getAllMaches() {
		// TODO Auto-generated method stub
		return dao.listAllMatches();
	}
	
	public void creaGrafo(Match m) {
		grafo= new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		idMap= new HashMap<>();
		dao.listAllPlayersMatch(idMap, m);
		archi = new ArrayList<>(dao.listAllAdiacenze(idMap, m));
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		for(Adiacenze a :archi) {
			if(a.getPeso()>0) {
				if(idMap.containsKey(a.getP1()))
				Graphs.addEdge(grafo, a.getP1(), a.getP2(), a.getPeso());
			}
			else
				if(idMap.containsKey(a.getP2()))
				Graphs.addEdge(grafo, a.getP2(), a.getP1(), -a.getPeso());
		}
	}

	public String nVertici() {
		// TODO Auto-generated method stub
		return this.grafo.vertexSet().size()+"";
	}

	public String nArchi() {
		// TODO Auto-generated method stub
		return this.grafo.edgeSet().size()+"";
	}
	
}
