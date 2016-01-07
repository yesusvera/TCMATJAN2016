package br.com.diabetesvirtual.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.model.ItensRefeicao;
import br.com.diabetesvirtual.util.Formatos;

public class ItensRefeicaoAdapter extends ArrayAdapter<ItensRefeicao>{
	
	int resource;
	List<ItensRefeicao> lista;
	
	public ItensRefeicaoAdapter(Context context, int resource, List<ItensRefeicao> objects) {
		super(context, resource);
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
		public TextView descricao;
		public TextView medida;
		public TextView carb;
		public TextView qtd;
	}
	   
	public View getView(int position, View convertView, ViewGroup parent) {	   
		ViewHolder holder;	   
		if (convertView == null) {
		   holder = new ViewHolder();
		   LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   convertView = inflater.inflate(resource, null);	   
		   holder.descricao = (TextView) convertView.findViewById(R.id.item_descricao);
		   holder.medida = (TextView) convertView.findViewById(R.id.item_medida);
		   holder.carb =  (TextView) convertView.findViewById(R.id.item_carb);
		   holder.qtd = (TextView) convertView.findViewById(R.id.item_qtd);
		   convertView.setTag(holder);  
		} else {
		   holder = (ViewHolder) convertView.getTag();
		}  
		ItensRefeicao item = lista.get(position);	
		if (item != null) {
		   holder.descricao.setText(item.getAlimento().getDescricao());
		   holder.medida.setText(item.getAlimento().getMedida());
		   holder.carb.setText(Formatos.formataDouble(item.getAlimento().getCarboidrato())+"g");	
		   holder.qtd.setText(Formatos.formataDouble(item.getQtd())); 		      
		}
		return convertView;
	}

}
