package br.com.diabetesvirtual.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.model.Exercicios;

public class ExerciciosAdapter extends ArrayAdapter<Exercicios>{
	List<Exercicios> lista;
	int resource;
	int dia=0;
	int cor;
	int atual=-1;
	Context context;
	List<String> cores;
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm", Locale.getDefault());
	
	public ExerciciosAdapter(Context context, int resource, List<Exercicios> objects) {
		super(context, resource, objects);
		cores = new ArrayList<String>();
		this.lista = objects;
		this.context = context;
		this.resource = resource;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {	   
		   ViewHolder holder;	   
		   if (convertView == null) {
			   holder = new ViewHolder();
			   LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			   convertView = inflater.inflate(resource, null);	   
			   holder.dia = (TextView) convertView.findViewById(R.id.exe_diaa);
			   holder.hora = (TextView) convertView.findViewById(R.id.exe_horaa);
			   holder.desc =  (TextView) convertView.findViewById(R.id.exe_descc);
			   holder.tipo = (TextView) convertView.findViewById(R.id.exe_tipoo);	
			   holder.intensidade = (TextView) convertView.findViewById(R.id.exe_intensidadee);
			   holder.modalidade = (TextView) convertView.findViewById(R.id.exe_modalidade);
			   holder.duracao = (TextView) convertView.findViewById(R.id.exe_duracaoo);		   
			   convertView.setTag(holder);  
		   } else {
			   holder = (ViewHolder) convertView.getTag();
		   }  
		   Exercicios exercicio = lista.get(position);	
		   if (exercicio != null) {
			   holder.dia.setText(exercicio.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())+", "+format_dia.format(exercicio.getData().getTime()));
			   holder.hora.setText(format_hora.format(exercicio.getData().getTime()));		   
			   holder.desc.setText(exercicio.getDescricao());		    
			   holder.duracao.setText("Duração: "+exercicio.getDuracao()+" min");  
			   holder.tipo.setText(exercicio.getTipo());
//			   holder.intensidade.setText(ExerciciosIntensidade.getNome(context, exercicio.getIntensidade().getCod()));
			   holder.intensidade.setText(exercicio.getIntensidade());
			   holder.modalidade.setText(exercicio.getModalidade());
			   if (position > atual) { //Caso a lista esteja descendo 
				   cor = this.setCoresLinhas(exercicio.getData().get(Calendar.DAY_OF_MONTH), position); //Altera a cor da linha
				   atual = position;
				   cores.add(cor+"");
			   } else {
				  cor = Integer.parseInt(cores.get(position)); //Caso a lista esta subindo recupera as cores
			   }
			   LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.exe_cor_da_linha);
			   layout.setBackgroundResource(cor);
		   }
		   return convertView;
	   }

	public int getCount() {
		return lista.size();  	
	}
	      
	public long getItemId(int position) {
	   return position;
	}   
	
	private static class ViewHolder {
		public TextView dia;
		public TextView hora;
		public TextView desc;
		public TextView duracao;
		public TextView tipo;
		public TextView intensidade;
		public TextView modalidade;
	}
		
	public int setCoresLinhas(int dia2, int position) {  //Seta a cor das linhas	      
		if(dia == 0) {
		   cor = R.color.gelo;
		   dia = dia2;
		} else if (dia != dia2) {
		   this.trocaCor(cor);
		   dia = dia2;
		} 
		atual = position;
		return cor; 
	}
		   
	public void trocaCor(int aux) { //Troca a cor qnd o dia eh mudado
	   if (aux == R.color.gelo) {
		   cor = R.color.verde_claro;
	   }	else {
		   cor = R.color.gelo;
	   }
	}

}
