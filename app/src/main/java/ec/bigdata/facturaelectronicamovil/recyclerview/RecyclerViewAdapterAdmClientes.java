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
import ec.bigdata.facturaelectronicamovil.modelo.Cliente;

/**
 * Created by DavidLeonardo on 8/7/2016.
 */
public class RecyclerViewAdapterAdmClientes extends RecyclerView.Adapter<RecyclerViewHolderAdmClientes> {

    private List<Cliente> clientes;

    private SparseBooleanArray sparseBooleanArrayIds;


    public RecyclerViewAdapterAdmClientes(List<Cliente> clientes, Context context) {

        this.clientes = clientes;
        sparseBooleanArrayIds = new SparseBooleanArray();
    }
    /*
    Añade una lista completa de clientes
     */

    public void addItems(List<Cliente> lista) {
        clientes.addAll(lista);
        notifyDataSetChanged();
    }

    /**
     * Actualiza la lista de clientes, con el objeto Cliente y la posición
     */
    public void actualizarCliente(Cliente cliente, int posicion) {
        clientes.set(posicion, cliente);
        notifyDataSetChanged();
    }

    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear() {
        clientes.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != clientes ? clientes.size() : 0);
    }

    @Override
    public RecyclerViewHolderAdmClientes onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.estilo_recycler_view_adm_clientes, viewGroup, false);

        return new RecyclerViewHolderAdmClientes(v);
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
    public void onBindViewHolder(RecyclerViewHolderAdmClientes viewHolder, int i) {
        String nombre = "";
        String inicial = "";
        nombre = ec.bigdata.facturaelectronicamovil.utilidad.Utilidades.obtenerNombreCliente(clientes.get(i));
        inicial = String.valueOf(nombre.charAt(0));
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

        TextDrawable textDrawable = TextDrawable.builder().beginConfig().bold().endConfig()
                .buildRound(inicial, colorGenerator.getColor(nombre));
        viewHolder.imageViewInicialCliente.setImageDrawable(textDrawable);
        viewHolder.textViewIdentificacionNombreCliente.setText(clientes.get(i).getIdentificacionCliente() + " " + nombre);
        viewHolder.textViewCorreoElectronicoCliente.setText(clientes.get(i).getCorreoElectronicoCliente());
        viewHolder.textViewEstadoCliente.setText(clientes.get(i).isEstadoCliente() == true ? "ACTIVO" : "INACTIVO");

        //Cambia el color de fondo del item seleccionado
        viewHolder.itemView
                .setBackgroundColor(sparseBooleanArrayIds.get(i) ? 0x9934B5E4
                        : Color.TRANSPARENT);

    }

}
