package ec.bigdata.facturaelectronicamovil.adaptador;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.Secuencial;

/**
 * Created by DavidLeonardo on 4/5/2016.
 */
public class ArrayAdapterSecuencial extends ArrayAdapter {

    public ArrayAdapterSecuencial(Context context, List<Secuencial> objects) {
        super(context, 0, objects);
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
                    R.layout.spinner_establecimiento,
                    parent,
                    false);
        }

        //Obteniendo instancias de los TextView
        TextView titulo = (TextView) listItemView.findViewById(R.id.text_view_info_establecimiento);


        //Obteniendo instancia del Secuencial en la posición actual
        Secuencial item = (Secuencial) getItem(position);

        String estado = item.getEstadoSecuencial().equals(Boolean.TRUE) ? "Activo" : "Inactivo";
        titulo.setText(item.getCodigoEstablecimientoSecuencial() + "-" + item.getPuntoEmisionSecuencial() + "-" + estado);
        titulo.setTextColor(ContextCompat.getColor(this.getContext(), android.R.color.black));

        //Devolver al ListView la fila creada
        return listItemView;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        /*
        Debido a que deseamos usar spinner_item.xml para inflar los
        items del Spinner en ambos casos, entonces llamamos a getView()
         */
        return getView(position, convertView, parent);
    }
}
