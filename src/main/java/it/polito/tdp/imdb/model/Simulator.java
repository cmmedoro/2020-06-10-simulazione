package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Event.EventType;

public class Simulator {
	
	//dati in ingresso
	private int n;
	
	//dati in uscita
	private List<Actor> intervistati;
	private int numPause;
	
	//modello del mondo
	private Set<Actor> validi; //vertici del grafo fra cui scegliere per le interviste
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	//costruttore
	public Simulator(Graph<Actor, DefaultWeightedEdge> g) {
		this.grafo = g;
		this.validi = new HashSet<>(this.grafo.vertexSet());
	}
	
	//inizializzo
	public void init(int n) {
		this.n = n;
		this.intervistati = new ArrayList<>();
		this.numPause = 0;
		this.queue = new PriorityQueue<>();
		//inizializzo la coda degli eventi con una scelta casuale fra tutti gli attori presenti nel vertexset
		Actor intervistato = this.selezionaIntervistato(this.validi);
		this.intervistati.add(intervistato);
		this.queue.add(new Event(1, EventType.DA_INTERVISTARE));
	}
	
	public void run() {
		while(!this.queue.isEmpty() && this.intervistati.size() < this.n) {
			Event e = this.queue.poll();
			if(e.getnGiorno() < this.n) {
				processEvent(e);
			}
		}
	}
	
	private void processEvent(Event e) {
		switch(e.getTipo()) {
		case DA_INTERVISTARE:
			int giorno = e.getnGiorno();
			boolean generiUguali = false;
			if(intervistati.size() > 1) {
				Actor a1 = this.intervistati.get(this.intervistati.size()-1);
				Actor a2 = this.intervistati.get(this.intervistati.size()-2);
				if(a1.getGender().equals(a2.getGender())) {
					generiUguali = true;
				}
			}
			if(generiUguali == true) {
				if(Math.random() < 0.9) {
					this.queue.add(new Event(e.getnGiorno()+1, EventType.PAUSA));
				}else {
					Actor scelto = this.selezionaIntervistato(this.validi);
					this.intervistati.add(scelto);
					this.queue.add(new Event(e.getnGiorno()+1, EventType.DA_INTERVISTARE));
				}
			}else {
				double caso = Math.random();
				if(caso < 0.6) {
					//scelta casuale fra quelli che rimangono da intervistare
					Actor scelto = this.selezionaIntervistato(this.validi);
					this.intervistati.add(scelto);
					this.queue.add(new Event(e.getnGiorno()+1, EventType.DA_INTERVISTARE));
				}else {
					List<Actor> scelte = this.vicinoGradoMassimo(this.intervistati.get(this.intervistati.size()-1));
					if(scelte.size() == 0 || scelte == null) { //scelta casuale
						Actor scelto = this.selezionaIntervistato(this.validi);
						this.intervistati.add(scelto);
						this.queue.add(new Event(e.getnGiorno()+1, EventType.DA_INTERVISTARE));
					}else {
						int scelto = (int)(Math.random()*scelte.size());
						Actor actor = scelte.get(scelto);
						this.intervistati.add(actor);
						this.queue.add(new Event(e.getnGiorno()+1, EventType.DA_INTERVISTARE));
					}
				}
			}
			break;
			
		case PAUSA:
			//scelgo attore casuale fra quelli rimanenti da intervistare
			Actor choice = this.selezionaIntervistato(this.validi);
			this.intervistati.add(choice);
			this.queue.add(new Event(e.getnGiorno()+1, EventType.DA_INTERVISTARE));
			break;
		}
		
	}

	private Actor selezionaIntervistato(Collection<Actor> lista) {
		Set<Actor> candidati = new HashSet<>(lista);
		//tolgo dai candidati tutti coloro che sono già stati intervistati
		candidati.removeAll(this.intervistati);
		//estraggo un numero casuale fra 0 e la dimensione dei candidati
		int scelto = (int)(Math.random()*candidati.size());
		return (new ArrayList<>(candidati).get(scelto));
	}
	
	private List<Actor> vicinoGradoMassimo(Actor a){
		List<Actor> vicini = Graphs.neighborListOf(this.grafo, a);
		vicini.removeAll(intervistati);
		if(vicini.size() == 0) {
			return null;
		}
		double max = 0;
		for(Actor aa : vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(a, aa));
			if(peso > max) {
				max = peso;
			}
		}
		List<Actor> migliori = new ArrayList<>();
		for(Actor ac : vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(a, ac));
			if(peso == max) {
				migliori.add(ac);
			}
		}
		/*int scelto = (int)(Math.random()*migliori.size());
		return migliori.get(scelto);*/
		return migliori;
	}
	
	public List<Actor> getIntervistati(){
		return this.intervistati;
	}
	public int getPause() {
		return this.n-this.intervistati.size();
	}

}
