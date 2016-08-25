package ec.bigdata.facturaelectronicamovil.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 30/7/2016.
 */
public class RecyclerViewHolderComprobantesEmitidos extends RecyclerView.ViewHolder {

    public ImageView imageViewIdentificadorComprobante;
    public TextView textViewNumeroComprobante;
    public TextView textViewReceptor;
    public TextView textViewFechaAutorizacion;


    public RecyclerViewHolderComprobantesEmitidos(View itemView) {
        super(itemView);
        imageViewIdentificadorComprobante = (ImageView) itemView.findViewById(R.id.image_view_identificador_comprobante);
        textViewNumeroComprobante = (TextView) itemView.findViewById(R.id.text_view_numero_comprobante);
        textViewReceptor = (TextView) itemView.findViewById(R.id.text_view_receptor);
        textViewFechaAutorizacion = (TextView) itemView.findViewById(R.id.text_view_fecha_autorizacion);
    }
}
