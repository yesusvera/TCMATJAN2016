package br.com.diabetesvirtual.grafico;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.listactivity.ExerciciosListActivity;
import br.com.diabetesvirtual.model.Exercicios;
import br.com.diabetesvirtual.util.Mensagem;

public class ExercicioGrafico extends Activity{
	GraphViewData[] data;
	List<Exercicios> lista;
	SimpleDateFormat dateFormat;
	Exercicios exercicios;
	GraphView graphView;
	LinearLayout layout;
	ProgressDialog alerta;
	Handler handler = new Handler();
	int size;
	Mensagem msg;	
	int j;
	int posicaoCerta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercicios_grafico);	
		layout = (LinearLayout) findViewById(R.id.grafico_exe);		
		dateFormat = new SimpleDateFormat("dd/MM-HH", Locale.getDefault());	
		alerta = new ProgressDialog(this);
		try {						
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando gráfico, aguarde..",false,true);
			this.carregarLista();
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao carregar gráfico.");
		}		
	}
	
	public void carregarLista() {
		new Thread() {
			@Override
			public void run() {
				try {
					lista = ExerciciosListActivity.lista;
					atualizaTela();
				} catch (Exception e) {
					lista = null;
					atualizaTela();
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
					msg.mensagemToast(ExercicioGrafico.this, "Dados insuficientes para gerar gráfico. (Mínimo de 3 dados)");
					finish();
					return;
				} else {
					graphView = new BarGraphView(ExercicioGrafico.this,"");
					size = lista.size();
					data = new GraphViewData[size];
					j=size-1;
					for (int i = 0; i < size; i++) {
						exercicios = new Exercicios();
						exercicios = lista.get(i);
						data[j] = new GraphViewData(j, exercicios.getOrdem());		
						j--;
					}
					graphView.addSeries(new GraphViewSeries(data));					
					graphView.setViewPort(0, lista.size()-1);
					graphView.setScrollable(true);
					graphView.setScalable(true);
					graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.parseColor("#CDC9C9"));
					graphView.getGraphViewStyle().setVerticalLabelsColor(Color.parseColor("#FFA500"));
					graphView.getGraphViewStyle().setGridColor(Color.WHITE);
					graphView.getGraphViewStyle().setTextSize(18);
					graphView.setManualYAxisBounds(3, 0);
					graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
					@Override
					public String formatLabel(double value, boolean isValueX) {
						if (isValueX) {
							int w = (int) value;
							if (w < size) {
								posicaoCerta =  (int) (size-1) - (int) value;
								Date d = lista.get(posicaoCerta).getData().getTime();
								String x = dateFormat.format(d);
								return x+"h";
							} 
						}	
						return null;
					}
				});
//				graphView.setVerticalLabels(new String[] {ExerciciosIntensidade.getNome(ExercicioGrafico.this, ExerciciosIntensidade.INTENSO.getCod()), 
//						ExerciciosIntensidade.getNome(ExercicioGrafico.this, ExerciciosIntensidade.MODERADO.getCod()), ExerciciosIntensidade.getNome(ExercicioGrafico.this, ExerciciosIntensidade.LEVE.getCod())
//						,"Nenhum"});
					
				graphView.setVerticalLabels(new String[] {"Muito Leve","Leve","Moderado","Intenso","Muito Intenso", "Nenhum"});
				layout.addView(graphView);
				alerta.dismiss();
				}	
			};
		});
	}
	
}
