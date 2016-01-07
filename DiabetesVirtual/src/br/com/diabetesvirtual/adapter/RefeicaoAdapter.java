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
import br.com.diabetesvirtual.R.color;
import br.com.diabetesvirtual.model.Refeicao;
import br.com.diabetesvirtual.util.Formatos;

public class RefeicaoAdapter extends ArrayAdapter<Refeicao>{
	
	List<Refeicao> lista;
	int resource;
	int dia=0;
	int cor;
	int atual=-1;
	Context context;
	List<String> cores;
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yy",Locale.getDefault());
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm",Locale.getDefault());
	
	public RefeicaoAdapter(Context context, int resource, List<Refeicao> objects) {
		super(context, resource, objects);
		cores = new ArrayList<String>();
		this.lista = objects;
		this.resource = resource;
		this.context = context;
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
		public TextView carb;
		public TextView tipo;
		public TextView obs;
	}
   
   public View getView(int position, View convertView, ViewGroup parent) {	   
	   ViewHolder holder;	   
	   if (convertView == null) {
		   holder = new ViewHolder();
		   LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   convertView = inflater.inflate(resource, null);	   
		   holder.dia = (TextView) convertView.findViewById(R.id.refeicao_dia);
		   holder.hora = (TextView) convertView.findViewById(R.id.refeicao_hora);
		   holder.carb =  (TextView) convertView.findViewById(R.id.refeicao_carb);
		   holder.tipo = (TextView) convertView.findViewById(R.id.refeicao_tipo);
		   holder.obs = (TextView) convertView.findViewById(R.id.ref_obs);
		   convertView.setTag(holder);  
	   } else {
		   holder = (ViewHolder) convertView.getTag();
	   }  
	   Refeicao refeicao = lista.get(position);	
	   if (refeicao != null) {
		   holder.dia.setText(refeicao.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())+", "+format_dia.format(refeicao.getData().getTime()));
		   holder.hora.setText(format_hora.format(refeicao.getData().getTime()));
		   holder.carb.setText(Formatos.formataDouble(refeicao.getCarboidrato())+"g");	
		   holder.tipo.setText(refeicao.getTipo()); 	
//		   holder.tipo.setText(RefeicaoTipos.getNome(context, refeicao.getTipo().getCod())); 	
		   holder.obs.setText("Obs.: "+refeicao.getObs()); 
		   if (position > atual) { //Caso a lista esteja descendo 
			   cor = this.setCoresLinhas(refeicao.getData().get(Calendar.DAY_OF_MONTH), position); //Altera a cor da linha
			   atual = position;
			   cores.add(cor+"");
		   } else {
			  cor = Integer.parseInt(cores.get(position)); //Caso a lista esta subindo recupera as cores
		   }
		   LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.ref_cor_da_linha);
		   linearLayout.setBackgroundResource(cor);
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
   