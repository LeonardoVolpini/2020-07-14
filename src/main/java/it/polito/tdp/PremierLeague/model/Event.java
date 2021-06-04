package it.polito.tdp.PremierLeague.model;

import java.time.LocalDateTime;

public class Event implements Comparable<Event> {
	
	public enum EventType{
		PREMATCH,
		VITTORIA,
		//PAREGGIO,
		SCONFITTA,
		PROMOZIONE,
		BOCCIATURA
	};
	private LocalDateTime data;
	private EventType type;
	private Match match;
	
	public Event(LocalDateTime data, EventType type, Match match) {
		super();
		this.data = data;
		this.type = type;
		this.match = match;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	@Override
	public String toString() {
		return "Event [data=" + data + ", type=" + type + ", match=" + match + "]";
	}

	@Override
	public int compareTo(Event o) {
		return this.data.compareTo(o.data);
	}
}
