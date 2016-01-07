package br.com.diabetesvirtual.rest;

import java.sql.Timestamp;
import java.util.Calendar;

import br.com.diabetesvirtual.model.Alimento;
import br.com.diabetesvirtual.model.Exercicios;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.model.Insulina;
import br.com.diabetesvirtual.model.Login;
import br.com.diabetesvirtual.model.Refeicao;
import br.com.docedesafio.model.Exercicio;
import br.com.docedesafio.model.Usuario;

/**
 * Classe responsável por converter objetos que vem do WEB via conectividade REST para os objetos Android.
 * Trata dos objetos das classes de tabelas principais (Alimento, Insulina, Glicemia...) 
 *
 */
public class ConvertObject {

	public static Login convertFromUsuario(Usuario usuario){
		Login log = new Login();
		log.setId(usuario.getId());
		log.setEmail(usuario.getEmail());
		log.setLogin(usuario.getLogin());
		log.setNivelAcesso(usuario.getNivelAcesso());
		log.setNome(usuario.getNome());
		log.setSenha(usuario.getSenha());
		return log;
	}
	
	public static Exercicios convertExercicios(Exercicio _ex){
		Exercicios ex = new Exercicios();
		ex.setId(_ex.getCodigo());
		ex.setTipo(_ex.getTipo()==null?"":_ex.getTipo());
		if(_ex.getData()!=null){
			ex.setData(getCalendarWithTimeInMillis(_ex.getData()));
		}
		ex.setDescricao(_ex.getDescricao()==null?"":_ex.getDescricao());
		ex.setDuracao(_ex.getDuracao());
		ex.setIntensidade(_ex.getIntensidade()==null?"":_ex.getIntensidade());
		ex.setModalidade(ex.getModalidade()==null?"":_ex.getModalidade());
		return ex;
	}
	
	
	public static Glicemia convertGlicemia(br.com.docedesafio.model.Glicemia _gl){
		Glicemia glicemia = new Glicemia();
		glicemia.setId(_gl.getCodigo());
		
		if(_gl.getData()!=null){
			glicemia.setData(getCalendarWithTimeInMillis(_gl.getData()));
		}
		
		glicemia.setMedida(_gl.getMedida());
		glicemia.setObs(_gl.getObservacao()==null?"":_gl.getObservacao());
		glicemia.setTipo(_gl.getTipo()==null?"":_gl.getTipo());
		
		return glicemia;
	}
	
	public static Insulina convertInsulina(br.com.docedesafio.model.Insulina _ins){
		Insulina insulina = new Insulina();
		
		insulina.setId(_ins.getCodigo());
		insulina.setObs(_ins.getObservacao()==null?"":_ins.getObservacao());
		insulina.setQtd(_ins.getQuantidade());
		insulina.setTipo(_ins.getTipo()==null?"":_ins.getTipo());
		if(_ins.getData()!=null){
			insulina.setData(getCalendarWithTimeInMillis(_ins.getData()));
		}
		
		return insulina;
	}
	
	
	public static Refeicao convertRefeicao(br.com.docedesafio.model.Refeicao _rf){
		Refeicao refeicao = new Refeicao();
		refeicao.setId(_rf.getCodigo());
		refeicao.setCarboidrato(_rf.getCarboidrato());
		refeicao.setObs(_rf.getObservacao()==null?"":_rf.getObservacao());
		refeicao.setPeso(_rf.getPeso());
		refeicao.setTipo(_rf.getTipo()==null?"":_rf.getTipo());
		
		if(_rf.getData()!=null){
			refeicao.setData(getCalendarWithTimeInMillis(_rf.getData()));
		}
		return refeicao;
	}
	
	public static Alimento convertAlimento(br.com.docedesafio.model.Alimento _al){
		Alimento alimento = new Alimento();
		alimento.setId_alimentos(_al.getCarboidratos());
		alimento.setCarboidrato(_al.getCarboidratos());
		alimento.setDescricao(_al.getNome()==null?"":_al.getNome());
		alimento.setFavorito(_al.getFavoritos());
		alimento.setMedida(_al.getMedida()==null?"":_al.getMedida());
		alimento.setPeso(_al.getPeso());
	
		return alimento;
	}
	
	private static Calendar getCalendarWithTimeInMillis(Timestamp timeStamp){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeStamp.getTime());
		return c;
	}
	
}
