package br.com.diabetesvirtual.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.model.Historico;

@SuppressLint("DefaultLocale")
public class HistoricoAdapter extends ArrayAdapter<Historico>{

	List<Historico> lista;
	List<Integer> aparece;
	List<String> lista_texto;
	String texto;
	int resource;
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yy",Locale.getDefault());
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm",Locale.getDefault());
	LinearLayout t;
	LinearLayout separador;
	TextView separador_dia;
	int dia=0;
	int cor;
	int atual=-1;
	Calendar c = Calendar.getInstance();
	
	
	public HistoricoAdapter(Context context, int resource, List<Historico> objects) {
		super(context, resource, objects);
		this.lista = objects;
		this.resource = resource;
		aparece = new ArrayList<Integer>();
		lista_texto = new ArrayList<String>();
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
		public TextView tipo;
		public TextView obs;
		public TextView dados;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {	   
		   ViewHolder holder;	   
		   if (convertView == null) {
			   holder = new ViewHolder();
			   LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			   convertView = inflater.inflate(resource, null);	   
			   holder.dia = (TextView) convertView.findViewById(R.id.historico_data);
			   holder.hora = (TextView) convertView.findViewById(R.id.historico_hora);
			   holder.tipo =  (TextView) convertView.findViewById(R.id.historico_tipo);
			   holder.obs = (TextView) convertView.findViewById(R.id.historico_obs);	
			   holder.dados = (TextView) convertView.findViewById(R.id.historico_dados);
			   convertView.setTag(holder);  
		   } else {
			   holder = (ViewHolder) convertView.getTag();
		   }  
		   Historico historico = lista.get(position);	
		   if (historico != null) {
			   c.setTimeInMillis(historico.getData());
			   holder.dia.setText(format_dia.format(historico.getData()));
			   holder.hora.setText(format_hora.format(historico.getData()));		   
			   holder.tipo.setText(historico.getTipo());		    
			   holder.obs.setText(historico.getObs());  
			   holder.dados.setText(historico.getDado());
			   separador = (LinearLayout) convertView.findViewById(R.id.separador);
			   if (position > atual) { //Caso a lista esteja descendo 
				   cor = this.setCoresLinhas(c.get(Calendar.DAY_OF_MONTH), position);
				   lista_texto.add(texto);
				   aparece.add(cor);
			   } else {
				  cor = aparece.get(position); //Caso a lista esta subindo recupera as cores
				  texto = lista_texto.get(position);
			   }			   
			   t = (LinearLayout) convertView.findViewById(R.id.historico_cor_item);			   
			   t.setBackgroundResource(R.color.verde_claro);
			   separador_dia = (TextView) convertView.findViewById(R.id.hist_dia_sepadaror);
			   separador_dia.setText(texto);
			   separador.setVisibility(cor);
			   if (historico.getTipo().equals("GLICEMIA")) {
				   t.setBackgroundResource(R.color.azul_celeste);
			   } else if (historico.getTipo().equals("INSULINA")) {
				   t.setBackgroundResource(R.color.gelo);
			   } else if (historico.getTipo().equals("ATIV.FÍSICA")) {
				   t.setBackgroundResource(R.color.amarelo_claro);
			   } 
		   }		   
		   return convertView;
	   }
		
	   public int setCoresLinhas(int dia2, int position) {  //Seta a cor das linhas	      
		 cor = View.GONE;
		 texto = "";
		 if (dia==0) {
			 dia = dia2;			 
		 } else if (dia != dia2) {
		   texto = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+",  "+format_dia.format(c.getTime());
		   cor = View.VISIBLE;
		   dia = dia2;
		 } 
		 atual = position;
		 return cor; 
	   }
}
