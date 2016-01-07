package br.com.diabetesvirtual.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.model.Exercicios;
import br.com.diabetesvirtual.model.Glicemia;

public class DialogDetalhes {
	Context context;
	Dialog dialog;
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm",Locale.getDefault());
	
	public DialogDetalhes(Context ctx) {
		this.context = ctx;
	}
	
	public void DialogExercicios(Exercicios exercicio) {
		dialog = new Dialog(context, R.style.tema_dialogo);
		dialog.setTitle("Detalhes");
		dialog.setContentView(R.layout.dialog_exercicio);
		TextView dia = (TextView) dialog.findViewById(R.id.exe_det_dia);
		TextView hora = (TextView) dialog.findViewById(R.id.exe_det_hora);
		TextView desc = (TextView) dialog.findViewById(R.id.exe_det_desc);
		TextView duracao = (TextView) dialog.findViewById(R.id.exe_det_duracao);
		TextView tipo = (TextView) dialog.findViewById(R.id.exe_det_tipo);
		TextView intensidade = (TextView) dialog.findViewById(R.id.exe_det_intensidade);
		Button ok = (Button) dialog.findViewById(R.id.exe_b_ok);
		
		dia.setText(format_dia.format(exercicio.getData().getTime())+"\n"+exercicio.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
		hora.setText(format_hora.format(exercicio.getData().getTime()));
		desc.setText(exercicio.getDescricao());
		duracao.setText(exercicio.getDuracao()+" minutos");
		tipo.setText(exercicio.getTipo());
		//intensidade.setText(ExerciciosIntensidade.getNome(context, exercicio.getIntensidade().getCod()));
		intensidade.setText(exercicio.getIntensidade());
		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				dialog.dismiss();
			}
		});		
		dialog.show();
	}
	
	public void DialogEstatistica(List<Glicemia> lista) {
		if (lista!=null && lista.size()>1) { // 2 ou mais dados
			double DoubleMedia=0;
			double DoubleDesvio=0;
	 		dialog = new Dialog(context, R.style.tema_dialogo);
			dialog.setTitle("Estatísticas");
			dialog.setContentView(R.layout.dialog_estatistica);		
			TextView qtd = (TextView) dialog.findViewById(R.id.estatistica_qtd);
			TextView media = (TextView) dialog.findViewById(R.id.estatistica_media);
			TextView desvio = (TextView) dialog.findViewById(R.id.estatistica_desvio);		
			DoubleMedia= this.media(lista);
			DoubleDesvio= this.desvioPadrao(lista, DoubleMedia);
			media.setText(Formatos.formataDouble(DoubleMedia));	
			desvio.setText(Formatos.formataDouble(DoubleDesvio));
			qtd.setText(lista.size()+"");
			dialog.show();
		} else {
			Mensagem msg = new Mensagem();
			msg.mensagemToast(context, "Dados insuficientes. (Mínimo de 2)");
		}
		
	}
	
	private double media(List<Glicemia> lista) {
		double x=0;
		for (Glicemia glic : lista) {
			x = x+glic.getMedida();
		}
		return x/(double)lista.size();
	}
	
	private double desvioPadrao(List<Glicemia> lista, double media) {
		double total=0;
		for (Glicemia glic : lista) {
			double quad = (glic.getMedida()-media);
			quad = quad*quad;
			total = total+quad;
		}
		total = total/(lista.size()-1);
		total = Math.sqrt(total);
		return total;
	}
	
	
	
}
