package ec.bigdata.facturaelectronicamovil.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 30/7/2016.
 */
public class RecyclerViewHolderAdmProductos extends RecyclerView.ViewHolder {

    public ImageView imageViewInicialProducto;
    public TextView textViewCodigoProducto;
    public TextView textViewDescripcionProducto;
    public TextView textViewPrecioUnitario;

    public RecyclerViewHolderAdmProductos(View itemView) {
        super(itemView);
        imageViewInicialProducto = (ImageView) itemView.findViewById(R.id.image_view_inicial_producto);
        textViewCodigoProducto = (TextView) itemView.findViewById(R.id.text_view_codigo_principal_producto);
        textViewDescripcionProducto = (TextView) itemView.findViewById(R.id.text_view_descripcion_producto);
        textViewPrecioUnitario = (TextView) itemView.findViewById(R.id.text_view_precio_unitario_producto);
    }
}
