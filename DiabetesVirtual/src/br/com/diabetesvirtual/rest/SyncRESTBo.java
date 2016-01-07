package br.com.diabetesvirtual.rest;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import br.com.diabetesvirtual.activity.LoginActivity;
import br.com.diabetesvirtual.dao.ExerciciosDao;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.dao.InsulinaDao;
import br.com.diabetesvirtual.dao.RefeicaoDao;
import br.com.diabetesvirtual.dao.SyncRESTDao;
import br.com.diabetesvirtual.dao.TabelaDao;
import br.com.diabetesvirtual.model.Alimento;
import br.com.diabetesvirtual.model.Exercicios;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.model.Insulina;
import br.com.diabetesvirtual.model.Refeicao;
import br.com.diabetesvirtual.model.SyncREST;
import br.com.diabetesvirtual.prop.ConstantesREST;

public class SyncRESTBo {

	// *********************** ALIMENTO
	public void insertAlimentoREST(Alimento alimento, Activity context, Integer idSyncREST) {
		ConnectREST connRESTInsert = new ConnectREST(ConstantesREST.INSERT_ALIMENTO_SERVICE, context);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idLogin", LoginActivity.usuarioLogado.getUser().getId().toString());
		params.put("nome", alimento.getDescricao());
		params.put("medida", alimento.getMedida());
		params.put("peso", alimento.getPeso() + "");
		params.put("carboidratos", alimento.getCarboidrato() + "");
		params.put("codigo", alimento.getId_alimentos() + "");

		if (idSyncREST != null) {
			connRESTInsert.sincronizacaoDeOfflinePorID(idSyncREST);
		}

		connRESTInsert.asyncExecute(params);
	}

	public void deleteAlimentoREST(Alimento alimento, Activity context, Integer idSyncREST) {
		ConnectREST connRESTInsert = new ConnectREST(ConstantesREST.DELETE_ALIMENTO_SERVICE, context);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idLogin", LoginActivity.usuarioLogado.getUser().getId().toString());
		params.put("codigo", alimento.getId_alimentos() + "");

		if (idSyncREST != null) {
			connRESTInsert.sincronizacaoDeOfflinePorID(idSyncREST);
		}

		connRESTInsert.asyncExecute(params);
	}

	// ******************* EXERCICIO

	public void insertExercicioREST(Exercicios exercicio, Activity context, Integer idSyncREST) {
		ConnectREST connRESTInsert = new ConnectREST(ConstantesREST.INSERT_EXERCICIO_SERVICE, context);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idLogin", LoginActivity.usuarioLogado.getUser().getId().toString());
		params.put("descricao", exercicio.getDescricao());
		params.put("tipo", exercicio.getTipo());
		params.put("modalidade", exercicio.getModalidade());
		params.put("intensidade", exercicio.getIntensidade());
		params.put("duracao", exercicio.getDuracao() + "");
		params.put("data", exercicio.getData().getTimeInMillis() + "");
		params.put("codigo", exercicio.getId() + "");

		if (idSyncREST != null) {
			connRESTInsert.sincronizacaoDeOfflinePorID(idSyncREST);
		}

		connRESTInsert.asyncExecute(params);
	}

	public void deleteExercicioREST(Exercicios exercicio, Activity context, Integer idSyncREST) {
		ConnectREST connRESTInsert = new ConnectREST(ConstantesREST.DELETE_EXERCICIO_SERVICE, context);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idLogin", LoginActivity.usuarioLogado.getUser().getId().toString());
		params.put("codigo", exercicio.getId() + "");

		if (idSyncREST != null) {
			connRESTInsert.sincronizacaoDeOfflinePorID(idSyncREST);
		}

		connRESTInsert.asyncExecute(params);
	}

	// ******************* GLICEMIA

	public void insertGlicemiaREST(Glicemia glicemia, Activity context, Integer idSyncREST) {
		ConnectREST connRESTInsert = new ConnectREST(ConstantesREST.INSERT_GLICEMIA_SERVICE, context);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idLogin", LoginActivity.usuarioLogado.getUser().getId().toString());
		params.put("tipo", glicemia.getTipo());
		params.put("obs", glicemia.getObs());
		params.put("medida", glicemia.getMedida() + "");
		params.put("codigo", glicemia.getId() + "");
		params.put("data", glicemia.getData().getTimeInMillis() + "");

		if (idSyncREST != null) {
			connRESTInsert.sincronizacaoDeOfflinePorID(idSyncREST);
		}

		connRESTInsert.asyncExecute(params);
	}

	public void deleteGlicemiaREST(Glicemia glicemia, Activity context, Integer idSyncREST) {
		ConnectREST connRESTInsert = new ConnectREST(ConstantesREST.DELETE_GLICEMIA_SERVICE, context);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idLogin", LoginActivity.usuarioLogado.getUser().getId().toString());
		params.put("codigo", glicemia.getId() + "");

		if (idSyncREST != null) {
			connRESTInsert.sincronizacaoDeOfflinePorID(idSyncREST);
		}
		connRESTInsert.asyncExecute(params);
	}

	// ******************* INSULINA
	public void insertInsulinaREST(Insulina insulina, Activity context, Integer idSyncREST) {
		ConnectREST connRESTInsert = new ConnectREST(ConstantesREST.INSERT_INSULINA_SERVICE, context);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idLogin", LoginActivity.usuarioLogado.getUser().getId().toString());
		params.put("obs", insulina.getObs());
		params.put("tipo", insulina.getTipo());
		params.put("quantidade", Math.round(insulina.getQtd()) + "");
		params.put("data", insulina.getData().getTimeInMillis() + "");
		params.put("codigo", insulina.getId() + "");

		if (idSyncREST != null) {
			connRESTInsert.sincronizacaoDeOfflinePorID(idSyncREST);
		}

		connRESTInsert.asyncExecute(params);
	}

	public void deleteInsulinaREST(Insulina insulina, Activity context, Integer idSyncREST) {
		ConnectREST connRESTInsert = new ConnectREST(ConstantesREST.DELETE_INSULINA_SERVICE, context);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idLogin", LoginActivity.usuarioLogado.getUser().getId().toString());
		params.put("codigo", insulina.getId() + "");

		if (idSyncREST != null) {
			connRESTInsert.sincronizacaoDeOfflinePorID(idSyncREST);
		}
		connRESTInsert.asyncExecute(params);
	}

	// ******************* REFEICAO
	public void insertRefeicaoREST(Refeicao refeicao, Activity context, Integer idSyncREST) {
		ConnectREST connRESTInsert = new ConnectREST(ConstantesREST.INSERT_REFEICAO_SERVICE, context);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idLogin", LoginActivity.usuarioLogado.getUser().getId().toString());
		params.put("carboidrato", Math.round(refeicao.getCarboidrato()) + "");
		params.put("peso", Math.round(refeicao.getPeso()) + "");
		params.put("obs", refeicao.getObs());
		params.put("tipo", refeicao.getTipo());
		params.put("data", refeicao.getData().getTimeInMillis() + "");
		params.put("codigo", refeicao.getId() + "");

		if (idSyncREST != null) {
			connRESTInsert.sincronizacaoDeOfflinePorID(idSyncREST);
		}

		connRESTInsert.asyncExecute(params);
	}

	public void deleteRefeicaoREST(Refeicao refeicao, Activity context, Integer idSyncREST) {
		ConnectREST connRESTInsert = new ConnectREST(ConstantesREST.DELETE_REFEICAO_SERVICE, context);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idLogin", LoginActivity.usuarioLogado.getUser().getId().toString());
		params.put("codigo", refeicao.getId() + "");

		if (idSyncREST != null) {
			connRESTInsert.sincronizacaoDeOfflinePorID(idSyncREST);
		}
		connRESTInsert.asyncExecute(params);
	}

	public void sincronizarRegistros(Activity context) {
		
		SyncRESTDao synRestDAO = new SyncRESTDao(context);

		List<SyncREST> list = synRestDAO.listar();

		if(list!=null && list.size()>0){
			SyncREST sr = list.get(0);
			try {
				switch (sr.getTipoTabela()) {
				// ALIMENTO
				case 1:
					TabelaDao tabelaDAO = new TabelaDao(context);
					Alimento alimento = tabelaDAO.findByID(sr.getCodigo());
						if (sr.getOperacao() == 1) {
							insertAlimentoREST(alimento, context, sr.getId());
						} else {
							alimento = new Alimento();
							alimento.setId_alimentos(sr.getCodigo());
							deleteAlimentoREST(alimento, context, sr.getId());
						}
					break;
				// EXERCICIO
				case 2:
					ExerciciosDao exDAO = new ExerciciosDao(context);
					try {
						Exercicios ex = exDAO.getByID(sr.getCodigo());
							if (sr.getOperacao() == 1) {
								insertExercicioREST(ex, context, sr.getId());
							} else {
								ex = new Exercicios();
								ex.setId(sr.getCodigo());
								deleteExercicioREST(ex, context, sr.getId());
							}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				// GLICEMIA
				case 3:
					GlicemiaDao glmDAO = new GlicemiaDao(context);
					try {
						Glicemia glcm = glmDAO.findByID(sr.getCodigo());
							if (sr.getOperacao() == 1) {
								insertGlicemiaREST(glcm, context, sr.getId());
							} else {
								glcm = new Glicemia();
								glcm.setId(sr.getCodigo());
								deleteGlicemiaREST(glcm, context, sr.getId());
							}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				// INSULINA
				case 4:
					InsulinaDao insulinaDAO = new InsulinaDao(context);
					try {
						Insulina insulina = insulinaDAO.findByID(sr.getCodigo());
							if (sr.getOperacao() == 1) {
								insertInsulinaREST(insulina, context, sr.getId());
							} else {
								insulina = new Insulina();
								insulina.setId(sr.getCodigo());
								deleteInsulinaREST(insulina, context, sr.getId());
							}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				// REFEICAO
				case 5:
					RefeicaoDao refeicaoDAO = new RefeicaoDao(context);
					try {
						Refeicao refeicao = refeicaoDAO.findByID(sr.getCodigo());
							if (sr.getOperacao() == 1) {
								insertRefeicaoREST(refeicao, context, sr.getId());
							} else {
								refeicao = new Refeicao();
								refeicao.setId(sr.getCodigo());
								deleteRefeicaoREST(refeicao, context, sr.getId());
							}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {

			}
		}
	}
	
	public int qtdeRegistrosSincronizacao(Activity context) {
		SyncRESTDao dao = new SyncRESTDao(context);
		List<SyncREST> list = dao.listar();

		if (list != null) {
			return list.size();
		} else {
			return 0;
		}
	}
}
