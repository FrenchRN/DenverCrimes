package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}	
	}
	
	public List<String> getCategorie(){
		String sql = "SELECT DISTINCT(offense_category_id) from events ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(
							res.getString("offense_category_id"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}	
	}
	
	public List<Integer> getMesi(){
		String sql = "SELECT DISTINCT (MONTH(reported_date)) from events ORDER BY (MONTH(reported_date)) ASC ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
					list.add(
					res.getInt("(MONTH(reported_date))"));
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}	
	}
	
	public void getVertici(Map<String,String> idMap, String categoria, Integer mese) {
		String sql = "SELECT DISTINCT(offense_type_id) " +
				"FROM EVENTS " +
				"WHERE offense_category_id = ? AND MONTH(reported_date) = ?";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1,categoria);
			st.setInt(2,mese);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
					String of = res.getString("offense_type_id");
					if(!idMap.containsKey(of))
						idMap.put(of, of);
			}
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	public List<Arco> getArchi(Map<String,String> idMap,String categoria, int mese) {
		String sql = "SELECT e1.offense_type_id as of1, e2.offense_type_id as of2,COUNT(e1.neighborhood_id) AS TOT " +
				"FROM EVENTS AS e1, EVENTS AS e2 " + 
				"WHERE e1.offense_type_id != e2.offense_type_id and e1.neighborhood_id = e2.neighborhood_id AND e1.offense_category_id = e2.offense_category_id AND e1.offense_category_id = ? AND Month(e1.reported_date) = MONTH(e2.reported_date) AND MONTH(e1.reported_date) = ? "+
				"GROUP BY  e1.offense_type_id , e2.offense_type_id"; //QUANDO CE UN COUNT GROUP BY CI VUOLE SEMPRE!!!!!!!!!
		
		List<Arco> list;
		try {
			list = new ArrayList<>();
			
			Connection conn = DBConnect.getConnection() ;
			
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, mese);
						
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String of1 = idMap.get(res.getString("of1"));
				String of2 = idMap.get(res.getString("of2"));
				
				Arco arco = new Arco(of1,of2,res.getInt("TOT"));
				if(!list.contains(arco))
					list.add(arco);
			}
			
			
			conn.close();
			
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}
}

	

