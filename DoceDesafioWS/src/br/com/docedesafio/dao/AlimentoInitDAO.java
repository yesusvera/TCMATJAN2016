package br.com.docedesafio.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.docedesafio.model.Alimento;

public class AlimentoInitDAO extends BaseDAO<Alimento> {
	
	public void saveAlimentoFromInit(int idLogin){
		List<Alimento> listAl = listAll();
		
		AlimentoDAO alimentoDAO = new AlimentoDAO();
		
		for(Alimento al: listAl){
			al.setIdLogin(idLogin);
			alimentoDAO.insert(al);
		}
	}
	
	@Override
	protected Alimento factoryEntity(ResultSet rs) throws SQLException {
		Alimento alimento = new Alimento();
		
		alimento.setId(rs.getInt("id"));
		alimento.setNome(rs.getString("nome"));
		alimento.setMedida(rs.getString("medida"));
		alimento.setPeso(rs.getInt("peso"));
		alimento.setCarboidratos(rs.getInt("carboidratos"));
		alimento.setFavoritos(rs.getInt("favorito"));
		alimento.setCodigo(rs.getInt("id"));
		
		return alimento;
	}

	@Override
	protected String getNameEntity() {
		return "alimento_init";
	}

	@Override
	public Alimento insert(Alimento entity) {
		return null;
	}

}
