package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.RecyclerViewAdapterRepositorioComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestRepositorioComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositorioComprobantesEmitidos extends AppCompatActivity {

    private RecyclerView recyclerViewComprobantesEmitidosAutorizados;

    private RecyclerView.LayoutManager layoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerViewAdapterRepositorioComprobanteElectronico recyclerViewAdapterRepositorioComprobanteElectronico;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestRepositorioComprobanteElectronico.ServicioRepositorioComprobanteElectronico servicioRepositorioComprobanteElectronico;

    private Button buttonFiltroBusqueda;

    private int VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS_AUTORIZADOS;

    private int VALOR_MAXIMO;

    private String secuencialSeleccionado;

    private String identificacionReceptorSeleccionado;

    private String tipoComprobanteSeleccionado;

    private Date fechaInicio;

    private Date fechaFin;

    private boolean existeFiltroBusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositorio_comprobantes_emitidos);
        buttonFiltroBusqueda = (Button) findViewById(R.id.button_filtrar_busqueda);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_comprobantes_emitidos);
        recyclerViewComprobantesEmitidosAutorizados = (RecyclerView) findViewById(R.id.recycler_view_comprobantes_emitidos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tituloToolbar.setText(getResources().getString(R.string.titulo_repositorio_comprobantes_emitidos_autorizados));
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        servicioRepositorioComprobanteElectronico = ClienteRestRepositorioComprobanteElectronico.getServicioRepositorioComprobanteElectronico();
        layoutManager = new LinearLayoutManager(this);
        recyclerViewComprobantesEmitidosAutorizados.setLayoutManager(layoutManager);
        reinicarValoresMaximos();
        cargarComprobanteEmitidosAutorizados();
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_blue_dark, android.R.color.holo_green_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Map<String, String> estados = new HashMap<String, String>();
                estados.put("ESTADO", "1");
                VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS_AUTORIZADOS = VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS_AUTORIZADOS + VALOR_MAXIMO + 1;
                Call<List<ComprobanteElectronico>> call = null;
                if (existeFiltroBusqueda) {
                    call = servicioRepositorioComprobanteElectronico.obtenerComprobantesElectronicosEmitidosPorEmpresaPorFiltroBusqueda(claseGlobalUsuario.getIdEmpresa(), estados, fechaInicio, fechaFin, tipoComprobanteSeleccionado, secuencialSeleccionado, identificacionReceptorSeleccionado, VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS_AUTORIZADOS, VALOR_MAXIMO);
                } else {
                    call = servicioRepositorioComprobanteElectronico.obtenerComprobantesElectronicosEmitidosPorEmpresa(claseGlobalUsuario.getIdEmpresa(), estados, VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS_AUTORIZADOS, VALOR_MAXIMO);
                }
                call.enqueue(new Callback<List<ComprobanteElectronico>>() {
                    @Override
                    public void onResponse(Call<List<ComprobanteElectronico>> call, Response<List<ComprobanteElectronico>> response) {
                        if (response.isSuccessful()) {
                            List<ComprobanteElectronico> comprobanteElectronicoList = response.body();
                            if (comprobanteElectronicoList != null && !comprobanteElectronicoList.isEmpty()) {
                                recyclerViewAdapterRepositorioComprobanteElectronico.addItems(comprobanteElectronicoList);
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ComprobanteElectronico>> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });
        buttonFiltroBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FiltroBusquedaRepositorio.class);
                startActivityForResult(intent, Codigos.CODIGO_FILTRO_BUSQUEDA);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }

    private void reinicarValoresMaximos() {
        VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS_AUTORIZADOS = 1;
        VALOR_MAXIMO = 2;
    }

    private void cargarComprobanteEmitidosAutorizados() {
        Map<String, String> estados = new HashMap<String, String>();
        estados.put("ESTADO", "1");
        Call<List<ComprobanteElectronico>> call = servicioRepositorioComprobanteElectronico.obtenerComprobantesElectronicosEmitidosPorEmpresa(claseGlobalUsuario.getIdEmpresa(), estados, VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS_AUTORIZADOS, VALOR_MAXIMO);
        call.enqueue(new Callback<List<ComprobanteElectronico>>() {
            @Override
            public void onResponse(Call<List<ComprobanteElectronico>> call, Response<List<ComprobanteElectronico>> response) {
                if (response.isSuccessful()) {
                    List<ComprobanteElectronico> comprobanteElectronicoList = response.body();
                    if (comprobanteElectronicoList != null && !comprobanteElectronicoList.isEmpty()) {
                        recyclerViewAdapterRepositorioComprobanteElectronico = new RecyclerViewAdapterRepositorioComprobanteElectronico(comprobanteElectronicoList);
                        recyclerViewComprobantesEmitidosAutorizados.setAdapter(recyclerViewAdapterRepositorioComprobanteElectronico);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ComprobanteElectronico>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Codigos.CODIGO_FILTRO_BUSQUEDA) {
            if (resultCode == RESULT_OK) {
                Map<String, String> estados = new HashMap<String, String>();
                estados.put("ESTADO", "1");
                existeFiltroBusqueda = true;
                if (data != null) {
                    reinicarValoresMaximos();
                    secuencialSeleccionado = data.getExtras().getString(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_SECUENCIAL));
                    identificacionReceptorSeleccionado = data.getExtras().getString(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_RECEPTOR));
                    tipoComprobanteSeleccionado = data.getExtras().getString(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_TIPO_COMPROBANTE));
                    fechaInicio = (Date) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_FECHA_INICIO));
                    fechaFin = (Date) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_FECHA_FINAL));
                    Call<List<ComprobanteElectronico>> call = servicioRepositorioComprobanteElectronico.obtenerComprobantesElectronicosEmitidosPorEmpresaPorFiltroBusqueda(claseGlobalUsuario.getIdEmpresa(), estados, fechaInicio, fechaFin, tipoComprobanteSeleccionado, secuencialSeleccionado, identificacionReceptorSeleccionado, VALOR_MINIMO_CONSULTA_COMPROBANTES_EMITIDOS_AUTORIZADOS, VALOR_MAXIMO);
                    call.enqueue(new Callback<List<ComprobanteElectronico>>() {
                        @Override
                        public void onResponse(Call<List<ComprobanteElectronico>> call, Response<List<ComprobanteElectronico>> response) {
                            if (response.isSuccessful()) {
                                List<ComprobanteElectronico> comprobanteElectronicoList = response.body();
                                if (comprobanteElectronicoList != null && !comprobanteElectronicoList.isEmpty()) {
                                    recyclerViewAdapterRepositorioComprobanteElectronico.clear();
                                    recyclerViewAdapterRepositorioComprobanteElectronico.addAll(comprobanteElectronicoList);
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ComprobanteElectronico>> call, Throwable t) {
                            call.cancel();
                        }
                    });
                }
            } else if (resultCode == RESULT_CANCELED) {
                existeFiltroBusqueda = false;
                reinicarValoresMaximos();
                cargarComprobanteEmitidosAutorizados();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
