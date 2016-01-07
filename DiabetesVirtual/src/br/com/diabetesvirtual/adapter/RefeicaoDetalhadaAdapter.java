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
import br.com.diabetesvirtual.model.Alimento;
import br.com.diabetesvirtual.model.ItensRefeicao;
import br.com.diabetesvirtual.model.Refeicao;
import br.com.diabetesvirtual.util.Formatos;

public class RefeicaoDetalhadaAdapter extends ArrayAdapter<ItensRefeicao>{

	List<ItensRefeicao> lista;
	List<Integer> aparece;
	List<String> lista_texto;
	Refeicao refeicao;
	Alimento alimento;
	String texto;
	int resource;
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yy HH:mm",Locale.getDefault());
	LinearLayout t;
	LinearLayout separador;
	TextView separador_dia;
	int atual=-1;
	int estado;
	int id;
	int id2=-1;
	double qtd_medida;
	Context ctx;
	
	public RefeicaoDetalhadaAdapter(Context context, int resource, List<ItensRefeicao> objects) {
		super(context, resource, objects);
		this.lista = objects;
		this.resource = resource;
		aparece = new ArrayList<Integer>();
		lista_texto = new ArrayList<String>();
		this.ctx = context;
	}
	
	public int getCount() {
		return lista.size();  	
	}
	      
	public long getItemId(int position) {
	   return position;
	}   
	
	private static class ViewHolder {
		public TextView desc;
		public TextView medida;
		public TextView medida_qtd;
		public TextView carb_total;
		public TextView carb_total_item;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {	   
		   ViewHolder holder;	
		   if (convertView == null) {
			   holder = new ViewHolder();
			   LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			   convertView = inflater.inflate(resource, null);	   
			   holder.desc = (TextView) convertView.findViewById(R.id.ref_item_desc);
			   holder.medida = (TextView) convertView.findViewById(R.id.ref_item_medida);
			   holder.medida_qtd =  (TextView) convertView.findViewById(R.id.ref_item_medida_qtd);
			   holder.carb_total = (TextView) convertView.findViewById(R.id.ref_item_carb_qtd);	
			   holder.carb_total_item = (TextView) convertView.findViewById(R.id.ref_item_carb_qtd_total);
			   convertView.setTag(holder);  
		   } else {
			   holder = (ViewHolder) convertView.getTag();
		   }  
		   alimento = lista.get(position).getAlimento();
		   refeicao = lista.get(position).getRefeicao();
		   qtd_medida = lista.get(position).getQtd();
		   id = lista.get(position).getRefeicao().getId();
		   if (alimento != null) {
			   holder.desc.setText(alimento.getDescricao());
			   holder.medida.setText(alimento.getMedida());		   
			   holder.medida_qtd.setText(Formatos.formataDouble(qtd_medida));		    
			   holder.carb_total.setText(Formatos.formataDouble(alimento.getCarboidrato())+"g");  
			   holder.carb_total_item.setText(Formatos.formataDouble(qtd_medida*alimento.getCarboidrato())+"g");
			   if (position > atual) { //Caso a lista esteja descendo 
				   estado = this.setEstado(id, position);
				   lista_texto.add(texto);
				   aparece.add(estado);
			   } else {
				  estado = aparece.get(position); //Caso a lista esta subindo recupera as cores
				  texto = lista_texto.get(position);
			   }	
			   separador = (LinearLayout) convertView.findViewById(R.id.ref_item_separador);		   
			   separador_dia = (TextView) convertView.findViewById(R.id.ref_item_sepadaror_text);
			   separador_dia.setText(texto);
			   separador.setVisibility(estado);
		   }		   
		   return convertView;
	   }
	
		public int setEstado(int id, int position) {  //Seta a cor das linhas	      
			 estado = View.GONE;
			 texto = "";
			 if (id != id2) {
			   texto = "Em, "+refeicao.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+
						  ",  "+format_dia.format(refeicao.getData().getTime())+	
						  "\n"+refeicao.getTipo()+" ("+Formatos.formataDouble(refeicao.getCarboidrato())+"g CHO"
					   //"\n"+RefeicaoTipos.getNome(ctx, refeicao.getTipo().getCod())+" ("+Formatos.formataDouble(refeicao.getCarboidrato())+"g CHO"
					   +")";
			   estado = View.VISIBLE;
			   id2 = id;
			 } 
			 atual = position;
			 return estado; 
	   }
}
