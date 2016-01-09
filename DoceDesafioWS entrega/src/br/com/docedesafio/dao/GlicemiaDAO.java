package br.com.docedesafio.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.docedesafio.connection.StatementFactory;
import br.com.docedesafio.model.Glicemia;

public class GlicemiaDAO extends BaseDAO<Glicemia> {
	
	
	public Glicemia save(Glicemia glicemia){
		if(findByIdLoginCodigo(glicemia.getIdLogin(), glicemia.getCodigo())){
			return update(glicemia);
		}else{
			return insert(glicemia);
		}
	}
	
	public Glicemia update(Glicemia glicemia){
		String query = "UPDATE glicemia SET tipo=?, "
				+ "obs=?, medida=?, data=?"
				+ " WHERE id_login=? and codigo=?";
		
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setString(1, glicemia.getTipo());
			prepareStatement.setString(2, glicemia.getObservacao());
			prepareStatement.setInt(3, glicemia.getMedida());
			prepareStatement.setTimestamp(4, glicemia.getData());
			
			prepareStatement.setInt(5, glicemia.getIdLogin());
			prepareStatement.setInt(6, glicemia.getCodigo());
			
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return glicemia;
	}
	
	public List<Glicemia> listByFilters(String nomeUsuarioFiltro, String tipoGlicemiaFiltro, Integer idLogin) {
		String query = "select tbl.*, log.nome AS nomeUsuario from glicemia tbl inner join login log on tbl.id_login = log.id where 1 = 1 ";

		if (nomeUsuarioFiltro != null && nomeUsuarioFiltro.length() > 0) {
			query += " and upper(log.nome) like '%" + nomeUsuarioFiltro.toUpperCase() + "%'";
		}
		if (tipoGlicemiaFiltro != null && tipoGlicemiaFiltro.length() > 0) {
			query += " and upper(tbl.tipo) like '%" + tipoGlicemiaFiltro.toUpperCase() + "%'";
		}
		if (idLogin != null) {
			query += " and id_login=" + idLogin;
		}
		
		return selectedListByQuery(query);
	}
	
	@Override
	protected Glicemia factoryEntity(ResultSet rs) throws SQLException {
		Glicemia glicemia = new Glicemia();
		
		glicemia.setId(rs.getInt("id"));
		glicemia.setIdLogin(rs.getInt("id_login"));
		glicemia.setTipo(rs.getString("tipo"));
		glicemia.setData(rs.getTimestamp("data"));
		glicemia.setMedida(rs.getInt("medida"));
		glicemia.setObservacao(rs.getString("obs"));
		glicemia.setCodigo(rs.getInt("codigo"));
		
		try{
			glicemia.setNomeUsuario(rs.getString("nomeUsuario"));
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return glicemia;
	}

	@Override
	protected String getNameEntity() {
		return "glicemia";
	}

	@Override
	public Glicemia insert(Glicemia entity) {
		String query = "INSERT INTO glicemia(id_login, tipo, obs, "
				+ "medida, data, codigo)  VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setInt(1, entity.getIdLogin());
			prepareStatement.setString(2, entity.getTipo());
			prepareStatement.setString(3, entity.getObservacao());
			prepareStatement.setInt(4, entity.getMedida());
			prepareStatement.setTimestamp(5, entity.getData());
			prepareStatement.setInt(6, entity.getCodigo());
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entity;
	}

}
