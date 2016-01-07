package br.com.diabetesvirtual.prop;

import java.util.ResourceBundle;

public class ConstantesREST {

	public final static String HOST_KEY = "host";
	public final static String AUTHENTICATION_SERVICE = "authentication_service";
	public final static String INSERT_ALIMENTO_SERVICE = "inserir_alimento_service";
	public final static String INSERT_EXERCICIO_SERVICE = "inserir_exercicio_service";
	public final static String INSERT_GLICEMIA_SERVICE = "inserir_glicemia_service";
	public final static String INSERT_INSULINA_SERVICE = "inserir_insulina_service";
	public final static String INSERT_REFEICAO_SERVICE = "inserir_refeicao_service";
	public final static String INSERT_ITEM_REFEICAO_SERVICE = "inserir_item_refeicao_service";
	

	public final static String DELETE_ALIMENTO_SERVICE = "excluir_alimento_service";
	public final static String DELETE_EXERCICIO_SERVICE = "excluir_exercicio_service";
	public final static String DELETE_GLICEMIA_SERVICE = "excluir_glicemia_service";
	public final static String DELETE_INSULINA_SERVICE = "excluir_insulina_service";
	public final static String DELETE_REFEICAO_SERVICE = "excluir_refeicao_service";
	
	public static String getValue(String KEY_REST){
		ResourceBundle rb = ResourceBundle.getBundle("br.com.diabetesvirtual.prop.servicos_rest");
		return rb.getString(KEY_REST);
	}
	
	public static String getURLService(String KEY_REST){
		return getValue(HOST_KEY) + getValue(KEY_REST);
	}
	
	public static void main(String[] args) {
		ConstantesREST.getURLService(INSERT_ALIMENTO_SERVICE); //http://192.168.25.194:8080/DoceDesafioWS/service/alimentos/excluir
	}
}
