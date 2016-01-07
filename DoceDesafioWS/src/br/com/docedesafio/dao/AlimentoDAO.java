package br.com.docedesafio.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.docedesafio.connection.StatementFactory;
import br.com.docedesafio.model.Alimento;

public class AlimentoDAO extends BaseDAO<Alimento> {
	
	public List<Alimento> listByFilters(String nomeUsuarioFiltro, String nomeAlimentoFiltro, Integer idLogin) {
		String query = "select al.*, log.nome AS nomeUsuario from alimento al inner join login log on al.id_login = log.id where 1 = 1 ";

		if (nomeUsuarioFiltro != null && nomeUsuarioFiltro.length() > 0) {
			query += " and upper(log.nome) like '%" + nomeUsuarioFiltro.toUpperCase() + "%'";
		}
		if (nomeAlimentoFiltro != null && nomeAlimentoFiltro.length() > 0) {
			query += " and upper(al.nome) like '%" + nomeAlimentoFiltro.toUpperCase() + "%'";
		}
		
		if (idLogin != null) {
			query += " and id_login=" + idLogin;
		}
		
		return selectedListByQuery(query);
	}
	
	
	@Override
	protected Alimento factoryEntity(ResultSet rs) throws SQLException {
		Alimento alimento = new Alimento();
		
		alimento.setId(rs.getInt("id"));
		alimento.setIdLogin(rs.getInt("id_login"));
		alimento.setNome(rs.getString("nome"));
		alimento.setMedida(rs.getString("medida"));
		alimento.setPeso(rs.getInt("peso"));
		alimento.setCarboidratos(rs.getInt("carboidratos"));
		alimento.setFavoritos(rs.getInt("favorito"));
		alimento.setCodigo(rs.getInt("codigo"));
		
		try{
			alimento.setNomeUsuario(rs.getString("nomeUsuario"));
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		
		return alimento;
	}

	
	@Override
	protected String getNameEntity() {
		return "alimento";
	}

	public Alimento save(Alimento alimento){
		if(findByIdLoginCodigo(alimento.getIdLogin(), alimento.getCodigo())){
			return update(alimento);
		}else{
			return insert(alimento);
		}
	}

	@Override
	public Alimento insert(Alimento alimento) {
		String query = "INSERT INTO alimento(id_login, nome, medida, peso, carboidratos, favorito, codigo)  VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			
			prepareStatement.setInt(1, alimento.getIdLogin());
			prepareStatement.setString(2, alimento.getNome());
			prepareStatement.setString(3, alimento.getMedida());
			prepareStatement.setInt(4, alimento.getPeso());
			prepareStatement.setInt(5, alimento.getCarboidratos());
			prepareStatement.setInt(6, alimento.getFavoritos());
			prepareStatement.setInt(7, alimento.getCodigo());
			
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return alimento;
	}
	
	public Alimento update(Alimento alimento){
		String query = "UPDATE alimento"
				+ " SET nome=?, medida=?, peso=?, carboidratos=?, favorito=? "
				+ " WHERE id_login=? and codigo=?";
		
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			
			prepareStatement.setString(1, alimento.getNome());
			prepareStatement.setString(2, alimento.getMedida());
			prepareStatement.setInt(3, alimento.getPeso());
			prepareStatement.setInt(4, alimento.getCarboidratos());
			prepareStatement.setInt(5, alimento.getFavoritos());
			
			prepareStatement.setInt(6, alimento.getIdLogin());
			prepareStatement.setInt(7, alimento.getCodigo());
			
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return alimento;
	}

}
