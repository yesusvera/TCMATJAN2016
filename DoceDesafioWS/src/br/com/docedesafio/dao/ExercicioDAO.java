package br.com.docedesafio.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.docedesafio.connection.StatementFactory;
import br.com.docedesafio.model.Exercicio;

public class ExercicioDAO extends BaseDAO<Exercicio> {
	
	
	public List<Exercicio> listByFilters(String nomeUsuarioFiltro, String tipoExercicioFiltro, String modalidadeExercicioFiltro, Integer idLogin) {
		String query = "select tbl.*, log.nome AS nomeUsuario from exercicio tbl inner join login log on tbl.id_login = log.id where 1 = 1 ";

		if (nomeUsuarioFiltro != null && nomeUsuarioFiltro.length() > 0) {
			query += " and upper(log.nome) like '%" + nomeUsuarioFiltro.toUpperCase() + "%'";
		}
		if (tipoExercicioFiltro != null && tipoExercicioFiltro.length() > 0) {
			query += " and upper(tbl.tipo) like '%" + tipoExercicioFiltro.toUpperCase() + "%'";
		}
		if (modalidadeExercicioFiltro != null && modalidadeExercicioFiltro.length() > 0) {
			query += " and upper(tbl.modalidade) like '%" + modalidadeExercicioFiltro.toUpperCase() + "%'";
		}
		if (idLogin != null) {
			query += " and id_login=" + idLogin;
		}
		
		return selectedListByQuery(query);
	}
	
	@Override
	protected Exercicio factoryEntity(ResultSet rs) throws SQLException {
		Exercicio exercicio = new Exercicio();
		
		exercicio.setId(rs.getInt("id"));
		exercicio.setIdLogin(rs.getInt("id_login"));
		exercicio.setTipo(rs.getString("tipo"));
		exercicio.setDescricao(rs.getString("descricao"));
		exercicio.setModalidade(rs.getString("modalidade"));
		exercicio.setIntensidade(rs.getString("intensidade"));
		exercicio.setDuracao(rs.getInt("duracao"));
		exercicio.setData(rs.getTimestamp("data"));
		exercicio.setCodigo(rs.getInt("codigo"));
		
		try{
			exercicio.setNomeUsuario(rs.getString("nomeUsuario"));
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		
		return exercicio;
	}

	@Override
	protected String getNameEntity() {
		return "exercicio";
	}

	public Exercicio save(Exercicio ex) {
		if(findByIdLoginCodigo(ex.getIdLogin(), ex.getCodigo())){
			return update(ex);
		}else{
			return insert(ex);
		}
	}
	
	@Override
	public Exercicio insert(Exercicio ex) {
		String query = "INSERT INTO exercicio("
				+ "id_login, descricao, tipo, "
				+ "modalidade, intensidade, duracao,"
				+ "data, codigo) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setInt(1, ex.getIdLogin());
			prepareStatement.setString(2, ex.getDescricao());
			prepareStatement.setString(3, ex.getTipo());
			prepareStatement.setString(4, ex.getModalidade());
			prepareStatement.setString(5, ex.getIntensidade());
			prepareStatement.setInt(6, ex.getDuracao());
			prepareStatement.setTimestamp(7, ex.getData());
			prepareStatement.setInt(8, ex.getCodigo());
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ex;
	}
	
	public Exercicio update(Exercicio ex){
		String query = "UPDATE exercicio SET descricao=?, tipo=?, "
				+ "modalidade=?, intensidade=?, duracao=?, data=? "
				+ "WHERE id_login=? and codigo=?";
		
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setString(1, ex.getDescricao());
			prepareStatement.setString(2, ex.getTipo());
			prepareStatement.setString(3, ex.getModalidade());
			prepareStatement.setString(4, ex.getIntensidade());
			prepareStatement.setInt(5, ex.getDuracao());
			prepareStatement.setTimestamp(6, ex.getData());
			
			prepareStatement.setInt(7, ex.getIdLogin());
			prepareStatement.setInt(8, ex.getCodigo());
			
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ex;
	}

}
