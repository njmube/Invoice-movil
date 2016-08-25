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

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 15/6/2016.
 */
public class ArrayAdapterInformacionAdicional<T> extends ArrayAdapter<InformacionAdicional> {
    private HashMap<Integer, Boolean> selecccionados;

    public ArrayAdapterInformacionAdicional(Context context, List<InformacionAdicional> objects) {
        super(context, 0, objects);
        selecccionados = new HashMap<Integer, Boolean>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            listItemView = inflater.inflate(
                    R.layout.list_view_informacion_adicional,
                    parent,
                    false);
        }

        //Obteniendo instancias de los text views
        TextView nombre = (TextView) listItemView.findViewById(R.id.text_view_nombre_informacion_adicional);

        TextView valor = (TextView) listItemView.findViewById(R.id.text_view_valor_informacion_adicional);


        //Obteniendo instancia del InformacionAdicional en la posición actual
        InformacionAdicional item = getItem(position);

        nombre.setText(item.getNombre());
        valor.setText(item.getValor());

        //Color cuando se seleccione un item
        listItemView.setBackgroundColor(selecccionados.get(position) != null ? 0x9934B5E4
                : Color.TRANSPARENT);
        //Devolver al ListView la fila creada
        return listItemView;

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

}
