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
import ec.bigdata.facturaelectronicamovil.modelo.Producto;

/**
 * Created by DavidLeonardo on 8/7/2016.
 */
public class RecyclerViewAdapterAdmProductos extends RecyclerView.Adapter<RecyclerViewHolderAdmProductos> {

    private List<Producto> productos;

    private SparseBooleanArray sparseBooleanArrayIds;


    public RecyclerViewAdapterAdmProductos(List<Producto> productos, Context context) {

        this.productos = productos;
        sparseBooleanArrayIds = new SparseBooleanArray();
    }
    /*
    Añade una lista completa de clientes
     */

    public void addItems(List<Producto> lista) {
        productos.addAll(lista);
        notifyDataSetChanged();
    }

    /**
     * Actualiza la lista de producto, con el objeto Producto y la posición.
     */
    public void actualizarProducto(Producto producto, int posicion) {
        productos.set(posicion, producto);
        notifyDataSetChanged();
    }

    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear() {
        productos.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != productos ? productos.size() : 0);
    }

    @Override
    public RecyclerViewHolderAdmProductos onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.estilo_recycler_view_adm_productos, viewGroup, false);

        return new RecyclerViewHolderAdmProductos(v);
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
    public void onBindViewHolder(RecyclerViewHolderAdmProductos viewHolder, int i) {
        String descripcion = "";
        String inicial = "";
        descripcion = productos.get(i).getDescripcionProducto();
        inicial = String.valueOf(descripcion.charAt(0));
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

        TextDrawable textDrawable = TextDrawable.builder().beginConfig().bold().endConfig()
                .buildRound(inicial, colorGenerator.getColor(descripcion));
        viewHolder.imageViewInicialProducto.setImageDrawable(textDrawable);
        viewHolder.textViewCodigoProducto.setText(productos.get(i).getCodigoPrincipalProducto());
        viewHolder.textViewDescripcionProducto.setText(descripcion);
        viewHolder.textViewPrecioUnitario.setText(productos.get(i).getPrecioUnitarioProducto());

        //Cambia el color de fondo del item seleccionado
        viewHolder.itemView
                .setBackgroundColor(sparseBooleanArrayIds.get(i) ? 0x9934B5E4
                        : Color.TRANSPARENT);

    }

}
