package br.com.diabetesvirtual.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.grafico.GlicemiaGrafico;
import br.com.diabetesvirtual.grafico.InsulinaGrafico;
import br.com.diabetesvirtual.grafico.RefeicaoGrafico;
import br.com.diabetesvirtual.listactivity.ExercicioPorTipoListActivity;
import br.com.diabetesvirtual.listactivity.ExerciciosListActivity;
import br.com.diabetesvirtual.listactivity.GlicemiaDataHoraListActivity;
import br.com.diabetesvirtual.listactivity.GlicemiaListActivity;
import br.com.diabetesvirtual.listactivity.GlicemiaPorTipoListActivity;
import br.com.diabetesvirtual.listactivity.GlicemiaVariacaoListActivity;
import br.com.diabetesvirtual.listactivity.HistoricoListActivity;
import br.com.diabetesvirtual.listactivity.HistoricoTipoListActivity;
import br.com.diabetesvirtual.listactivity.InsulinaDataHoraListActivity;
import br.com.diabetesvirtual.listactivity.InsulinaListActivity;
import br.com.diabetesvirtual.listactivity.InsulinaPorTipoListActivity;
import br.com.diabetesvirtual.listactivity.RefeicaoDataHoraListActivity;
import br.com.diabetesvirtual.listactivity.RefeicaoDetalhesListActivity;
import br.com.diabetesvirtual.listactivity.RefeicaoListActivity;
import br.com.diabetesvirtual.listactivity.RefeicaoVariacaoListActivity;
import br.com.diabetesvirtual.util.ConnectDatabase;

public class RelatorioActivity extends Activity {

	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relatorios);
	}
// metodos dos relatorios da GLICEMIA	
	public void glic_geral(View view) {  
		Intent intent = new Intent(this,GlicemiaListActivity.class);
		startActivity(intent);
	}
	
	public void glic_variacaoGlicemica(View view) {
		Intent intent = new Intent(this, GlicemiaVariacaoListActivity.class);
		startActivity(intent);
	}
	
	public void glic_grafico(View view) {
		Intent intent = new Intent(this, GlicemiaGrafico.class);
		intent.putExtra("extra", "todos");
		startActivity(intent);
	}
	
	public void glic_dataHora(View view) {
		Intent intent = new Intent(this, GlicemiaDataHoraListActivity.class);
		startActivity(intent);
	}
	
	public void glicemiaRelatorioPorTipo(View view) {
		Intent intent = new Intent(this, GlicemiaPorTipoListActivity.class);
		startActivity(intent);
	}
	
// metodos dos relatorios da INSULINA	
	public void insulina_geral(View view) {
		Intent intent = new Intent(this, InsulinaListActivity.class);
		startActivity(intent);
	}
	
	public void insulina_grafico(View view) {
		Intent i = new Intent(this, InsulinaGrafico.class);
		startActivity(i);
	}
	
	public void insulina_dataHora(View view) {
		Intent i = new Intent(this, InsulinaDataHoraListActivity.class);
		startActivity(i);
	}
	
	public void relatorioPorTipo(View view) {
		Intent i = new Intent(this, InsulinaPorTipoListActivity.class);
		startActivity(i);
	}
	
	
// metodos dos relatorios da REFEICAO	
	public void ref_geral(View view) {
		Intent intent = new Intent(this, RefeicaoListActivity.class);
		startActivity(intent);
	}
	
	public void ref_variacao(View view) {
		Intent intent = new Intent(this, RefeicaoVariacaoListActivity.class);
		startActivity(intent);
	}
	
	public void ref_data_hora(View view) {
		Intent intent = new Intent(this, RefeicaoDataHoraListActivity.class);
		startActivity(intent);
	}
	
	public void refeicao_grafico(View view) {
		Intent i = new Intent(this, RefeicaoGrafico.class);
		i.putExtra("extra", "todos");
		startActivity(i);
	}

	public void ref_detalhes(View view) {
		Intent intent = new Intent(this, RefeicaoDetalhesListActivity.class);
		startActivity(intent);
	}
	
// metodos dos relatorios da ATIVIDA FISICA	
	public void exe_relatorio_geral(View view) {
		Intent i = new Intent(this, ExerciciosListActivity.class);
		startActivity(i);
	}
	
	public void exercicioRelatorioPorTipo(View view) {
		Intent intent = new Intent(this, ExercicioPorTipoListActivity.class);
		startActivity(intent);
	}
	
	// metodos dos relatorios do historico
	public void historico_geral(View view) {
		Intent i = new Intent(this, HistoricoListActivity.class);
		startActivity(i);
	}
	
	public void historico_tipo(View view) {
		Intent i = new Intent(this, HistoricoTipoListActivity.class);
		startActivity(i);
	}

	
	
}
