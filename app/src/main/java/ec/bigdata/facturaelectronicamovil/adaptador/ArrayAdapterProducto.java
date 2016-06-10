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
import ec.bigdata.facturaelectronicamovil.modelo.Producto;

/**
 * Created by DavidLeonardo on 6/5/2016.
 */
public class ArrayAdapterProducto extends ArrayAdapter<Producto> {

    Context context;
    int resource, textViewResourceId;
    List<Producto> items, tempItems, suggestions;

    public ArrayAdapterProducto(Context context, int resource, int textViewResourceId, List<Producto> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Producto>(items); // this makes the difference.
        suggestions = new ArrayList<Producto>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fila_auto_complete, parent, false);
        }
        Producto producto = items.get(position);
        if (producto != null) {
            TextView lblName = (TextView) view.findViewById(R.id.tv_cliente_filtrado);
            if (lblName != null) {
                lblName.setText(producto.getCodigoPrincipalProducto() + "-" + producto.getDescripcionProducto());
                lblName.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Producto) resultValue).getDescripcionProducto();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Producto producto : tempItems) {
                    if (producto.getCodigoPrincipalProducto().contains(constraint.toString()) ||
                            producto.getDescripcionProducto().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        suggestions.add(producto);
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
            List<Producto> filterList = (ArrayList<Producto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Producto producto : filterList) {
                    add(producto);
                    notifyDataSetChanged();
                }
            }
        }
    };
}