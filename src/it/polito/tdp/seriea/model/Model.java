package it.polito.tdp.seriea.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private Set<DefaultWeightedEdge> usedEdges;
	
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
		this.usedEdges = new HashSet<>();
		
		List<Team> parziale = new LinkedList<>();

		/***ATTENZIONE***/
		/**
		 * Elimina dei vertici dal grafo per renderlo
		 * gestibile dalla ricorsione.
		 * Nella soluzione "vera" questa istruzione va rimossa
		 * (però l'algoritmo non termina in tempi umani).
		 */
		this.riduciGrafo(8);
		
		for (Team t : this.grafo.vertexSet()) {
			parziale.add(t);
			cerca(1, t, parziale);
			parziale.remove(t);
		}
		
		return this.best;
	}
	
	public void cerca(int step, Team t1, List<Team> parziale) {
		// controllo se ho migliorato il cammino "best"
		if (parziale.size() > best.size()) {
			// aggiorno la soluzione migliore
			best = new LinkedList<>(parziale);
		}
		
		// cerchiamo di aggiungere un nuovo vertice
		for (DefaultWeightedEdge edge : this.grafo.outgoingEdgesOf(t1)) {
			Team t2 = this.grafo.getEdgeTarget(edge);
			
			// dobbiamo verificare che l'arco sia relativo ad una partita vinta 
			// e che non sia ancora stato utilizzato
			if (this.grafo.getEdgeWeight(edge) == 1 && !this.usedEdges.contains(edge)) {
				// provo ad attraversare l'arco
				parziale.add(t2);
				this.usedEdges.add(edge);
				cerca(step+1, t2, parziale);
				usedEdges.remove(edge);
				parziale.remove(parziale.size()-1); // parziale.remove(t2) NON va bene perchè 
													// t2 può comparire più di una volta 
			}
		}		
	}
	
	/**
	 * cancella dei vertici dal grafo in modo che la sua dimensione
	 * sia solamente pari a {@code dim} vertici
	 * @param dim
	 */
	private void riduciGrafo(int dim) {
		Set<Team> togliere = new HashSet<>() ;
		
		Iterator<Team> iter = this.grafo.vertexSet().iterator() ;
		for(int i=0; i<this.grafo.vertexSet().size()-dim; i++) {
			togliere.add(iter.next()) ;
		}
		this.grafo.removeAllVertices(togliere) ;
		System.err.println("Attenzione: cancello dei vertici dal grafo");
		System.err.println("Vertici rimasti: "+this.grafo.vertexSet().size()+"\n");
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
