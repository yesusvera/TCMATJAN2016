package br.com.diabetesvirtual.grafico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.dao.InsulinaDao;
import br.com.diabetesvirtual.model.Insulina;
import br.com.diabetesvirtual.model.InsulinaTipos;
import br.com.diabetesvirtual.util.Mensagem;

public class InsulinaGrafico extends Activity {

	GraphViewData[] data;
	InsulinaDao insulinaDao;
	List<Insulina> lista;
	List<Insulina> lista2;
	SimpleDateFormat dateFormat;
	Insulina insulina;
	GraphView graphView;
	LinearLayout layout;
	ProgressDialog alerta;
	Handler handler = new Handler();
	RadioGroup radioGroup;
	RadioButton rapida;
	RadioButton lenta;
	long size = 0;
	double port = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.insulina_grafico);	
		super.onCreate(savedInstanceState);
		layout = (LinearLayout) findViewById(R.id.grafico_insulina);	
		dateFormat = new SimpleDateFormat("dd/MM-HH", Locale.getDefault());	
		alerta = new ProgressDialog(this);
		try {
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando gráfico, aguarde..",false,true);
			this.carregarLista(R.id.lenta);
		} catch (Exception e) {
			Log.e("GLICEMIA LIST ACTIVITY", e.getMessage());
		}
	}
				
	public void carregarLista(final int id) {
		new Thread() {
			@Override
			public void run() {				
				try {
					insulinaDao = new InsulinaDao(InsulinaGrafico.this);
					lista = new ArrayList<Insulina>();
					lista = insulinaDao.listarPorTipoASC(InsulinaTipos.RAPIDA);
					insulinaDao = new InsulinaDao(InsulinaGrafico.this);
					lista2 = new ArrayList<Insulina>();
					lista2 = insulinaDao.listarPorTipoASC(InsulinaTipos.LENTA);
					atualizaTela();
				} catch (Exception e) {
					lista = new ArrayList<Insulina>();
					lista2 = new ArrayList<Insulina>();
					atualizaTela();
				}
			}
		}.start();
	};
				
	public void atualizaTela() {
		handler.post(new Runnable() {			
			@Override
			public void run() {
				if (lista == null || lista.size()<3|| lista2 == null || lista2.size()<3) {
					Mensagem msg = new Mensagem();
					msg.mensagemToast(InsulinaGrafico.this, "Dados insuficientes para gerar gráfico. (Mínimo de 3 dados de cada tipo.)");
					finish();
				} else {
					graphView = new LineGraphView(InsulinaGrafico.this,"");
// add a primeira serie no grafico - insulina rapida
					data = new GraphViewData[lista.size()];
					for (int i = 0; i < lista.size(); i++) {
						insulina = new Insulina();
						insulina = lista.get(i);
						data[i] = new GraphViewData((double)insulina.getData().getTimeInMillis(), (double)insulina.getQtd());
					}
					size = insulina.getData().getTimeInMillis();
					graphView.addSeries(new GraphViewSeries(InsulinaTipos.RAPIDA.getNome(), new GraphViewSeriesStyle(Color.BLUE, 3),data));					
					data = new GraphViewData[lista2.size()]; // add a segunfa serie no grafico - insulina rapida
					for (int j = 0; j < lista2.size(); j++) {
						insulina = new Insulina();
						insulina = lista2.get(j);
						data[j] = new GraphViewData((double)insulina.getData().getTimeInMillis(),(double)insulina.getQtd());
					}
					graphView.addSeries(new GraphViewSeries(InsulinaTipos.LENTA.getNome(), new GraphViewSeriesStyle(Color.YELLOW, 3),data));				
					if (size < insulina.getData().getTimeInMillis()) {
						size = insulina.getData().getTimeInMillis();
					} 
					graphView.setScrollable(true);
					graphView.setScalable(true);
					graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.parseColor("#CDC9C9"));
					graphView.getGraphViewStyle().setVerticalLabelsColor(Color.parseColor("#FFA500"));
					graphView.getGraphViewStyle().setGridColor(Color.WHITE);
					graphView.getGraphViewStyle().setTextSize(18);
					port = lista.get(lista.size()-1).getData().getTimeInMillis()-lista.get(0).getData().getTimeInMillis();
					graphView.setViewPort(lista.get(0).getData().getTimeInMillis(), port);
					graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
					@Override
					public String formatLabel(double value, boolean isValueX) {
						if (isValueX) {
							long w = (long) value;
							if (w <= size) {
								Date d = new Date(w);
								String x = dateFormat.format(d);
								return x+"h";
							} 
						}else {
							int z = (int) value;
							return z+" UI"; 
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

