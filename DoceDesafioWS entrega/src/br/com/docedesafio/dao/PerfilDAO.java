package br.com.docedesafio.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.docedesafio.connection.StatementFactory;
import br.com.docedesafio.model.Perfil;

public class PerfilDAO extends BaseDAO<Perfil> {

	public Perfil save(Perfil perfil){
		Perfil perfilTmp = findPerfilByIDLogin(perfil.getIdLogin());
		
		if(perfilTmp==null){
			return insert(perfil);
		}else{
			perfil.setIdPerfil(perfilTmp.getIdPerfil());
			return update(perfil);
		}
	}
	
	public Perfil insert(Perfil perfil){
		String query = "INSERT INTO perfil(id_login, peso, data_nasc, obs, altura, sexo, fator_glicemia,"
				+ "fator_carboidrato) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			int i= 1;
			prepareStatement.setInt(i++, perfil.getIdLogin());
			prepareStatement.setInt(i++, getValueNumber(perfil.getPeso()));
			prepareStatement.setDate(i++, perfil.getDataNascimento());
			prepareStatement.setString(i++, perfil.getObservacao());
			prepareStatement.setInt(i++,  getValueNumber(perfil.getAltura()));
			prepareStatement.setByte(i++,  perfil.getSexo());
			prepareStatement.setInt(i++,  getValueNumber(perfil.getFatorGlicemia()));
			prepareStatement.setInt(i++,  getValueNumber(perfil.getFatorCarboidrato()));
//			prepareStatement.setByte(i++,  getValueNumber(perfil.getCategoria()));
			
			prepareStatement.executeUpdate();
			
			perfil = findPerfilByIDLogin(perfil.getIdLogin());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return perfil;
	}
	
	public Perfil update(Perfil perfil){
		String query = "UPDATE perfil SET id_perfil=?, id_login=?, peso=?, data_nasc=?, obs=?, altura=?,"
				+ "sexo=?, fator_glicemia=?, fator_carboidrato=?"
				+ " WHERE id_perfil=?;";
		
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			int i= 1;
			prepareStatement.setInt(i++, perfil.getIdPerfil());
			prepareStatement.setInt(i++, perfil.getIdLogin());
			prepareStatement.setInt(i++, getValueNumber(perfil.getPeso()));
			prepareStatement.setDate(i++, perfil.getDataNascimento());
			prepareStatement.setString(i++, perfil.getObservacao());
			prepareStatement.setInt(i++, getValueNumber(perfil.getAltura()));
			prepareStatement.setInt(i++, perfil.getSexo());
			prepareStatement.setInt(i++, getValueNumber(perfil.getFatorGlicemia()));
			prepareStatement.setInt(i++, getValueNumber(perfil.getFatorCarboidrato()));
//			prepareStatement.setInt(i++, getValueNumber(perfil.getCategoria()));
			prepareStatement.setInt(i++, perfil.getIdPerfil());
			
			prepareStatement.executeUpdate();
			
			return perfil;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return perfil;
	}
	
	public void delete(int id){
		String query = "DELETE FROM perfil WHERE id_perfil=?";
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setInt(1, id);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public Perfil findPerfilByI(int idPerfil) {
		return selectedByQuery("select * from perfil where id_perfil = " + idPerfil);
	}
	
	public Perfil findPerfilByIDLogin(int idLogin) {
		return selectedByQuery("select * from perfil where id_login = " + idLogin);
	}


	@Override
	protected Perfil factoryEntity(ResultSet rs) throws SQLException  {
		Perfil perfil = new Perfil();
		perfil.setIdPerfil(rs.getInt("id_perfil"));
		perfil.setIdLogin(rs.getInt("id_login"));
		perfil.setPeso(rs.getInt("peso"));
		perfil.setDataNascimento(rs.getDate("data_nasc"));
		perfil.setObservacao(rs.getString("obs"));
		perfil.setAltura(rs.getInt("altura"));
		perfil.setSexo(rs.getByte("sexo"));
		perfil.setFatorGlicemia(rs.getInt("fator_glicemia"));
		perfil.setFatorCarboidrato(rs.getInt("fator_carboidrato"));
		perfil.setCategoria(rs.getByte("categoria"));
		return perfil;
	}

	@Override
	protected String getNameEntity() {
		return "perfil";
	}
}