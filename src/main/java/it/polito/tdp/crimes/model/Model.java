package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private EventsDao dao;
	private Graph<String,DefaultWeightedEdge> grafo;
	private Map<String,String> idMap;
	private ArrayList<String> best;

	public Model() {
		this.dao = new EventsDao();
		idMap = new HashMap<>();
	}
	
	public List<String> getCategorie(){
		return dao.getCategorie();
	}
	
	public List<Integer> getMesi(){
		return dao.getMesi();
	}
	
	public void creaGrafo(String categoria, Integer mese) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao.getVertici(idMap, categoria, mese);
		Graphs.addAllVertices(this.grafo, idMap.keySet());
		for(Arco ar : dao.getArchi(idMap,categoria,mese)) {
			DefaultWeightedEdge edge = grafo.getEdge(ar.getOf1(), ar.getOf2());
			if(grafo.containsVertex(ar.getOf1()) && grafo.containsVertex(ar.getOf2()) && edge == null && ar.getPeso() > 0)
				Graphs.addEdge(this.grafo, ar.getOf1(), ar.getOf2(), ar.getPeso());
		}
	}
	
	public String stampaArchiMaggiori() {
		double media = this.calcolaPesoMedio();
		String stampa = "";
		for(DefaultWeightedEdge edge : grafo.edgeSet())
			if(this.grafo.getEdgeWeight(edge) >= media)
				stampa = stampa + "Source " + this.grafo.getEdgeSource(edge) + " Target: " + this.grafo.getEdgeTarget(edge) + " Peso: " + this.grafo.getEdgeWeight(edge) + "\n";
		return stampa;
	}
	
	public List<String> trovaPercorso(String sorgente, String destinazione) {
		List<String> parziale = new ArrayList<>();
		this.best = new ArrayList<>();
		parziale.add(sorgente);
		trovaRiscorsivo(destinazione,parziale, 0);
		return this.best;
	}

	private void trovaRiscorsivo(String destinazione, List<String> parziale, int L) {

		//CASO TERMINALE? -> quando l'ultimo vertice inserito in parziale è uguale alla destinazione
		if(parziale.get(parziale.size() - 1).equals(destinazione)) {
			if(parziale.size() > this.best.size()) {
				this.best = new ArrayList<>(parziale);
			}
			return;
		}
		
		//scorro i vicini dell'ultimo vertice inserito in parziale
		for(String vicino : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size() -1 ))) {
			//cammino aciclico -> controllo che il vertice non sia già in parziale
			if(!parziale.contains(vicino)) {
				//provo ad aggiungere
				parziale.add(vicino);
				//continuo la ricorsione
				this.trovaRiscorsivo(destinazione, parziale, L+1);
				//faccio backtracking
				parziale.remove(parziale.size() -1);
			}
		}
	}
	
	private double calcolaPesoMedio() {
		double tot = 0;
		for(DefaultWeightedEdge edge : grafo.edgeSet())
			tot = tot + this.grafo.getEdgeWeight(edge);
		return tot/(this.grafo.edgeSet().size());
	}

	public int nVertex() {
		// TODO Auto-generated method stub
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		// TODO Auto-generated method stub
		return grafo.edgeSet().size();
	}
}
