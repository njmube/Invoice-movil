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
import ec.bigdata.facturaelectronicamovil.modelo.Cliente;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCliente;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DavidLeonardo on 30/7/2016.
 */
public class AdministracionClientesFragmento extends Fragment implements AdmClientesComunicacionFragmentInterface {

    private RecyclerView recyclerViewAdmClientes;

    private RecyclerViewAdapterAdmClientes recyclerViewAdapterAdmClientes;

    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private SearchView searchViewFiltroBusquedaCliente;

    private List<Cliente> clientesTotales;

    private List<Cliente> clientesFiltro;

    private ActionMode actionMode;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestCliente.ServicioCliente servicioCliente;

    private int VALOR_MINIMO_CONSULTA_CLIENTES;

    private int VALOR_MAXIMO;

    private String filtro;

    private AdministracionClientesActividad administracionClientesActividad;

    private List<Integer> posiciones;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_adm_clientes_fragment, container, false);

        searchViewFiltroBusquedaCliente = (SearchView) view.findViewById(R.id.search_view_filtro_cliente);
        searchViewFiltroBusquedaCliente.setOnQueryTextListener(onQueryTextListener);
        filtro = "";

        clientesTotales = new ArrayList<>();
        clientesFiltro = new ArrayList<>();
        claseGlobalUsuario = (ClaseGlobalUsuario) getActivity().getApplicationContext();
        servicioCliente = ClienteRestCliente.getServicioCliente();
        recyclerViewAdmClientes = (RecyclerView) view.findViewById(R.id.recycler_view_adm_clientes);
        recyclerViewAdmClientes.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAdmClientes.setLayoutManager(layoutManager);
        recyclerViewAdmClientes.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_adm_clientes);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Call<List<ec.bigdata.facturaelectronicamovil.modelo.Cliente>> listCall = servicioCliente.obtenerClientesPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), VALOR_MINIMO_CONSULTA_CLIENTES, VALOR_MAXIMO);
                listCall.enqueue(new Callback<List<Cliente>>() {
                    @Override
                    public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                        if (response.isSuccessful()) {
                            List<ec.bigdata.facturaelectronicamovil.modelo.Cliente> clientesConsultados = response.body();

                            if (clientesConsultados != null && !clientesConsultados.isEmpty()) {
                                clientesTotales.addAll(clientesConsultados);
                                recyclerViewAdapterAdmClientes.addItems(clientesConsultados);
                                actualizarValores();
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Cliente>> call, Throwable t) {
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
        VALOR_MINIMO_CONSULTA_CLIENTES = 0;
        VALOR_MAXIMO = 2;
    }

    private void actualizarValores() {
        VALOR_MINIMO_CONSULTA_CLIENTES = VALOR_MINIMO_CONSULTA_CLIENTES + VALOR_MAXIMO;
    }


    public void cargarClientesPorEmpresa() {

        Call<List<Cliente>> listCall = servicioCliente.obtenerClientesPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), VALOR_MINIMO_CONSULTA_CLIENTES, VALOR_MAXIMO);
        listCall.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<ec.bigdata.facturaelectronicamovil.modelo.Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful()) {
                    List<ec.bigdata.facturaelectronicamovil.modelo.Cliente> clientesConsultados = response.body();
                    if (clientesConsultados != null && !clientesConsultados.isEmpty()) {
                        clientesTotales.addAll(clientesConsultados);
                        recyclerViewAdapterAdmClientes = new RecyclerViewAdapterAdmClientes(clientesConsultados, getActivity());
                        recyclerViewAdmClientes.setAdapter(recyclerViewAdapterAdmClientes);
                        actualizarValores();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ec.bigdata.facturaelectronicamovil.modelo.Cliente>> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private List<Cliente> seleccionarClientes() {
        SparseBooleanArray sparseBooleanArraySeleccionado = recyclerViewAdapterAdmClientes
                .obtenerSparseBooleanArray();
        List<Cliente> clientesSeleccionados = new ArrayList<>();
        posiciones = new ArrayList<>();
        for (int i = (sparseBooleanArraySeleccionado.size() - 1); i >= 0; i--) {
            if (sparseBooleanArraySeleccionado.valueAt(i)) {
                posiciones.add(sparseBooleanArraySeleccionado.keyAt(i));
                if (!filtro.equals("")) {
                    clientesSeleccionados.add(clientesFiltro.get(sparseBooleanArraySeleccionado.keyAt(i)));
                } else {
                    clientesSeleccionados.add(clientesTotales.get(sparseBooleanArraySeleccionado.keyAt(i)));
                }

            }
        }
        return clientesSeleccionados;
    }

    private void seleccionarItem(int position) {
        recyclerViewAdapterAdmClientes.toggleSelection(position);
        //Verifica si existe algun item seleccionado o no
        boolean tieneItemsSeleccionados = recyclerViewAdapterAdmClientes.obtenerContadorSeleccionado() > 0;

        if (tieneItemsSeleccionados && actionMode == null)
            // there are some selected items, start the actionMode
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolbarActionModeAdmClientes(getActivity(), recyclerViewAdapterAdmClientes));
        else if (!tieneItemsSeleccionados && actionMode != null)
            // there no selected items, finish the actionMode
            actionMode.finish();

        if (actionMode != null)
            //set action mode title on item selection
            actionMode.setTitle(String.valueOf(recyclerViewAdapterAdmClientes
                    .obtenerContadorSeleccionado()) + " seleccionado");
    }

    private void seleccionarItemSinActionMode(int position) {
        recyclerViewAdapterAdmClientes.toggleSelection(position);
    }

    //Poner action mode en nulo
    public void setNullToActionMode() {
        if (actionMode != null)
            actionMode = null;
    }

    public void implementarItemTouchListener() {
        recyclerViewAdmClientes.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), recyclerViewAdmClientes, new RecyclerViewClickInterface() {
            @Override
            public void onClick(View view, int position) {

                //Si el action mode no es nulo selecciona el item
                if (actionMode != null) {
                    seleccionarItem(position);
                }

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
        if (clientesTotales != null && !clientesTotales.isEmpty()) {
            clientesFiltro = new ArrayList<>();
            Predicate condicion = new Predicate() {

                @Override
                public boolean evaluate(Object object) {
                    ec.bigdata.facturaelectronicamovil.modelo.Cliente cliente = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) object;


                    return cliente.getRazonSocialCliente().toLowerCase().contains(filtro) || cliente.getIdentificacionCliente().startsWith(filtro);


                }
            };
            clientesFiltro = (List) CollectionUtils.select(clientesTotales, condicion);

            recyclerViewAdapterAdmClientes = new RecyclerViewAdapterAdmClientes(clientesFiltro, getActivity());
            recyclerViewAdmClientes.setAdapter(recyclerViewAdapterAdmClientes);
            recyclerViewAdapterAdmClientes.notifyDataSetChanged();

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        administracionClientesActividad = (AdministracionClientesActividad) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        administracionClientesActividad = null;
    }

    /**
     * Comparte la información de los clientes según las funcionalidades instaladas en el dispositivo móvil del cliente, llama a la actividad.
     */
    public void compartirCliente() {
        List<Cliente> clientes = seleccionarClientes();
        administracionClientesActividad.compartirCliente(clientes);
        actionMode.finish();
    }

    /**
     * Edita un cliente seleccionado, se valida que solo se pueda seleccionar un cliente, llama a la actividad.
     */
    public void editarCliente() {
        List<Cliente> clientes = seleccionarClientes();
        if (clientes != null && !clientes.isEmpty()) {
            if (clientes.size() == 1) {
                administracionClientesActividad.editarCliente(clientes.get(0));
            } else {
                MensajePersonalizado.mostrarToastPersonalizado(getContext(), getActivity().getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "Puede editar un cliente a la vez.");
            }
        }

        actionMode.finish();
    }


    /**
     * Realiza la llamada a un cliente seleccionando el teléfono desde un dialog con Spinner.
     */
    public void llamarCliente() {
        List<Cliente> clientes = seleccionarClientes();
        String[] telefonos = new String[1];
        if (clientes != null && !clientes.isEmpty()) {
            if (clientes.size() == 1) {
                telefonos[0] = clientes.get(0).getTelefonoCliente();
                administracionClientesActividad.cargarTelefonosCliente(telefonos);
            } else {
                MensajePersonalizado.mostrarToastPersonalizado(getContext(), getActivity().getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "No puede seleccionar más de un cliente.");
            }
        }

        actionMode.finish();
    }

    public void enviarCorreoCliente() {
        List<Cliente> clientes = seleccionarClientes();
        if (clientes != null && !clientes.isEmpty()) {
            if (clientes.size() == 1) {
                administracionClientesActividad.mostrarDialogEnvioCorreoCliente(clientes.get(0));
            } else {
                MensajePersonalizado.mostrarToastPersonalizado(getContext(), getActivity().getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "No puede seleccionar más de un cliente.");
            }
        }
    }

    /**
     * Regresa al inicio de la pantalla.
     */
    @Override
    public void irAInicio() {
        recyclerViewAdmClientes.smoothScrollToPosition(0);
    }

    /**
     * Método que actualiza el RecyclerView luego de actualizar el cliente.
     */

    public void actualizarClienteRecyclerView(Cliente clienteActualizado) {
        if (!filtro.equals("")) {
            clientesFiltro.set(posiciones.get(0), clienteActualizado);
        } else {
            clientesTotales.set(posiciones.get(0), clienteActualizado);
        }
        recyclerViewAdapterAdmClientes.actualizarCliente(clienteActualizado, posiciones.get(0));
    }
}
