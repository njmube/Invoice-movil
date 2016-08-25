package ec.bigdata.facturaelectronicamovil.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 30/7/2016.
 */
public class RecyclerViewHolderAdmClientes extends RecyclerView.ViewHolder {

    public ImageView imageViewInicialCliente;
    public TextView textViewIdentificacionNombreCliente;
    public TextView textViewCorreoElectronicoCliente;
    public TextView textViewEstadoCliente;

    public RecyclerViewHolderAdmClientes(View itemView) {
        super(itemView);
        imageViewInicialCliente = (ImageView) itemView.findViewById(R.id.image_view_inicial_cliente);
        textViewIdentificacionNombreCliente = (TextView) itemView.findViewById(R.id.text_view_identificacion_nombre_cliente);
        textViewCorreoElectronicoCliente = (TextView) itemView.findViewById(R.id.text_view_correo_electronico_cliente);
        textViewEstadoCliente = (TextView) itemView.findViewById(R.id.text_view_estado_cliente);
    }
}
