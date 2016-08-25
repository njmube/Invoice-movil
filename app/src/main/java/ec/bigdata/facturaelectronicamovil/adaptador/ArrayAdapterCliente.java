package ec.bigdata.facturaelectronicamovil.adaptador;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.Cliente;

/**
 * Created by DavidLeonardo on 6/5/2016.
 */
public class ArrayAdapterCliente extends ArrayAdapter<Cliente> {

    Context context;
    int resource, textViewResourceId;
    List<Cliente> items, tempItems, suggestions;

    public ArrayAdapterCliente(Context context, int resource, int textViewResourceId, List<Cliente> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Cliente>(items); // this makes the difference.
        suggestions = new ArrayList<Cliente>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fila_auto_complete, parent, false);
        }
        Cliente cliente = items.get(position);
        if (cliente != null) {
            TextView textViewCliente = (TextView) view.findViewById(R.id.text_view_filtrado);
            if (textViewCliente != null) {
                textViewCliente.setText(cliente.getIdentificacionCliente() + "-" + cliente.getRazonSocialCliente());
                textViewCliente.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }


    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Cliente) resultValue).getRazonSocialCliente();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Cliente clientes : tempItems) {
                    if (clientes.getIdentificacionCliente().contains(constraint.toString()) ||
                            clientes.getRazonSocialCliente().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        suggestions.add(clientes);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Cliente> filterList = (ArrayList<Cliente>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Cliente clientes : filterList) {
                    add(clientes);
                    notifyDataSetChanged();
                }
            }
        }
    };
}