package br.com.diabetesvirtual.grafico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.listactivity.GlicemiaDataHoraListActivity;
import br.com.diabetesvirtual.listactivity.GlicemiaListActivity;
import br.com.diabetesvirtual.listactivity.GlicemiaVariacaoListActivity;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.util.Mensagem;

public class GlicemiaGrafico extends Activity{

	GraphViewData[] data;
	GlicemiaDao glicemiaDao;
	List<Glicemia> lista;
	SimpleDateFormat dateFormat;
	Glicemia glicemia;
	GraphView graphView;
	LinearLayout layout;
	ProgressDialog alerta;
	Handler handler = new Handler();
	int size;
	int y =-1;
	int j;
	int posicaoCerta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.glicemia_grafico);	
		layout = (LinearLayout) findViewById(R.id.grafico_glic);		
		dateFormat = new SimpleDateFormat("dd/MM-HH", Locale.getDefault());	
		alerta = new ProgressDialog(this);
		try {						
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando gráfico, aguarde..",false,true);
			this.carregarLista();
		} catch (Exception e) {
			Log.e("GLICEMIA LIST ACTIVITY", e.getMessage());
		}
	}
				
	public void carregarLista() {
		new Thread() {
			@Override
			public void run() {
				try {
					Intent i = getIntent();
					String x = (String) i.getExtras().get("extra");
					if (x.equals("GlicemiaListActivity")) {
						lista = GlicemiaListActivity.lista;
					} else if (x.equals("GlicemiaVariacaoListActivity")) {
						lista = GlicemiaVariacaoListActivity.lista;
					} else if (x.equals("GlicemiaDataHoraListActivity")) {
						lista = GlicemiaDataHoraListActivity.lista;
					} else {
						glicemiaDao = new GlicemiaDao(GlicemiaGrafico.this);
						lista = new ArrayList<Glicemia>();
						lista = glicemiaDao.getAll();
					}
					atualizaTela();
				} catch (Exception e) {
					Log.e("GLICEMIA LIST ACTIVITY - THREAD CARREGAR LISTA", e.getMessage());
				}
			}
		}.start();
	};
				
	public void atualizaTela() {
		handler.post(new Runnable() {			
			@Override
			public void run() {
				if (lista == null || lista.size()<3) {
					Mensagem msg = new Mensagem();
					msg.mensagemToast(GlicemiaGrafico.this, "Dados insuficientes para gerar gráfico. (Mínimo de 3 dados)");
					finish();
					return;
				} else {
					size = lista.size();
					data = new GraphViewData[size];
					j=size-1;
					for (int i = 0; i < size; i++) {
						glicemia = new Glicemia();
						glicemia = lista.get(i);
						data[j] = new GraphViewData(j, glicemia.getMedida());
						j--;
					}
					graphView = new LineGraphView(GlicemiaGrafico.this,"");
					graphView.addSeries(new GraphViewSeries(data));
					graphView.setViewPort(0, size-1);
					graphView.setScrollable(true);
					graphView.setScalable(true);
					graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.parseColor("#CDC9C9"));
					graphView.getGraphViewStyle().setVerticalLabelsColor(Color.parseColor("#FFA500"));
					graphView.getGraphViewStyle().setGridColor(Color.WHITE);
					graphView.getGraphViewStyle().setTextSize(18);
					graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
					@Override
					public String formatLabel(double value, boolean isValueX) {
						if (isValueX) { 
							if (value < size) {
								posicaoCerta =  (int) (size-1) - (int) value;
								String x = dateFormat.format(lista.get(posicaoCerta).getData().getTimeInMillis());
								return x+"h";
							} 
						} else {
							int z = (int) value;
							return z+"mg/dl"; 
						}
						return null;
					}
				});
				layout.addView(graphView);
				alerta.dismiss();
				}	
			};
		});
	}
}
