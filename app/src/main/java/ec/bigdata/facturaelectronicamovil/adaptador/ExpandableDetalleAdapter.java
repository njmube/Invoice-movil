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
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;
       /* if (childPosition == 0) {
            v = inflater.inflate(R.layout.header_hijos_expandable_list_view_detalles, null);
        } else {*/

        if (v == null) {

            v = inflater.inflate(R.layout.list_view_item_detalle, parent, false);
        }

        TextView itemCodigo = (TextView) v.findViewById(R.id.text_view_item_codigo_impuesto);
        TextView itemCodigoPorcentaje = (TextView) v.findViewById(R.id.text_view_codigo_item_porcentaje_impuesto);
        TextView itemBaseImponible = (TextView) v.findViewById(R.id.text_view_item_base_imponible_impuesto);
        TextView itemTarifa = (TextView) v.findViewById(R.id.text_view_item_tarifa_impuesto);
        TextView itemValor = (TextView) v.findViewById(R.id.text_view_item_valor_impuesto);

        ImpuestoComprobanteElectronico det = detalles.get(groupPosition).getImpuestos().get(childPosition);

        itemCodigo.setText(det.getCodigo());
        itemCodigoPorcentaje.setText(det.getCodigoPorcentaje());
        itemBaseImponible.setText(det.getBaseImponible());
        itemTarifa.setText(det.getTarifa());
        itemValor.setText(det.getValor());
        //  }
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

}

