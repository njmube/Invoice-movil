package ec.bigdata.facturaelectronicamovil.adaptador;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico;

/**
 * Created by DavidLeonardo on 8/7/2016.
 */
public class RecyclerViewAdapterRepositorioComprobanteElectronico extends RecyclerView.Adapter<RecyclerViewAdapterRepositorioComprobanteElectronico.RecyclerViewAdapterRepositorioComprobanteElectronicoViewHolder> {
    private List<ComprobanteElectronico> items;

    public static class RecyclerViewAdapterRepositorioComprobanteElectronicoViewHolder extends RecyclerView.ViewHolder {
        // Campos de la lista
        private ImageView imageViewIdentificadorComprobante;
        public TextView textViewNumeroComprobante;
        public TextView textViewNumeroAutorizacion;
        public TextView textViewFechaAutorizacion;

        public RecyclerViewAdapterRepositorioComprobanteElectronicoViewHolder(View v) {
            super(v);
            imageViewIdentificadorComprobante = (ImageView) v.findViewById(R.id.image_view_identificador_comprobante);
            textViewNumeroComprobante = (TextView) v.findViewById(R.id.text_view_numero_comprobante);
            textViewNumeroAutorizacion = (TextView) v.findViewById(R.id.text_view_numero_autorizacion);
            textViewFechaAutorizacion = (TextView) v.findViewById(R.id.text_view_fecha_autorizacion);
        }
    }

    public RecyclerViewAdapterRepositorioComprobanteElectronico(List<ComprobanteElectronico> items) {
        this.items = items;
    }

    /*
    Añade una lista completa de items
     */
    public void addAll(List<ComprobanteElectronico> lista) {
        items.addAll(lista);
        notifyDataSetChanged();
    }

    public void addItems(List<ComprobanteElectronico> lista) {
        items.addAll(lista);
        notifyDataSetChanged();
    }

    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerViewAdapterRepositorioComprobanteElectronicoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.estilo_recycler_view_comprobantes_emitidos, viewGroup, false);
        return new RecyclerViewAdapterRepositorioComprobanteElectronicoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterRepositorioComprobanteElectronicoViewHolder viewHolder, int i) {

        String identificador = "";
        int color = 0;

        if (items.get(i).getTipoComprobanteElectronico().equals("01")) {
            identificador = "F";
            color = Color.BLUE;
        } else if (items.get(i).getTipoComprobanteElectronico().equals("04")) {
            identificador = "NC";
            color = Color.GREEN;
        } else if (items.get(i).getTipoComprobanteElectronico().equals("05")) {
            identificador = "ND";
            color = Color.YELLOW;
        } else if (items.get(i).getTipoComprobanteElectronico().equals("06")) {
            identificador = "GR";
            color = Color.MAGENTA;
        } else if (items.get(i).getTipoComprobanteElectronico().equals("07")) {
            identificador = "CR";
            color = Color.RED;
        }
        TextDrawable textDrawable = TextDrawable.builder().beginConfig().bold().endConfig()
                .buildRound(identificador, color);
        viewHolder.imageViewIdentificadorComprobante.setImageDrawable(textDrawable);
        viewHolder.textViewNumeroComprobante.setText(items.get(i).getCodigoEstablecimientoComprobanteElectronico().concat("-").concat(items.get(i).getPuntoEmisionComprobanteElectronico().concat("-").concat(items.get(i).getSecuencialComprobanteElectronico())));
        viewHolder.textViewNumeroAutorizacion.setText(items.get(i).getNumeroAutorizacionComprobanteElectronico());
        viewHolder.textViewFechaAutorizacion.setText(items.get(i).getFechaAutorizacionComprobanteElectronico().toString());
    }


}
