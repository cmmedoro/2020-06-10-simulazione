package it.polito.tdp.imdb.model;

public class Event implements Comparable<Event> {
	
	public enum EventType{
		DA_INTERVISTARE,
		PAUSA
	}
	
	private int nGiorno;
	private EventType tipo;
	public Event(int nGiorno, EventType tipo) {
		super();
		this.nGiorno = nGiorno;
		this.tipo = tipo;
	}
	public int getnGiorno() {
		return nGiorno;
	}
	public void setnGiorno(int nGiorno) {
		this.nGiorno = nGiorno;
	}
	public EventType getTipo() {
		return tipo;
	}
	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}
	@Override
	public int compareTo(Event o) {
		return this.nGiorno-o.nGiorno;
	}

	
	
	

}
