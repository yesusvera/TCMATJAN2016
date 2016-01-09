package br.com.docedesafio.connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.docedesafio.relatorios.ConnectionFactory;

public class StatementFactory {

	public static Statement getStatement(){
		//Carregar o driver
		try {
			Statement st = ConnectionFactory
								.getConnection()
								.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			return st;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static PreparedStatement getPrepareStatement(String sql){
		//Carregar o driver
		try {
			PreparedStatement prepareStatement = ConnectionFactory
								.getConnection()
								.prepareStatement(sql);
			return prepareStatement;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
