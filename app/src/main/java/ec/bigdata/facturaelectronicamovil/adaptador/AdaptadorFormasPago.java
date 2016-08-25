package ec.bigdata.facturaelectronicamovil.adaptador;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ec.bigdata.comprobanteelectronico.esquema.factura.Pagos;
import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 2/8/2016.
 */
public class AdaptadorFormasPago<T> extends ArrayAdapter<Pagos> {

    private HashMap<Integer, Boolean> selecccionados;


    public AdaptadorFormasPago(Context context, List<Pagos> pagosList) {
        super(context, 0, pagosList);

        selecccionados = new HashMap<Integer, Boolean>();
    }

    public void setNuevaSeleccion(int position, boolean value) {
        selecccionados.put(position, value);
        notifyDataSetChanged();
    }

    public boolean estaPosicionActualSeleccionada(int position) {
        Boolean result = selecccionados.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> obtenerPosicionActualSeleccionada() {
        return selecccionados.keySet();
    }

    public void quitarSeleccion(int position) {
        selecccionados.remove(position);
        notifyDataSetChanged();
    }

    public void limpiarSeleccion() {
        selecccionados = new HashMap<>();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe.
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            listItemView = inflater.inflate(
                    R.layout.estilo_list_view_formas_pago,
                    parent,
                    false);
        }
        //Obteniendo instancias de los TextView
        TextView formaPago = (TextView) listItemView.findViewById(R.id.text_view_forma_pago);

        TextView totalFormaPago = (TextView) listItemView.findViewById(R.id.text_view_total_forma_pago);

        TextView plazoFormaPago = (TextView) listItemView.findViewById(R.id.text_view_plazo_forma_pago);

        TextView unidadTiempo = (TextView) listItemView.findViewById(R.id.text_view_unidad_tiempo_forma_pago);

        //Obteniendo instancia del Pago en la posición actual
        Pagos item = getItem(position);
        formaPago.setText(item.getFormaPago());
        totalFormaPago.setText(item.getTotal());
        plazoFormaPago.setText(item.getPlazo());
        unidadTiempo.setText(item.getUnidadTiempo());

        //Color cuando se seleccione un item
        listItemView.setBackgroundColor(selecccionados.get(position) != null ? 0x9934B5E4
                : Color.TRANSPARENT);

        return listItemView;
    }
}
