package br.com.docedesafio.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.docedesafio.connection.StatementFactory;

public abstract class BaseDAO<T> {

	protected T selectedByQuery(String query){
		T entityReturn = null;
		try {
			Statement st = StatementFactory.getStatement();
			System.out.println("Executando-> " + query);
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				entityReturn = factoryEntity(rs);
				break;
			}
		} catch (SQLException sqlEx) {
			System.out.println("Erro de conexão com o BD, erro = " + sqlEx);
		}
		return entityReturn;
	}
	
	protected List<T> selectedListByQuery(String query){
		List<T> listResult = new ArrayList<T>();
		try {
			Statement st = StatementFactory.getStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				T entityReturn = factoryEntity(rs);
				listResult.add(entityReturn);
			}
		} catch (SQLException sqlEx) {
			System.out.println("Erro de conexão com o BD, erro = " + sqlEx);
		}
		return listResult;
	}
	
	
	public List<T> listByLogin(int idLogin) {
		return selectedListByQuery("select * from "+getNameEntity()+" al where id_login=" + idLogin);
	}
	
	public List<T> listAll() {
		return selectedListByQuery("select * from " +getNameEntity());
	}
	
	public T finById(int id) {
		return selectedByQuery("select * from "+getNameEntity()+" where id = " + id + "");
	}
	

	public void delete(int id){
		String query = "DELETE FROM "+ getNameEntity() + " WHERE id=?";
		System.out.println("Executando-> " + query + " --> ID " + id);
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setInt(1, id);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public void deleteByIdLoginCodigo(int idLogin, int codigo){
		String query = "DELETE FROM "+ getNameEntity() + " WHERE id_login=? and codigo=?";
		System.out.println("Executando-> " + query);
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setInt(1, idLogin);
			prepareStatement.setInt(2, codigo);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	protected boolean findByIdLoginCodigo(int idLogin, int codigo){
		String query = "SELECT * FROM "+getNameEntity()+" where id_login = "+idLogin+" and codigo = " + codigo;
		T al = selectedByQuery(query);
		return al != null;
	}
	
	protected int getValueNumber(Integer objNumber){
		if(objNumber==null){
			return 0;
		}else{
			return objNumber;
		}
	}
	
	protected abstract T factoryEntity(ResultSet rs) throws SQLException ;
	protected abstract String getNameEntity();
	public abstract T insert(T entity);
}
