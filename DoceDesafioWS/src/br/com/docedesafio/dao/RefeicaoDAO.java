package br.com.docedesafio.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.docedesafio.connection.StatementFactory;
import br.com.docedesafio.model.Refeicao;

public class RefeicaoDAO extends BaseDAO<Refeicao> {
	
	public Refeicao save(Refeicao refeicao){
		if(findByIdLoginCodigo(refeicao.getIdLogin(), refeicao.getCodigo())){
			return update(refeicao);
		}else{
			return insert(refeicao);
		}
	}
	

	public Refeicao update(Refeicao refeicao){
		String query = "UPDATE refeicao SET "
				+ " carboidrato=?, peso=?, obs=?, tipo=?, data=?"
				+ " WHERE id_login=? and codigo=?";
		
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setInt(1, refeicao.getCarboidrato());
			prepareStatement.setInt(2, refeicao.getPeso());
			
			prepareStatement.setString(3, refeicao.getObservacao());
			prepareStatement.setString(4, refeicao.getTipo());
			
			prepareStatement.setTimestamp(5, refeicao.getData());
			
			prepareStatement.setInt(6, refeicao.getIdLogin());
			prepareStatement.setInt(7, refeicao.getCodigo());
			
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return refeicao;
	}
	
	public List<Refeicao> listByFilters(String nomeUsuarioFiltro, String tipoRefeicaoFiltro, Integer idLogin) {
		String query = "select tbl.*, log.nome AS nomeUsuario from refeicao tbl inner join login log on tbl.id_login = log.id where 1 = 1 ";

		if (nomeUsuarioFiltro != null && nomeUsuarioFiltro.length() > 0) {
			query += " and upper(log.nome) like '%" + nomeUsuarioFiltro.toUpperCase() + "%'";
		}
		if (tipoRefeicaoFiltro != null && tipoRefeicaoFiltro.length() > 0) {
			query += " and upper(tbl.tipo) like '%" + tipoRefeicaoFiltro.toUpperCase() + "%'";
		}
		if (idLogin != null) {
			query += " and id_login=" + idLogin;
		}
		return selectedListByQuery(query);
	}
	
	@Override
	protected Refeicao factoryEntity(ResultSet rs) throws SQLException {
		Refeicao refeicao = new Refeicao();
		
		refeicao.setId(rs.getInt("id"));
		refeicao.setIdLogin(rs.getInt("id_login"));
		refeicao.setTipo(rs.getString("tipo"));
		refeicao.setData(rs.getTimestamp("data"));
		refeicao.setCarboidrato(rs.getInt("carboidrato"));
		refeicao.setPeso(rs.getInt("peso"));
		refeicao.setObservacao(rs.getString("obs"));
		refeicao.setCodigo(rs.getInt("codigo"));
		
		try{
			refeicao.setNomeUsuario(rs.getString("nomeUsuario"));
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return refeicao;
	}

	@Override
	protected String getNameEntity() {
		return "refeicao";
	}

	@Override
	public Refeicao insert(Refeicao entity) {
		String query = "INSERT INTO refeicao(id_login, carboidrato, peso, obs, "
				+ "tipo, data, codigo) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setInt(1, entity.getIdLogin());
			prepareStatement.setInt(2, entity.getCarboidrato());
			prepareStatement.setInt(3, entity.getPeso());
			prepareStatement.setString(4, entity.getObservacao());
			prepareStatement.setString(5, entity.getTipo());
			
			prepareStatement.setTimestamp(6, entity.getData());
			prepareStatement.setInt(7, entity.getCodigo());
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entity;
	}

}
