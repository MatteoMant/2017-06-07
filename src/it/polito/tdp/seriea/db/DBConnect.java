package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class DBConnect {
	
	private static String jdbcURL = "jdbc:mysql://localhost/serie_a?serverTimezone=Europe/Rome&useSSL=false" ;
	
	private static DataSource ds ;
	
	public static Connection getConnection() {
		
		if(ds==null) {
			// initialize DataSource
			try {
				ds = DataSources.pooledDataSource(DataSources.unpooledDataSource(jdbcURL)) ;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		try {
			Connection c = DriverManager.getConnection(jdbcURL, "root", "simmy2003!");
			return c ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

}
