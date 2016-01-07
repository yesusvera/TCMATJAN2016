package br.com.docedesafio.relatorios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private static Connection conn;
	
	public static Connection getConnection() {
		if(conn!=null){
			return conn;
		}
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost/doce_desafio", "postgres", "admin");
			
			return conn;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
