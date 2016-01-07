package br.com.docedesafio.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.docedesafio.connection.StatementFactory;
import br.com.docedesafio.model.Perfil;
import br.com.docedesafio.model.Usuario;

public class LoginDAO extends BaseDAO<Usuario> {

	PerfilDAO perfilDAO = new PerfilDAO();
	
	public Usuario save(Usuario usuario){
		if(usuario.getId()==null){
			return insert(usuario);
		}else{
			update(usuario);
			return usuario;
		}
	}

	public Usuario insert(Usuario usuario) {
		String query = "INSERT INTO login(nome, email, senha, nivelacesso, login) VALUES (?, ?, ?, ?, ?);";
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setString(1, usuario.getNome());
			prepareStatement.setString(2, usuario.getEmail());
			prepareStatement.setString(3, usuario.getSenha());
			prepareStatement.setString(4, usuario.getNivelAcesso());
			prepareStatement.setString(5, usuario.getLogin());
			
			prepareStatement.executeUpdate();
			
			usuario = findUsuarioByLogin(usuario.getLogin());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return usuario;
	}
	
	public void update(Usuario usuario){
		String query = "UPDATE login SET id=?, nome=?, email=?, senha=?, nivelacesso=?, login=? WHERE id=?";
		PreparedStatement prepareStatement = StatementFactory.getPrepareStatement(query);
		try {
			prepareStatement.setInt(1, usuario.getId());
			prepareStatement.setString(2, usuario.getNome());
			prepareStatement.setString(3, usuario.getEmail());
			prepareStatement.setString(4, usuario.getSenha());
			prepareStatement.setString(5, usuario.getNivelAcesso());
			prepareStatement.setString(6, usuario.getLogin());
			prepareStatement.setInt(7, usuario.getId());
			
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Usuario findUsuarioByLogin(String login) {
		Usuario usuario =  selectedByQuery("select * from login where login = '" + login + "'");
		fillPerfil(usuario);
		return usuario;
	}
	
	public Usuario findUsuarioByEmail(String email) {
		Usuario usuario = selectedByQuery("select * from login where email = '" + email + "'");
		fillPerfil(usuario);
		return usuario;
	}
	
	public Usuario loginUsuario(String login, String senha) {
		Usuario usuario = selectedByQuery("select * from login where login = '" + login + "' and senha = '" + senha + "'");
		fillPerfil(usuario);
		return usuario;
	}

	public List<Usuario> listAll(Usuario userFiltro) {
		String query = "select * from login tbl where 1=1";
		if(userFiltro!=null){
			if(userFiltro.getNome()!=null && !userFiltro.getNome().isEmpty()){
				query +=" and upper(nome) like '%" + userFiltro.getNome().toUpperCase() + "%'";
			}
			if(userFiltro.getLogin()!=null && !userFiltro.getLogin().isEmpty()){
				query +=" and upper(login) like '" + userFiltro.getLogin().toUpperCase() + "'";
			}
			if(userFiltro.getEmail()!=null && !userFiltro.getEmail().isEmpty()){
				query +=" and upper(email) like '%" + userFiltro.getEmail().toUpperCase() + "%'";
			}
			if(userFiltro.getNivelAcesso()!=null && !userFiltro.getNivelAcesso().isEmpty()){
				query +=" and upper(nivelacesso) = '" + userFiltro.getNivelAcesso().toUpperCase() + "'";
			}
		}
		List<Usuario> listaUsuario =  selectedListByQuery(query);
		for(Usuario usr: listaUsuario){
			fillPerfil(usr);
		}
		
		return listaUsuario;
	}

	@Override
	protected Usuario factoryEntity(ResultSet rs) throws SQLException  {
		Usuario usuario = new Usuario();
		usuario.setId(rs.getInt("id"));
		usuario.setLogin(rs.getString("login"));
		usuario.setNome(rs.getString("nome"));
		usuario.setEmail(rs.getString("email"));
		usuario.setSenha(rs.getString("senha"));
		usuario.setNivelAcesso(rs.getString("nivelacesso"));
		return usuario;
	}
	
	public void fillPerfil(Usuario usuario) {
		if(usuario!=null){
			usuario.setPerfil(perfilDAO.findPerfilByIDLogin(usuario.getId()));
			if(usuario.getPerfil()==null){
				usuario.setPerfil(new Perfil());
			}
		}
	}

	@Override
	protected String getNameEntity() {
		return "login";
	}
}