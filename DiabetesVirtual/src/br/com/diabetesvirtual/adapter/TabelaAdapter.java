package br.com.diabetesvirtual.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.model.Alimento;

@SuppressLint("DefaultLocale")
public class TabelaAdapter extends ArrayAdapter<Alimento> {
	
	private List<Alimento> lista;
	private List<Alimento> original;
	private Filter filter;
	
	public TabelaAdapter(Context context, int resource, List<Alimento> objects) {
		super(context, resource, objects);
		this.lista = objects;
		this.original = objects;
		this.filter = new AlimentoFilter();
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
			view = inflater.inflate(R.layout.tabela_linha, null);
	   }	   
	   Alimento alimento = lista.get(position);	   
	   if (alimento != null) {		  
		   TextView descricao = (TextView) view.findViewById(R.id.descricao);
		   descricao.setText(alimento.getDescricao());
		   
		   TextView medida = (TextView) view.findViewById(R.id.medida);
		   medida.setText(alimento.getMedida());
		   
		   TextView peso = (TextView) view.findViewById(R.id.peso);
		   peso.setText(alimento.getPeso()+"g");
		   
		   TextView carboidrato = (TextView) view.findViewById(R.id.carboidrato);
		   carboidrato.setText(alimento.getCarboidrato()+"g");
	   }
	   return view;	   
   }


   @Override
   public Filter getFilter() {
	   if (filter == null) {
		   filter = new AlimentoFilter();
	   }	   
   return filter;
   }
   
	public Alimento getItemFilter(int position) {
		return lista.get(position);
	}
   
   private class AlimentoFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			String prefix = constraint.toString().toLowerCase();
			if (prefix == null || prefix.length() == 0) {
				results.values = new ArrayList<Alimento>(original);
				results.count = new ArrayList<Alimento>(original).size();
			} else {
				final ArrayList<Alimento> list = new ArrayList<Alimento>(original);
				final ArrayList<Alimento> nlist = new ArrayList<Alimento>();
				int count = list.size();
				for (int i = 0; i < count; i++) {
					final Alimento alimento = list.get(i);
					final String value = alimento.getDescricao().toLowerCase();
					if (value.contains(prefix)) {
						nlist.add(alimento);
					} 					 	
				}
				results.values = nlist;
				results.count = nlist.size();
			}			
			return results;
		}
	
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			if (results.count == 0) {
				notifyDataSetInvalidated();
			} else {
				lista = new ArrayList<Alimento>();
				lista = (ArrayList<Alimento>) results.values;
				notifyDataSetChanged();
			}
		}   
   }
}
