package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Adiacenza;
import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public void listTeams(Map<String, Team> teamIdMap) {
		String sql = "SELECT team FROM teams" ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Team t = new Team(res.getString("team"));
				if (!teamIdMap.containsKey(res.getString("team"))) {
					teamIdMap.put(t.getTeam(), t);
				}
			}
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Team> listTeamsWithSeason(Season season) {
		String sql = "select distinct HomeTeam "
				+ "from matches "
				+ "where season=? "
				+ "order by HomeTeam" ;
		
		List<Team> result = new LinkedList<>();
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, season.getSeason());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Team t = new Team(res.getString("HomeTeam"));
				result.add(t);
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listMatches(Season season, Map<String, Team> teamIdMap) {
		String sql = "select match_id, season, `Div`, Date, HomeTeam, AwayTeam, FTHG, FTAG, FTR "
				+ "from matches where season=?";
		
		Connection conn = DBConnect.getConnection() ;
		
		List<Match> result = new ArrayList<>() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, season.getSeason());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Match(
						res.getInt("match_id"),
						season,
						res.getString("Div"),
						res.getDate("Date").toLocalDate(),
						teamIdMap.get(res.getString("HomeTeam")),
						teamIdMap.get(res.getString("AwayTeam")),
						res.getInt("FTHG"),
						res.getInt("FTAG"),
						res.getString("FTR")
						)) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> getAllAdiacenze(Season season, Map<String, Team> teamIdMap) {
		String sql = "select HomeTeam as t1, AwayTeam as t2, FTR as risultato "
				+ "from matches "
				+ "where season=?" ;
		
		List<Adiacenza> result = new LinkedList<>();
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, season.getSeason());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				Team t1 = teamIdMap.get(res.getString("t1"));
				Team t2 = teamIdMap.get(res.getString("t2"));
				String risultato = res.getString("risultato");
				int peso = 0;
				if (risultato.equals("H")) {
					peso = 1;
				} else if (risultato.equals("A")) {
					peso = -1;
				} else if (risultato.equals("D")) {
					peso = 0;
				}
				Adiacenza a = new Adiacenza(t1, t2, peso);
				result.add(a);
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	

}
