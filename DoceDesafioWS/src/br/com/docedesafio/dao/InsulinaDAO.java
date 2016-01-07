package br.com.docedesafio.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.docedesafio.connection.StatementFactory;
import br.com.docedesafio.model.Insulina;

public class InsulinaDAO extends BaseDAO<Insulina> {
	
	public Insulina save(Insulina insulina){
		if(findByIdLoginCodigo(insulina.getIdLogin(), insulina.getCodigo())){
			return update(insulina);
		}else{
			return insert(insulina);
		}
	}
	
	public Insulina update(Insulina insulina){
		String query = "UPDATE insulina SET tipo=?, "
				+ "obs=?, quantidade=?, data=?"
				+ " WHERE id_login=? and codigo=?";
		
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setString(1, insulina.getTipo());
			prepareStatement.setString(2, insulina.getObservacao());
			prepareStatement.setInt(3, insulina.getQuantidade());
			prepareStatement.setTimestamp(4, insulina.getData());
			
			prepareStatement.setInt(5, insulina.getIdLogin());
			prepareStatement.setInt(6, insulina.getCodigo());
			
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return insulina;
	}
	
	public List<Insulina> listByFilters(String nomeUsuarioFiltro, String tipoInsulinaFiltro, Integer idLogin) {
		String query = "select tbl.*, log.nome AS nomeUsuario from insulina tbl inner join login log on tbl.id_login = log.id where 1 = 1 ";

		if (nomeUsuarioFiltro != null && nomeUsuarioFiltro.length() > 0) {
			query += " and upper(log.nome) like '%" + nomeUsuarioFiltro.toUpperCase() + "%'";
		}
		if (tipoInsulinaFiltro != null && tipoInsulinaFiltro.length() > 0) {
			query += " and upper(tbl.tipo) like '%" + tipoInsulinaFiltro.toUpperCase() + "%'";
		} 
		if (idLogin != null) {
			query += " and id_login=" + idLogin;
		}
		return selectedListByQuery(query);
	}
	
	@Override
	protected Insulina factoryEntity(ResultSet rs) throws SQLException {
		Insulina insulina = new Insulina();
		
		insulina.setId(rs.getInt("id"));
		insulina.setIdLogin(rs.getInt("id_login"));
		insulina.setTipo(rs.getString("tipo"));
		insulina.setData(rs.getTimestamp("data"));
		insulina.setQuantidade(rs.getInt("quantidade"));
		insulina.setObservacao(rs.getString("obs"));
		insulina.setCodigo(rs.getInt("codigo"));
		
		try{
			insulina.setNomeUsuario(rs.getString("nomeUsuario"));
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return insulina;
	}

	@Override
	protected String getNameEntity() {
		return "insulina";
	}

	@Override
	public Insulina insert(Insulina entity) {
		String query = "INSERT INTO insulina(id_login, tipo, obs, "
				+ "quantidade, data, codigo)  VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setInt(1, entity.getIdLogin());
			prepareStatement.setString(2, entity.getTipo());
			prepareStatement.setString(3, entity.getObservacao());
			prepareStatement.setInt(4, entity.getQuantidade());
			prepareStatement.setTimestamp(5, entity.getData());
			prepareStatement.setInt(6, entity.getCodigo());
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entity;
	}

}
