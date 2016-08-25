package ec.bigdata.facturaelectronicamovil.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.interfaz.AdmClientesComunicacionFragmentInterface;
import ec.bigdata.facturaelectronicamovil.interfaz.RecyclerViewClickInterface;
import ec.bigdata.facturaelectronicamovil.modelo.Producto;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestProducto;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DavidLeonardo on 30/7/2016.
 */
public class AdministracionProductosFragmento extends Fragment implements AdmClientesComunicacionFragmentInterface {

    private RecyclerView recyclerViewAdmProductos;

    private RecyclerViewAdapterAdmProductos recyclerViewAdapterAdmProductos;

    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private SearchView searchViewFiltroBusquedaProducto;

    private List<Producto> productosTotales;

    private List<Producto> productosFiltro;

    private ActionMode actionMode;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestProducto.ServicioProducto servicioProducto;

    private int VALOR_MINIMO_CONSULTA_PRODUCTOS;

    private int VALOR_MAXIMO;

    private String filtro;

    private AdministracionProductosActividad administracionProductosActividad;

    private List<Integer> posiciones;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_adm_productos_fragment, container, false);

        searchViewFiltroBusquedaProducto = (SearchView) view.findViewById(R.id.search_view_filtro_producto);
        searchViewFiltroBusquedaProducto.setOnQueryTextListener(onQueryTextListener);
        filtro = "";

        productosTotales = new ArrayList<>();
        productosFiltro = new ArrayList<>();
        claseGlobalUsuario = (ClaseGlobalUsuario) getActivity().getApplicationContext();
        servicioProducto = ClienteRestProducto.getServicioProducto();
        recyclerViewAdmProductos = (RecyclerView) view.findViewById(R.id.recycler_view_adm_productos);
        recyclerViewAdmProductos.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAdmProductos.setLayoutManager(layoutManager);
        recyclerViewAdmProductos.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_adm_productos);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Call<List<Producto>> listCall = servicioProducto.obtenerProductosPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), VALOR_MINIMO_CONSULTA_PRODUCTOS, VALOR_MAXIMO);
                listCall.enqueue(new Callback<List<Producto>>() {
                    @Override
                    public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                        if (response.isSuccessful()) {
                            List<Producto> productosConsultados = response.body();
                            if (productosConsultados != null && !productosConsultados.isEmpty()) {
                                productosTotales.addAll(productosConsultados);
                                recyclerViewAdapterAdmProductos.addItems(productosConsultados);
                                actualizarValores();
                            }

                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Producto>> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });

        reinicarValoresMaximos();
        cargarClientesPorEmpresa();
        implementarItemTouchListener();

        return view;
    }

    private void reinicarValoresMaximos() {
        VALOR_MINIMO_CONSULTA_PRODUCTOS = 0;
        VALOR_MAXIMO = 2;
    }

    private void actualizarValores() {
        VALOR_MINIMO_CONSULTA_PRODUCTOS = VALOR_MINIMO_CONSULTA_PRODUCTOS + VALOR_MAXIMO;
    }

    public void cargarClientesPorEmpresa() {

        Call<List<Producto>> listCall = servicioProducto.obtenerProductosPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), VALOR_MINIMO_CONSULTA_PRODUCTOS, VALOR_MAXIMO);
        listCall.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    List<Producto> productosConsultados = response.body();
                    if (productosConsultados != null && !productosConsultados.isEmpty()) {
                        productosTotales.addAll(productosConsultados);
                        recyclerViewAdapterAdmProductos = new RecyclerViewAdapterAdmProductos(productosConsultados, getActivity());
                        recyclerViewAdmProductos.setAdapter(recyclerViewAdapterAdmProductos);
                        actualizarValores();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private List<Producto> seleccionarProductos() {
        SparseBooleanArray sparseBooleanArraySeleccionado = recyclerViewAdapterAdmProductos
                .obtenerSparseBooleanArray();
        List<Producto> productosSeleccionados = new ArrayList<>();
        posiciones = new ArrayList<>();
        for (int i = (sparseBooleanArraySeleccionado.size() - 1); i >= 0; i--) {
            if (sparseBooleanArraySeleccionado.valueAt(i)) {
                posiciones.add(sparseBooleanArraySeleccionado.keyAt(i));
                if (!filtro.equals("")) {
                    productosSeleccionados.add(productosFiltro.get(sparseBooleanArraySeleccionado.keyAt(i)));
                } else {
                    productosSeleccionados.add(productosTotales.get(sparseBooleanArraySeleccionado.keyAt(i)));
                }

            }
        }
        return productosSeleccionados;
    }

    private void seleccionarItem(int position) {
        recyclerViewAdapterAdmProductos.toggleSelection(position);
        //Verifica si existe algun item seleccionado o no
        boolean tieneItemsSeleccionados = recyclerViewAdapterAdmProductos.obtenerContadorSeleccionado() > 0;

        if (tieneItemsSeleccionados && actionMode == null)
            // there are some selected items, start the actionMode
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolbarActionModeAdmProductos(getActivity(), recyclerViewAdapterAdmProductos));
        else if (!tieneItemsSeleccionados && actionMode != null)
            // there no selected items, finish the actionMode
            actionMode.finish();

        if (actionMode != null)
            //set action mode title on item selection
            actionMode.setTitle(String.valueOf(recyclerViewAdapterAdmProductos
                    .obtenerContadorSeleccionado()) + " seleccionado");
    }

    //Poner action mode en nulo
    public void setNullToActionMode() {
        if (actionMode != null)
            actionMode = null;
    }

    public void implementarItemTouchListener() {
        recyclerViewAdmProductos.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), recyclerViewAdmProductos, new RecyclerViewClickInterface() {
            @Override
            public void onClick(View view, int position) {

                //Si el action mode no es nulo selecciona el item
                if (actionMode != null)
                    seleccionarItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Selecciona el item en clic largo
                seleccionarItem(position);
            }
        }));
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            if (query != null) {
                filtro = query.toLowerCase();
                filtrarConsulta();
            }
            return true;
        }
    };

    private void filtrarConsulta() {
        if (productosTotales != null && !productosTotales.isEmpty()) {
            productosFiltro = new ArrayList<>();
            Predicate condicion = new Predicate() {

                @Override
                public boolean evaluate(Object object) {
                    Producto producto = (Producto) object;

                    return producto.getCodigoPrincipalProducto().toLowerCase().contains(filtro) || producto.getDescripcionProducto().toLowerCase().startsWith(filtro);

                }
            };
            productosFiltro = (List) CollectionUtils.select(productosTotales, condicion);

            recyclerViewAdapterAdmProductos = new RecyclerViewAdapterAdmProductos(productosFiltro, getActivity());
            recyclerViewAdmProductos.setAdapter(recyclerViewAdapterAdmProductos);
            recyclerViewAdapterAdmProductos.notifyDataSetChanged();

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        administracionProductosActividad = (AdministracionProductosActividad) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        administracionProductosActividad = null;
    }


    /**
     * Edita un producto seleccionado, se valida que solo se pueda seleccionar un producto, llama a la actividad.
     */
    public void editarProducto() {
        List<Producto> productos = seleccionarProductos();
        if (productos != null && !productos.isEmpty()) {
            if (productos.size() == 1) {
                administracionProductosActividad.editarProducto(productos.get(0));
            } else {
                MensajePersonalizado.mostrarToastPersonalizado(getContext(), getActivity().getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "Puede editar un producto a la vez.");
            }
        }

        actionMode.finish();
    }

    /**
     * Comparte la información de productos según las funcionalidades instaladas en el dispositivo móvil del cliente, llama a la actividad.
     */
    public void compartirProductos() {
        List<Producto> productos = seleccionarProductos();
        administracionProductosActividad.compartirProductos(productos);
        actionMode.finish();
    }


    /**
     * Regresa al inicio de la pantalla.
     */
    @Override
    public void irAInicio() {
        recyclerViewAdmProductos.smoothScrollToPosition(0);
    }

    /**
     * Método que actualiza el RecyclerView luego de actualizar el producto.
     */

    public void actualizarProductoRecyclerView(Producto productoActualizado) {
        if (!filtro.equals("")) {
            productosFiltro.set(posiciones.get(0), productoActualizado);
        } else {
            productosTotales.set(posiciones.get(0), productoActualizado);
        }
        recyclerViewAdapterAdmProductos.actualizarProducto(productoActualizado, posiciones.get(0));
    }
}
