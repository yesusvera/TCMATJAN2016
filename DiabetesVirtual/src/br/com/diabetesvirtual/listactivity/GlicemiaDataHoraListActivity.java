package br.com.diabetesvirtual.listactivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.adapter.GlicemiaAdapter;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.grafico.GlicemiaGrafico;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.pdf.GlicemiaPdf;
import br.com.diabetesvirtual.util.DialogDetalhes;
import br.com.diabetesvirtual.util.Mensagem;
import br.com.diabetesvirtual.util.OrdenaListaASCtoDESC;

public class GlicemiaDataHoraListActivity extends ActivityBase implements OnClickListener{
	
	private final Calendar c = Calendar.getInstance();
	GlicemiaDao glicemiaDao;
	Calendar data1= Calendar.getInstance();
	Calendar data2= Calendar.getInstance();
	ListView listView;
	public static List<Glicemia> lista;
	GlicemiaAdapter adapter;
	ProgressDialog alerta;
	Handler handler = new Handler();
	
	Dialog dialogo_time;
	Button b_ok_hora;
	Button b_cancel_hora;
	TimePicker timepicker_hora_1;
	TimePicker timepicker_hora_2;
	TextView text_hora_selecionada;
	int hora1;
	int minuto1;
	int hora2;
	int minuto2;
	
	Dialog dialog_data;
	Button b_ok_data;
	Button b_cancel_data;
	DatePicker datePicker_data_1;
	DatePicker datePicker_data_2;
	TextView text_data_selecionada;
	int ano1;
	int mes1;
	int dia1;
	int ano2;
	int mes2;
	int dia2;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.glicemia_data_hora);
		
		listView = (ListView) findViewById(R.id.glicemia_lista);
		text_hora_selecionada = (TextView) findViewById(R.id.hora_selecionada);
		dialogo_time = new Dialog(this, R.style.tema_dialogo);
		dialogo_time.setTitle("Selecione o horário");
		dialogo_time.setContentView(R.layout.dialog_intervalo_hora);
		timepicker_hora_1 = (TimePicker) dialogo_time.findViewById(R.id.hora1);
		timepicker_hora_2 = (TimePicker) dialogo_time.findViewById(R.id.hora2);	
		b_ok_hora = (Button) dialogo_time.findViewById(R.id.b_ok);
		b_cancel_hora = (Button) dialogo_time.findViewById(R.id.b_cancel);
		b_ok_hora.setOnClickListener(this);
		b_cancel_hora.setOnClickListener(this);
		
		//DATA PICKER DIALOG CUSTOMIZADO
		
		text_data_selecionada = (TextView) findViewById(R.id.tipo_glicemia);	
		dialog_data = new Dialog(this, R.style.tema_dialogo);
		dialog_data.setTitle("Selecione  a data");
		dialog_data.setContentView(R.layout.dialog_intervalo_data);
		datePicker_data_1 = (DatePicker) dialog_data.findViewById(R.id.data1);
		datePicker_data_2 = (DatePicker) dialog_data.findViewById(R.id.data2);	
		b_ok_data = (Button) dialog_data.findViewById(R.id.b_ok2);
		b_cancel_data  =(Button) dialog_data.findViewById(R.id.b_cancel2);
		b_ok_data.setOnClickListener(this);
		b_cancel_data.setOnClickListener(this);
		
		this.inicializarVariaveis();
	}
	
	public void inicializarVariaveis() {
		data1.set(c.get(Calendar.YEAR), 0, 1, 0, 0);
		data2.set(c.get(Calendar.YEAR), 11, 31, 23, 59);
		text_hora_selecionada.setText("HORA - De 00h:00min até 23h:59min");
		text_data_selecionada.setText("DATA - De 01/"+data1.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())+"/"+c.get(Calendar.YEAR)+" até 31/"+data2.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())+"/"+c.get(Calendar.YEAR));
	}

	public void mostrarDialogTime(View view) {
		timepicker_hora_1.setIs24HourView(true);
		timepicker_hora_2.setIs24HourView(true);	
		timepicker_hora_1.setCurrentHour(data1.get(Calendar.HOUR_OF_DAY));
		timepicker_hora_1.setCurrentMinute(data1.get(Calendar.MINUTE));
		timepicker_hora_2.setCurrentHour(data2.get(Calendar.HOUR_OF_DAY));
		timepicker_hora_2.setCurrentMinute(data2.get(Calendar.MINUTE));	
		dialogo_time.show();
	}
	
	public void mostrarDialogData(View view) {
		datePicker_data_1.init(data1.get(Calendar.YEAR), data1.get(Calendar.MONTH), data1.get(Calendar.DAY_OF_MONTH), null);
		datePicker_data_2.init(data2.get(Calendar.YEAR), data2.get(Calendar.MONTH), data2.get(Calendar.DAY_OF_MONTH), null);
		dialog_data.show();
	}
	
	public void confirmaHora() {
		hora1 = timepicker_hora_1.getCurrentHour();
		minuto1 = timepicker_hora_1.getCurrentMinute();
		hora2 = timepicker_hora_2.getCurrentHour();
		minuto2 = timepicker_hora_2.getCurrentMinute();
		data1.set(Calendar.HOUR_OF_DAY, hora1);
		data1.set(Calendar.MINUTE, minuto1);
		data2.set(Calendar.HOUR_OF_DAY, hora2);
		data2.set(Calendar.MINUTE, minuto2);		
		text_hora_selecionada.setText("HORA - De "+data1.get(Calendar.HOUR_OF_DAY)+"h"+":"+data1.get(Calendar.MINUTE)+"min"+" até "+data2.get(Calendar.HOUR_OF_DAY)+"h"+":"+data2.get(Calendar.MINUTE)+"min");
		dialogo_time.dismiss();				
	}
	
	public void confirmarData() {
		ano1 = datePicker_data_1.getYear();
		mes1 = datePicker_data_1.getMonth();
		dia1 = datePicker_data_1.getDayOfMonth();
		ano2 = datePicker_data_2.getYear();
		mes2 = datePicker_data_2.getMonth();
		dia2 = datePicker_data_2.getDayOfMonth();
		if ((ano1*365+mes1*30+dia1)>(ano2*365+mes2*30+dia2)) {
			Mensagem msg = new Mensagem();
			msg.mensagemToast(this, "A data inicial deve ser menor que a final.");
		} else {
			data1.set(ano1, mes1, dia1);
			data2.set(ano2, mes2, dia2);
			text_data_selecionada.setText("DATA - De "+dia1+"/"+data1.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())+"/"+ano1+" até "+dia2+"/"+data2.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())+"/"+ano2+"");
			dialog_data.dismiss();
		}
	}
	
	public void carregarLista() {
		new Thread() {
			@Override
			public void run() {
				try {
					glicemiaDao = new GlicemiaDao(GlicemiaDataHoraListActivity.this);
					lista = new ArrayList<Glicemia>();
					lista = glicemiaDao.bucarPorDataHora(data1, data2); 
					atualizaTela();
				} catch (Exception e) {
					lista = new ArrayList<Glicemia>();
					atualizaTela();
				}	
			}
		}.start();	
	}
	
	public void atualizaTela() {
		handler.post(new Runnable() {		
			@Override
			public void run() {	
				OrdenaListaASCtoDESC<Glicemia> ordena = new OrdenaListaASCtoDESC<Glicemia>(lista);
				lista = ordena.ordenar();
				adapter = new GlicemiaAdapter(GlicemiaDataHoraListActivity.this, R.layout.glicemia_item_da_lista_1, lista);
				listView.setAdapter(adapter);
				if (lista == null || lista.size()==0) {
					Mensagem msg = new Mensagem();
					msg.mensagemToast(GlicemiaDataHoraListActivity.this, "Nenhum resgitro foi encontrado");
				} 
				alerta.dismiss();
			}
		});
	}
	
	public void voltar() {
		dialogo_time.dismiss();
		dialog_data.dismiss();
	}
	
	public void voltar_main(View view) {
		finish();
		return;
	}

	public void pesquisar(View view) {
		alerta = ProgressDialog.show(this, "Carregando..", "Carregando dados, esta operação pode levar alguns segundos..");
		this.carregarLista(); 
	}
	
	@Override
	public void onClick(View v) {
		if (v == b_ok_hora) {
			this.confirmaHora();
		} else if (v == b_ok_data) {
			this.confirmarData();
		} else {
			this.voltar();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Gerar PDF");
		menu.add(0, 1, 1, "Gráfico");
		menu.add(0, 2, 2, "Estatística");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case 0:
				GlicemiaPdf pdf = new GlicemiaPdf(this,"Relatório Glicemia data-hora",lista);
				pdf.gerarPdf();				
				break;
			case 1:	
				Intent i = new Intent(this, GlicemiaGrafico.class);
				i.putExtra("extra", "GlicemiaDataHoraListActivity");
				startActivity(i);
				break;
			case 2:
				DialogDetalhes d = new DialogDetalhes(this);
				d.DialogEstatistica(lista);
			break;
		}
		return true;
	}
}
