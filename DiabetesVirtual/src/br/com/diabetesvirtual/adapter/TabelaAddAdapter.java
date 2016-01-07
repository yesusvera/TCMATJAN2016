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



public class TabelaAddAdapter extends ArrayAdapter<ItensRefeicao>{

	private List<ItensRefeicao> lista;	
	
	public TabelaAddAdapter(Context context, int resource, List<ItensRefeicao> objects) {
		super(context, resource, objects);
		this.lista = objects;
	}

   public int getCount() {
	 return lista.size();  	
   }
      
   public long getItemId(int position) {
	return position;
   }   
   
   public View getView(int position, View convertView, ViewGroup parent) {   
	   View view = convertView;
	   if (view == null) {
		   LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tabela_add_linha, null);
	   }	   
	   ItensRefeicao item = lista.get(position);	   
	   if (item != null) {		  
		   TextView descricao = (TextView) view.findViewById(R.id.descricao);
		   descricao.setText(item.getAlimento().getDescricao());
		   
		   TextView medida = (TextView) view.findViewById(R.id.medida);
		   medida.setText(item.getAlimento().getMedida());
		 
		   
		   TextView total_carb = (TextView) view.findViewById(R.id.total_carboidrato);
		   double x = item.getQtd()*item.getAlimento().getCarboidrato();	   
		   total_carb.setText(Formatos.formataDouble(x)+"g");
	   }
	   return view;	   
   }
   

}

