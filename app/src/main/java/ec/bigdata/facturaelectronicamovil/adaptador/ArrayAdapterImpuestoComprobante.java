package ec.bigdata.facturaelectronicamovil.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 4/5/2016.
 */
public class ArrayAdapterImpuestoComprobante<T> extends ArrayAdapter<ImpuestoComprobanteElectronico> {

    public ArrayAdapterImpuestoComprobante(Context context, List<ImpuestoComprobanteElectronico> objects) {
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
                    R.layout.estilo_list_view_impuesto,
                    parent,
                    false);
        }

        //Obteniendo instancias de los TextView
        TextView codigo = (TextView) listItemView.findViewById(R.id.text_view_codigo_impuesto);

        TextView codigoPorcentaje = (TextView) listItemView.findViewById(R.id.text_view_codigo_porcentaje_impuesto);

        TextView baseImponible = (TextView) listItemView.findViewById(R.id.text_view_base_imponible_impuesto);

        TextView tarifa = (TextView) listItemView.findViewById(R.id.text_view_tarifa_impuesto);

        TextView valor = (TextView) listItemView.findViewById(R.id.text_view_valor_impuesto);

        //Obteniendo instancia del ImpuestoComprobanteElectronico en la posición actual
        ImpuestoComprobanteElectronico item = getItem(position);

        codigo.setText(item.getCodigo());
        codigoPorcentaje.setText(item.getCodigoPorcentaje());
        baseImponible.setText(item.getBaseImponible());
        tarifa.setText(item.getTarifa());
        valor.setText(item.getValor());

        //Devolver al ListView la fila creada
        return listItemView;

    }

}
