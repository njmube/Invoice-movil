package ec.bigdata.facturaelectronicamovil.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico;

/**
 * Created by DavidLeonardo on 8/7/2016.
 */
public class RecyclerViewAdapterComprobantesEmitidos extends RecyclerView.Adapter<RecyclerViewHolderComprobantesEmitidos> {

    private List<ComprobanteElectronico> comprobanteElectronicos;

    private SparseBooleanArray sparseBooleanArrayIds;


    public RecyclerViewAdapterComprobantesEmitidos(List<ComprobanteElectronico> comprobanteElectronicos, Context context) {

        this.comprobanteElectronicos = comprobanteElectronicos;
        sparseBooleanArrayIds = new SparseBooleanArray();
    }
    /*
    Añade una lista completa de clientes
     */

    public void addItems(List<ComprobanteElectronico> lista) {
        comprobanteElectronicos.addAll(lista);
        notifyDataSetChanged();
    }

    /**
     * Actualiza la lista de ComprobanteElectronico, con el objeto ComprobanteElectronico y la posición.
     */
    public void actualizarComprobanteElectronico(ComprobanteElectronico comprobanteElectronico, int posicion) {
        comprobanteElectronicos.set(posicion, comprobanteElectronico);
        notifyDataSetChanged();
    }

    /*
    Permite limpiar todos los elementos del recyclerview
     */
    public void clear() {
        comprobanteElectronicos.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != comprobanteElectronicos ? comprobanteElectronicos.size() : 0);
    }

    @Override
    public RecyclerViewHolderComprobantesEmitidos onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.estilo_recycler_view_comprobantes_emitidos, viewGroup, false);

        return new RecyclerViewHolderComprobantesEmitidos(v);
    }

    //Remueve los items seleccionados
    public void removerSeleccion() {
        sparseBooleanArrayIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /***
     * Método para hacer selecciones, remover,etc.
     */

    //Alternar selección
    public void toggleSelection(int position) {
        seleccionarVista(position, !sparseBooleanArrayIds.get(position));
    }
//Pone o borra la posición seleccionada dentro SparseBooleanArray

    public void seleccionarVista(int position, boolean value) {
        if (value)
            sparseBooleanArrayIds.put(position, value);
        else
            sparseBooleanArrayIds.delete(position);

        notifyDataSetChanged();
    }

    //Obtiene el total de contador seleccionado
    public int obtenerContadorSeleccionado() {
        return sparseBooleanArrayIds.size();
    }


    //Retorna todos los ids seleccionados
    public SparseBooleanArray obtenerSparseBooleanArray() {
        return sparseBooleanArrayIds;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolderComprobantesEmitidos viewHolder, int i) {

        String identificador = "";
        if (comprobanteElectronicos.get(i).getTipoComprobanteElectronico().equals("01")) {
            identificador = "F";

        } else if (comprobanteElectronicos.get(i).getTipoComprobanteElectronico().equals("04")) {
            identificador = "NC";

        } else if (comprobanteElectronicos.get(i).getTipoComprobanteElectronico().equals("05")) {
            identificador = "ND";

        } else if (comprobanteElectronicos.get(i).getTipoComprobanteElectronico().equals("06")) {
            identificador = "GR";

        } else if (comprobanteElectronicos.get(i).getTipoComprobanteElectronico().equals("07")) {
            identificador = "CR";

        }

        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

        TextDrawable textDrawable = TextDrawable.builder().beginConfig().bold().endConfig()
                .buildRound(identificador, colorGenerator.getColor(identificador));
        viewHolder.imageViewIdentificadorComprobante.setImageDrawable(textDrawable);
        viewHolder.textViewFechaAutorizacion.setText(comprobanteElectronicos.get(i).getFechaAutorizacionComprobanteElectronico());
        viewHolder.textViewNumeroComprobante.setText(comprobanteElectronicos.get(i).getCodigoEstablecimientoComprobanteElectronico() + comprobanteElectronicos.get(i).getPuntoEmisionComprobanteElectronico() + comprobanteElectronicos.get(i).getSecuencialComprobanteElectronico());
        viewHolder.textViewReceptor.setText(comprobanteElectronicos.get(i).getRazonSocialReceptor());

        //Cambia el color de fondo del item seleccionado
        viewHolder.itemView
                .setBackgroundColor(sparseBooleanArrayIds.get(i) ? 0x9934B5E4
                        : Color.TRANSPARENT);

    }

}
