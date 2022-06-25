package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private List<String> generi;
	private Map<Integer, Actor> actorIdMap;
	private List<Actor> vertici;
	//simulazione
	private Simulator sim;
	private List<Actor> intervistati;
	private int pause;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.generi = new ArrayList<>();
		this.actorIdMap = new HashMap<>();
		this.actorIdMap = this.dao.listAllActors();
	}
	
	public List<String> getGeneri(){
		this.generi = this.dao.getAllGenres();
		return this.generi;
	}
	
	public void creaGrafo(String genere) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungi i vertici
		this.vertici = new ArrayList<>(this.dao.getVertices(genere, actorIdMap));
		Graphs.addAllVertices(this.grafo, this.vertici);
		//Aggiungi gli archi
		for(Adiacenza a : this.dao.getArchi(genere, actorIdMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	public boolean isGraphCreated() {
		if(this.grafo == null) {
			return false;
		}
		return true;
	}
	public int nVertices() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<Actor> getVertices(){
		Collections.sort(this.vertici);
		return this.vertici;
	}
	
	public List<Actor> getSimili(Actor a){
		List<Actor> visitati = new ArrayList<>();
		GraphIterator<Actor, DefaultWeightedEdge> visita = new BreadthFirstIterator<>(this.grafo, a);
		while(visita.hasNext()) {
			visitati.add(visita.next());
		}
		visitati.remove(a);
		Collections.sort(visitati);
		return visitati;
	}
	
	public void simulazione(int n) {
		this.sim = new Simulator(this.grafo);
		this.sim.init(n);
		this.sim.run();
		this.intervistati = new ArrayList<>(this.sim.getIntervistati());
		this.pause = this.sim.getPause();
	}
	public List<Actor> intereviews(){
		return this.intervistati;
	}
	public int pauses() {
		return this.pause;
	}

}
