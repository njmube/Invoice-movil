package ec.bigdata.facturaelectronicamovil.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.comprobanteelectronico.esquema.factura.Detalle;
import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 4/6/2016.
 */
public class ExpandableDetalleAdapter extends BaseExpandableListAdapter {

    private List<Detalle> detalles;
    private int itemLayoutId;
    private int groupLayoutId;
    private Context ctx;

    public ExpandableDetalleAdapter(List<Detalle> detalles, Context ctx) {

        this.itemLayoutId = R.layout.list_view_group_detalle;
        this.groupLayoutId = R.layout.list_view_item_detalle;
        this.detalles = detalles;
        this.ctx = ctx;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return detalles.get(groupPosition).getImpuestos().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_view_item_detalle, parent, false);
        }

        TextView itemCodigoPorcentaje = (TextView) v.findViewById(R.id.text_view_codigo_porcentaje);
        TextView itemTarifa = (TextView) v.findViewById(R.id.text_view_tarifa);
        TextView itemValor = (TextView) v.findViewById(R.id.text_view_valor);

        ImpuestoComprobanteElectronico det = detalles.get(groupPosition).getImpuestos().get(childPosition);

        itemCodigoPorcentaje.setText(det.getCodigoPorcentaje());
        itemTarifa.setText(det.getTarifa());
        itemValor.setText(det.getValor());
//TODO Poner cabecera en hijos
        return v;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = detalles.get(groupPosition).getImpuestos().size();

        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return detalles.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return detalles.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_view_group_detalle, parent, false);
        }


        //Obteniendo instancias de los text views
        TextView codigoPrincipal = (TextView) v.findViewById(R.id.text_view_codigo_producto);

        TextView descripcion = (TextView) v.findViewById(R.id.text_view_descripcion);

        TextView cantidad = (TextView) v.findViewById(R.id.text_view_cantidad);

        TextView precioUnitario = (TextView) v.findViewById(R.id.text_view_precio_unitario);

        //Obteniendo instancia del Detalle en la posición actual
        Detalle item = detalles.get(groupPosition);

        codigoPrincipal.setText(item.getCodigoPrincipal());

        descripcion.setText(item.getDescripcion());

        cantidad.setText(item.getCantidad());

        precioUnitario.setText(item.getPrecioUnitario());

        return v;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

/*
    public ExpandableDetalleAdapter(Context context, List<Detalle> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            listItemView = inflater.inflate(
                    R.layout.list_view_group_detalle,
                    parent,
                    false);
        }

        //Obteniendo instancias de los text views
        TextView codigoPrincipal = (TextView)listItemView.findViewById(R.id.text_view_codigo_producto);

        TextView descripcion = (TextView)listItemView.findViewById(R.id.text_view_descripcion);

        TextView cantidad = (TextView)listItemView.findViewById(R.id.text_view_cantidad);

        TextView precioUnitario = (TextView)listItemView.findViewById(R.id.text_view_precio_unitario);

        //Obteniendo instancia del Detalle en la posición actual
        Detalle item = (Detalle)getItem(position);

        codigoPrincipal.setText(item.getCodigoPrincipal());

        descripcion.setText(item.getDescripcion());

        cantidad.setText(item.getCantidad());

        precioUnitario.setText(item.getPrecioUnitario());

        //Devolver al ListView la fila creada
        return listItemView;

    }*/
}

