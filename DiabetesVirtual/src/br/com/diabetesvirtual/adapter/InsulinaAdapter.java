package br.com.diabetesvirtual.adapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.R.color;
import br.com.diabetesvirtual.model.Insulina;
import br.com.diabetesvirtual.util.Formatos;

@SuppressLint("SimpleDateFormat") //Apenas para tirar o warning
public class InsulinaAdapter extends ArrayAdapter<Insulina>{

	List<Insulina> lista;
	int resource;
	int dia=0;
	int cor;
	int atual=-1;
	List<String> cores;
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yy");
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm");
	Context ctx;
	int cor_rapida = Color.parseColor("#1E90FF");
	int cor_lenta = Color.parseColor("#FF7F00");
	
		
	public InsulinaAdapter(Context context, int resource, List<Insulina> objects) {
		super(context, resource, objects);
		cores = new ArrayList<String>();
		this.ctx = context;
		this.lista = objects;
		this.resource = resource;
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
		public TextView qtd;
		public TextView tipo;
		public TextView obs;
	}
   
   public View getView(int position, View convertView, ViewGroup parent) {	   
	   ViewHolder holder;	   
	   if (convertView == null) {
		   holder = new ViewHolder();
		   LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   convertView = inflater.inflate(resource, null);	   
		   holder.dia = (TextView) convertView.findViewById(R.id.insulina_dia);
		   holder.hora = (TextView) convertView.findViewById(R.id.insulina_hora);
		   holder.tipo =  (TextView) convertView.findViewById(R.id.insulina_tipo);
		   holder.qtd = (TextView) convertView.findViewById(R.id.insulina_qtd);	 
		   holder.obs = (TextView) convertView.findViewById(R.id.insulina_obs);	
		   convertView.setTag(holder);  
	   } else {
		   holder = (ViewHolder) convertView.getTag();
	   }  
	   Insulina insulina = lista.get(position);	
	   if (insulina != null) {
		   holder.dia.setText(insulina.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())+", "+format_dia.format(insulina.getData().getTime()));
		   holder.hora.setText(format_hora.format(insulina.getData().getTime()));		   
		   holder.qtd.setText(Formatos.formataUmaCasa(insulina.getQtd())+" UI");	
		   holder.tipo.setText(insulina.getTipo()); 	
		   holder.obs.setText("Obs.: "+insulina.getObs());
		   if (position > atual) { //Caso a lista esteja descendo 
			   cor = this.setCoresLinhas(insulina.getData().get(Calendar.DAY_OF_MONTH), position); //Altera a cor da linha
			   atual = position;
			   cores.add(cor+"");
		   } else {
			  cor = Integer.parseInt(cores.get(position)); //Caso a lista esta subindo recupera as cores
		   }
		   LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.insulina_cor_da_linha);
		   linearLayout.setBackgroundResource(cor);
		   if (insulina.getTipo().equals("Basal")) {
			   holder.tipo.setTextColor(cor_lenta);
		   } else {
			   holder.tipo.setTextColor(cor_rapida);
		   }
	   }
	   return convertView;
   }
	
   public int setCoresLinhas(int dia2, int position) {  //Seta a cor das linhas	      
	 if(dia == 0) {
		   cor = color.gelo;
		   dia = dia2;
	   } else if (dia != dia2) {
		   this.trocaCor(cor);
		   dia = dia2;
	   } 
	 	atual = position;
	   return cor; 
   }
   
   public void trocaCor(int aux) { //Troca a cor qnd o dia eh mudado
	   if (aux == color.gelo) {
		   cor = color.verde_claro;
	}	else {
		cor = color.gelo;
	}
   }
}
