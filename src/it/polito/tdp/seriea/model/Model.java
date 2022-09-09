package it.polito.tdp.seriea.model;

import java.util.List;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	
	public Model() {
		dao = new SerieADAO();
	}
	
	public void creaGrafo() {
		
	}
	
	public List<Season> getAllSeasons(){
		return dao.listSeasons();
	}
	
}
