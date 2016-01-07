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
import br.com.diabetesvirtual.model.Glicemia;

@SuppressLint("SimpleDateFormat") //Apenas para tirar o warning
public class GlicemiaAdapter extends ArrayAdapter<Glicemia>{

	List<Glicemia> lista;
	int resource;
	int dia=0;
	int cor;
	int atual=-1;
	List<String> cores;
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yy");
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm");
	
		
	public GlicemiaAdapter(Context context, int resource, List<Glicemia> objects) {
		super(context, resource, objects);
		cores = new ArrayList<String>();
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
		public TextView medida;
		public TextView obs;
	}
   
   public View getView(int position, View convertView, ViewGroup parent) {	   
	   ViewHolder holder;	   
	   if (convertView == null) {
		   holder = new ViewHolder();
		   LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   convertView = inflater.inflate(resource, null);	   
		   holder.dia = (TextView) convertView.findViewById(R.id.glicemia_dia);
		   holder.hora = (TextView) convertView.findViewById(R.id.glicemia_hora);
		   holder.medida =  (TextView) convertView.findViewById(R.id.glicemia_medida);
		   holder.obs = (TextView) convertView.findViewById(R.id.glicemia_obs);		   
		   convertView.setTag(holder);  
	   } else {
		   holder = (ViewHolder) convertView.getTag();
	   }  
	   Glicemia glicemia = lista.get(position);	
	   if (glicemia != null) {
		   holder.dia.setText(glicemia.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())+", "+format_dia.format(glicemia.getData().getTime()));
		   holder.hora.setText(format_hora.format(glicemia.getData().getTime()));		   
		   holder.medida.setText(glicemia.getMedida()+" mg/dL");		    
		   holder.obs.setText("Obs: "+glicemia.getObs()+"\n"+"Tipo:"+glicemia.getTipo());  		      
		   if (position > atual) { //Caso a lista esteja descendo 
			   cor = this.setCoresLinhas(glicemia.getData().get(Calendar.DAY_OF_MONTH), position); //Altera a cor da linha
			   atual = position;
			   cores.add(cor+"");
		   } else {
			  cor = Integer.parseInt(cores.get(position)); //Caso a lista esta subindo recupera as cores
		   }
		   LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.glicemia_cor_da_linha);
		   layout.setBackgroundResource(cor);
	   }
	   return convertView;
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


