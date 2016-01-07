package br.com.diabetesvirtual.bo;

import android.app.Activity;
import br.com.diabetesvirtual.dao.ExerciciosDao;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.dao.InsulinaDao;
import br.com.diabetesvirtual.dao.TabelaDao;
import br.com.diabetesvirtual.rest.ConvertObject;
import br.com.docedesafio.model.Exercicio;
import br.com.docedesafio.services.model.Authenticate;

/**
 * Classe responsável pela sincronização inicial ao realizar login.
 *
 */
public class SyncInit {
	
	public void synchronize(Authenticate auth, Activity ctx){
		if(auth!=null && auth.getUser()!=null){
			//******************* ALIMENTOS ********************
			if(auth.getUser().getListaAlimentos()!=null 
					&& auth.getUser().getListaAlimentos().size()>0){
				TabelaDao dao = new TabelaDao(ctx);
				for(br.com.docedesafio.model.Alimento obj : auth.getUser().getListaAlimentos()){
					try{
						dao.inserir(ConvertObject.convertAlimento(obj), true);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			//******************* EXERCICIOS ********************
			if(auth.getUser().getListaExercicios()!=null 
					&& auth.getUser().getListaExercicios().size()>0){
				ExerciciosDao exDao = new ExerciciosDao(ctx);
				for(Exercicio exRest : auth.getUser().getListaExercicios()){
					try {
						exDao.inserir(ConvertObject.convertExercicios(exRest), true);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}
			
			//******************* GLICEMIA ********************
			if(auth.getUser().getListaGlicemia()!=null 
					&& auth.getUser().getListaGlicemia().size()>0){
				GlicemiaDao glDao = new GlicemiaDao(ctx);
				for(br.com.docedesafio.model.Glicemia obj : auth.getUser().getListaGlicemia()){
					try{
						glDao.inserir(ConvertObject.convertGlicemia(obj), true);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			//******************* INSULINA ********************
			if(auth.getUser().getListaInsulina()!=null 
					&& auth.getUser().getListaInsulina().size()>0){
				InsulinaDao dao = new InsulinaDao(ctx);
				for(br.com.docedesafio.model.Insulina obj : auth.getUser().getListaInsulina()){
					try{
						dao.inserir(ConvertObject.convertInsulina(obj), true);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			//******************* REFEICOES ********************
			if(auth.getUser().getListaRefeicoes()!=null 
					&& auth.getUser().getListaRefeicoes().size()>0){
//				RefeicaoDao glDao = new RefeicaoDao(ctx);
//				for(br.com.docedesafio.model.Refeicao glRest : auth.getUser().getListaRefeicoes()){
//					try{
//						glDao.in(ConvertObject.convertRefeicao(glRest));
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//				}
			}
		}
	}
}