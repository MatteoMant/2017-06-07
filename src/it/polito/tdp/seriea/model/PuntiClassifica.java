package it.polito.tdp.seriea.model;

public class PuntiClassifica implements Comparable<PuntiClassifica>{
	
	private Team team;
	private int punti;
	
	public PuntiClassifica(Team team, int punti) {
		super();
		this.team = team;
		this.punti = punti;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getPunti() {
		return punti;
	}

	public void setPunti(int punti) {
		this.punti = punti;
	}

	@Override
	public int compareTo(PuntiClassifica other) {
		return other.getPunti() - this.getPunti();
	}
	
}

