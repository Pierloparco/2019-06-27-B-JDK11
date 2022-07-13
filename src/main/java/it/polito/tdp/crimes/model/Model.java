package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private Graph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, Integer mese) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.tipiReatoVertici(categoria, mese));
		
		for(Adiacenza a : dao.getArchi(categoria, mese)) {
			if(a.getPeso() >= 0) {
				if(grafo.containsVertex(a.getTipo1()) && grafo.containsVertex(a.getTipo2())) {
					Graphs.addEdge(this.grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
				}
			}
		}
	}
	
	public List<Adiacenza> getArchiMaxPesoMedio() {
		List<Adiacenza> archi = new ArrayList<>();
		double pesoTot = 0.0;
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesoTot += this.grafo.getEdgeWeight(e);
		}
		
		double avg = pesoTot/this.grafo.edgeSet().size();
		
		for(DefaultWeightedEdge ee : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(ee)>avg) {
				archi.add(new Adiacenza(this.grafo.getEdgeSource(ee), this.grafo.getEdgeTarget(ee), this.grafo.getEdgeWeight(ee)));
			}
		}
		return archi;
	}

	public List<String> getCategorie() {
		return dao.listOfCategorie();
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
}
