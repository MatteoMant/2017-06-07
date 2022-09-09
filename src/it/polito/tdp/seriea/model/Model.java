package it.polito.tdp.seriea.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	private Map<String, Team> teamIdMap;
	private Graph<Team, DefaultWeightedEdge> grafo;
	
	// Variabili per la ricorsione
	private List<Team> best;
	
	public Model() {
		dao = new SerieADAO();
		teamIdMap = new HashMap<>();
		dao.listTeams(teamIdMap);
	}
	
	public void creaGrafo(Season season) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiunta dei vertici
		Graphs.addAllVertices(this.grafo, dao.listTeamsWithSeason(season));	
		
		// Aggiunta degli archi
		for (Adiacenza a : dao.getAllAdiacenze(season, teamIdMap)) {
			Graphs.addEdge(this.grafo, a.getT1(), a.getT2(), a.getPeso());
		}
	}
	
	public List<PuntiClassifica> calcolaClassifica(){
		Map<Team, Integer> classifica = new HashMap<>();
		List<PuntiClassifica> result = new LinkedList<>();
		
		for (Team t : this.grafo.vertexSet()) {
			classifica.put(t, 0);
		}
		
		
		for (DefaultWeightedEdge edge : this.grafo.edgeSet()) {
			int peso = (int)this.grafo.getEdgeWeight(edge);
			if (peso == 1) {
				classifica.put(this.grafo.getEdgeSource(edge), classifica.get(this.grafo.getEdgeSource(edge))+3);
			} else if (peso == -1) {
				classifica.put(this.grafo.getEdgeTarget(edge), classifica.get(this.grafo.getEdgeTarget(edge))+3);
			} else if (peso == 0) {
				classifica.put(this.grafo.getEdgeSource(edge), classifica.get(this.grafo.getEdgeSource(edge))+1);
				classifica.put(this.grafo.getEdgeTarget(edge), classifica.get(this.grafo.getEdgeTarget(edge))+1);
			}
		}
		
		for (Team t : classifica.keySet()) {
			result.add(new PuntiClassifica(t, classifica.get(t)));
		}
		
		Collections.sort(result);
		return result;
	}
	
	public List<Team> calcolaDomino(){
		this.best = new LinkedList<>();
		List<Team> parziale = new LinkedList<>();
		
		for (Team t : this.grafo.vertexSet()) {
			parziale.add(t);
			cerca(parziale);
			parziale.clear();
		}
		
		return this.best;
	}
	
	public void cerca(List<Team> parziale) {
		Team ultimo = parziale.get(parziale.size()-1);
		/*
		for (DefaultWeightedEdge edge : this.grafo.)
		
		
		
		for (Team adiacente : Graphs.successorListOf(this.grafo, ultimo)) {
			if (this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, adiacente)) == 1) {
				parziale.add(adiacente);
				cerca(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		*/	
		if (parziale.size() > best.size()) {
			best = new LinkedList<>(parziale);
			return;
		}
			
	}
	
	public List<Season> getAllSeasons(){
		return dao.listSeasons();
	}
	
	public List<Match> getAllMatchesWithSeason(Season season){
		return dao.listMatches(season, teamIdMap);
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
}
