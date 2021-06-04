package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	PremierLeagueDAO dao;
	private Graph<Player,DefaultWeightedEdge> grafo;
	private Map<Integer,Player> idMap;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<Integer,Player>();
		this.dao.listAllPlayers(idMap);
	}
	public void creaGrafo(Match m) {
		
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici 
		Graphs.addAllVertices(grafo, dao.getVertici(m, idMap));
		
		//aggiungi gli archi
		for (Adiacenza a : this.dao.getAdiacenze(m, this.idMap)) {
			if(a.getPeso()>=0) {
				//p1 meglio di p2
				if(grafo.containsVertex(a.getP1())&&grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(),a.getPeso());
				}
				
			} else {
				//p2 meglio di p1
				if(grafo.containsVertex(a.getP1())&&grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(),(-1)*a.getPeso());
				
			}
		}
		
	}
	
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<Match> getAllMaches(){
		return dao.listAllMatches();
	}
	public GiocatoreMigliore getMigliore() {
		if(grafo==null) {
			return null;
		}
		Player best = null;
		Double maxDelta= (double)Integer.MIN_VALUE;
		
		for(Player p: this.grafo.vertexSet()) {
			//calcolo la somma dei pesi degli archi uscenti 
			double pesoUscente=0.0;
			for(DefaultWeightedEdge edge :this.grafo.outgoingEdgesOf(p)) {
				pesoUscente += this.grafo.getEdgeWeight(edge);
			}
			// calcolo la somma dei pesi degli archi entranti 
			double  pesoEntrante =0.0;
			for(DefaultWeightedEdge edge :this.grafo.incomingEdgesOf(p)) {
				pesoUscente += this.grafo.getEdgeWeight(edge);
			}
			double delta = pesoUscente- pesoEntrante;
			if(delta>maxDelta) {
				best=p;
				maxDelta= delta;
			}
			
		}
		return new GiocatoreMigliore(best,maxDelta);
		
	}
	public Object getGrafo() {
		// TODO Auto-generated method stub
		return this.grafo;
	}
}
