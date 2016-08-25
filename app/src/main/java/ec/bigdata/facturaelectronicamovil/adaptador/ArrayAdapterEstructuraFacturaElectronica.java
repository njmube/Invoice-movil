package ec.bigdata.facturaelectronicamovil.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.MenuEstructuraFacturaElectronica;

/**
 * Created by DavidLeonardo on 4/5/2016.
 */
public class ArrayAdapterEstructuraFacturaElectronica<T> extends ArrayAdapter<MenuEstructuraFacturaElectronica> {
    public ArrayAdapterEstructuraFacturaElectronica(Context context, List<MenuEstructuraFacturaElectronica> objects) {
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
                    R.layout.list_view_componentes_factura,
                    parent,
                    false);
        }

        //Obteniendo instancias de los text views
        TextView titulo = (TextView) listItemView.findViewById(R.id.text_view_titulo_componente_factura);

        //Obteniendo la imagen
        ImageView imagen = (ImageView) listItemView.findViewById(R.id.image_view_imagen_componentes_factura);

        //Obteniendo instancia del MenuEstructuraFacturacionElectronica en la posición actual
        MenuEstructuraFacturaElectronica item = getItem(position);

        titulo.setText(item.getTitulo());

        imagen.setImageResource(item.getValidacion());

        //Devolver al ListView la fila creada
        return listItemView;

    }

}
