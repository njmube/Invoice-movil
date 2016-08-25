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

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayListEstados;
import ec.bigdata.facturaelectronicamovil.interfaz.RecyclerViewClickInterface;
import ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestRepositorioComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DavidLeonardo on 19/8/2016.
 */
public class ComprobantesEmitidosAutorizadosFragmento extends Fragment {

    private RecyclerView recyclerView;

    private RecyclerViewAdapterComprobantesEmitidos recyclerViewAdapterComprobantesEmitidos;

    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<ComprobanteElectronico> comprobanteElectronicosTotales;

    private List<ComprobanteElectronico> comprobanteElectronicosFiltro;

    private ActionMode actionMode;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestRepositorioComprobanteElectronico.ServicioRepositorioComprobanteElectronico servicioRepositorioComprobanteElectronico;

    private int VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS;

    private int VALOR_MAXIMO;

    private String filtro;

    private ComprobantesEmitidosAutorizados comprobantesEmitidosAutorizados;

    private List<Integer> posiciones;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_comprobantes_emitidos_autorizados_fragment, container, false);
        filtro = "";
        comprobanteElectronicosTotales = new ArrayList<>();
        comprobanteElectronicosFiltro = new ArrayList<>();
        claseGlobalUsuario = (ClaseGlobalUsuario) getActivity().getApplicationContext();
        servicioRepositorioComprobanteElectronico = ClienteRestRepositorioComprobanteElectronico.getServicioRepositorioComprobanteElectronico();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_comprobantes_emitidos_autorizados);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_comprobantes_emitidos_autorizados);
        implementarItemTouchListener();
        reinicarValoresMaximos();
        cargarComprobanteEmitidosAutorizados();
        return view;

    }

    private void reinicarValoresMaximos() {
        VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS = 0;
        VALOR_MAXIMO = 2;
    }

    private void actualizarValores() {
        VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS = VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS + VALOR_MAXIMO;
    }


    private List<ComprobanteElectronico> seleccionarComprobantes() {
        SparseBooleanArray sparseBooleanArraySeleccionado = recyclerViewAdapterComprobantesEmitidos
                .obtenerSparseBooleanArray();
        List<ComprobanteElectronico> comprobanteElectronicos = new ArrayList<>();
        posiciones = new ArrayList<>();
        for (int i = (sparseBooleanArraySeleccionado.size() - 1); i >= 0; i--) {
            if (sparseBooleanArraySeleccionado.valueAt(i)) {
                posiciones.add(sparseBooleanArraySeleccionado.keyAt(i));
                if (!filtro.equals("")) {
                    comprobanteElectronicos.add(comprobanteElectronicosFiltro.get(sparseBooleanArraySeleccionado.keyAt(i)));
                } else {
                    comprobanteElectronicos.add(comprobanteElectronicosTotales.get(sparseBooleanArraySeleccionado.keyAt(i)));
                }

            }
        }
        return comprobanteElectronicos;
    }

    private void seleccionarItem(int position) {
        recyclerViewAdapterComprobantesEmitidos.toggleSelection(position);
        //Verifica si existe algun item seleccionado o no
        boolean tieneItemsSeleccionados = recyclerViewAdapterComprobantesEmitidos.obtenerContadorSeleccionado() > 0;

        if (tieneItemsSeleccionados && actionMode == null)
            // there are some selected items, start the actionMode
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolbarActionModeComprobantesEmitidos(getActivity(), recyclerViewAdapterComprobantesEmitidos));
        else if (!tieneItemsSeleccionados && actionMode != null)
            // there no selected items, finish the actionMode
            actionMode.finish();

        if (actionMode != null)
            //set action mode title on item selection
            actionMode.setTitle(String.valueOf(recyclerViewAdapterComprobantesEmitidos
                    .obtenerContadorSeleccionado()) + " seleccionado");
    }

    //Poner action mode en nulo
    public void setNullToActionMode() {
        if (actionMode != null)
            actionMode = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        comprobantesEmitidosAutorizados = (ComprobantesEmitidosAutorizados) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        comprobantesEmitidosAutorizados = null;
    }

    public void implementarItemTouchListener() {
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), recyclerView, new RecyclerViewClickInterface() {
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

    private void cargarComprobanteEmitidosAutorizados() {
        ArrayList<String> estados = new ArrayList<>();
        estados.add("1");
        ArrayListEstados arrayListEstados = new ArrayListEstados(estados);
        Call<List<ComprobanteElectronico>> call = servicioRepositorioComprobanteElectronico.obtenerComprobantesElectronicosEmitidosPorEmpresa(claseGlobalUsuario.getIdEmpresa(), arrayListEstados, VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS, VALOR_MAXIMO);
        call.enqueue(new Callback<List<ComprobanteElectronico>>() {
            @Override
            public void onResponse(Call<List<ComprobanteElectronico>> call, Response<List<ComprobanteElectronico>> response) {
                if (response.isSuccessful()) {
                    List<ComprobanteElectronico> comprobanteElectronicoList = response.body();
                    if (comprobanteElectronicoList != null && !comprobanteElectronicoList.isEmpty()) {
                        comprobanteElectronicosTotales.addAll(comprobanteElectronicoList);
                        recyclerViewAdapterComprobantesEmitidos = new RecyclerViewAdapterComprobantesEmitidos(comprobanteElectronicosTotales, getActivity());
                        recyclerView.setAdapter(recyclerViewAdapterComprobantesEmitidos);
                        actualizarValores();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ComprobanteElectronico>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    /**
     * Método que descargar el comprobante en XML o PDF, permite seleccionar solo uno.
     */
    public void descargarComprobanteElectronico() {
        List<ComprobanteElectronico> comprobantesEmitidosAutorizadosList = seleccionarComprobantes();
        if (comprobantesEmitidosAutorizadosList.size() == 1) {
            comprobantesEmitidosAutorizados.mostrarDialogDescargaArchivo(comprobantesEmitidosAutorizadosList.get(0));
        } else {
            MensajePersonalizado.mostrarToastPersonalizado(getContext(), getActivity().getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "Puede descargar un comprobante a la vez.");
        }
    }

    public void reenviarComprobante() {
        List<ComprobanteElectronico> comprobantesEmitidosAutorizadosList = seleccionarComprobantes();
        if (comprobantesEmitidosAutorizadosList.size() == 1) {
            comprobantesEmitidosAutorizados.mostrarDialogListaCheckBoxReenvioComprobante(comprobantesEmitidosAutorizadosList.get(0));
        } else {
            MensajePersonalizado.mostrarToastPersonalizado(getContext(), getActivity().getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "Puede reenviar un comprobante a la vez.");
        }
    }

    public void mostrarDetalleComprobante() {

        List<ComprobanteElectronico> comprobantesEmitidosAutorizadosList = seleccionarComprobantes();
        if (comprobantesEmitidosAutorizadosList.size() == 1) {
            comprobantesEmitidosAutorizados.mostrarDialogDetallesComprobante(comprobantesEmitidosAutorizadosList.get(0));
        } else {
            MensajePersonalizado.mostrarToastPersonalizado(getContext(), getActivity().getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "Puede ver un comprobante a la vez.");
        }
    }

    public void compartirComprobante() {

        List<ComprobanteElectronico> comprobantesEmitidosAutorizadosList = seleccionarComprobantes();
        if (comprobantesEmitidosAutorizadosList.size() == 1) {
            comprobantesEmitidosAutorizados.compartirComprobante(comprobantesEmitidosAutorizadosList.get(0));
        } else {
            MensajePersonalizado.mostrarToastPersonalizado(getContext(), getActivity().getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "Puede compartir un comprobante a la vez.");
        }
    }


}