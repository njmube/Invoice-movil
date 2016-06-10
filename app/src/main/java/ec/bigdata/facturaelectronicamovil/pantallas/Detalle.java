package ec.bigdata.facturaelectronicamovil.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterProducto;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestProducto;
import ec.bigdata.facturaelectronicamovil.utilidades.Calculos;
import ec.bigdata.facturaelectronicamovil.utilidades.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidades.Codigos;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detalle extends AppCompatActivity {

    private Toolbar toolbar;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestProducto.ServicioProducto servicioProducto;


    private ArrayAdapterProducto arrayAdapterProducto;

    private AutoCompleteTextView autoCompleteTextViewProductos;

    private ec.bigdata.facturaelectronicamovil.modelo.Producto productoSeleccionado;

    private TextView textViewProductoSeleccionado;

    private TextView textViewCantidad;

    private NumberPicker numberPickerCantidad;

    private EditText editTextPrecioUnitario;

    private EditText editTextDescuento;

    private TextView textViewPrecioTotal;

    private Button buttonNuevoProducto;

    private Button buttonActualizarProducto;

    private Button buttonAgregarDetalle;

    private Button buttonContinuar;

    private ec.bigdata.comprobanteelectronico.esquema.factura.Detalle nuevoDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);

        tituloToolbar.setText(getResources().getString(R.string.titulo_detalle));

        autoCompleteTextViewProductos = (AutoCompleteTextView) findViewById(R.id.auto_complete_text_view_busqueda_producto);

        textViewCantidad = (TextView) findViewById(R.id.text_view_valor_cantidad);

        numberPickerCantidad = (NumberPicker) findViewById(R.id.number_picker_cantidad);

        editTextPrecioUnitario = (EditText) findViewById(R.id.edit_text_precio_unitario);

        editTextDescuento = (EditText) findViewById(R.id.edit_text_descuento);

        textViewPrecioTotal = (TextView) findViewById(R.id.text_view_valor_precio_total);

        buttonAgregarDetalle = (Button) findViewById(R.id.button_agregar_detalle);

        textViewProductoSeleccionado = (TextView) findViewById(R.id.text_view_producto_seleccionado);

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        servicioProducto = ClienteRestProducto.getServicioProducto();

        //Valor mínimo de NumberPicker
        numberPickerCantidad.setMinValue(1);
        textViewCantidad.setText("1");
        //Valor máximo de NumberPicker
        numberPickerCantidad.setMaxValue(100);

        //Obtiene si la rueda selectora envuelve al alcanzar el valor mínimo / máximo.
        numberPickerCantidad.setWrapSelectorWheel(true);

        //Establecer un oyente de cambio de valor de NumberPicker
        numberPickerCantidad.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                textViewCantidad.setText(String.valueOf(newVal));
                String precioTotal = Calculos.actualizarPrecioTotal(textViewCantidad.getText().toString(), editTextPrecioUnitario.getText().toString(), editTextDescuento.getText().toString());
                textViewPrecioTotal.setText(precioTotal);
            }
        });

        //Captura el cambio en el ingreso del descuento
        editTextDescuento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String precioTotal = Calculos.actualizarPrecioTotal(textViewCantidad.getText().toString(), editTextPrecioUnitario.getText().toString(), editTextDescuento.getText().toString());
                textViewPrecioTotal.setText(precioTotal);
            }
        });
        //Captura el cambio en el ingreso del precio unitario
        editTextPrecioUnitario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String precioTotal = Calculos.actualizarPrecioTotal(textViewCantidad.getText().toString(), editTextPrecioUnitario.getText().toString(), editTextDescuento.getText().toString());
                textViewPrecioTotal.setText(precioTotal);
            }
        });

        autoCompleteTextViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productoSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Producto) parent.getItemAtPosition(position);
                if (productoSeleccionado != null) {
                    textViewProductoSeleccionado.setText(productoSeleccionado.getCodigoPrincipalProducto() + "-" + productoSeleccionado.getDescripcionProducto());
                    editTextPrecioUnitario.setText(productoSeleccionado.getPrecioUnitarioProducto());
                    String precioTotal = Calculos.actualizarPrecioTotal(textViewCantidad.getText().toString(), editTextPrecioUnitario.getText().toString(), editTextDescuento.getText().toString());
                    textViewPrecioTotal.setText(precioTotal);

                }
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });
        cargarProductos();
        buttonNuevoProducto = (Button) findViewById(R.id.button_nuevo_producto);
        buttonNuevoProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNuevoProducto = new Intent(getApplicationContext(), NuevoProducto.class);
                startActivity(intentNuevoProducto);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
        buttonActualizarProducto = (Button) findViewById(R.id.button_editar_producto);
        buttonActualizarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productoSeleccionado != null) {
                    Intent intentActualizarProducto = new Intent(getApplicationContext(), ActualizacionProducto.class);
                    intentActualizarProducto.putExtra(String.valueOf(Codigos.CODIGO_PRODUCTO_SELECCIONADO), productoSeleccionado);
                    startActivity(intentActualizarProducto);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                } else {
                    Toast.makeText(Detalle.this, "Debe seleccionar un producto.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonAgregarDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productoSeleccionado != null) {
                    nuevoDetalle = new ec.bigdata.comprobanteelectronico.esquema.factura.Detalle();
                    nuevoDetalle.setCodigoPrincipal(productoSeleccionado.getCodigoPrincipalProducto());
                    if (productoSeleccionado.getCodigoAuxiliarProducto() != null) {
                        nuevoDetalle.setCodigoAuxiliar(productoSeleccionado.getCodigoAuxiliarProducto());
                    }
                    nuevoDetalle.setDescripcion(productoSeleccionado.getDescripcionProducto());
                    nuevoDetalle.setCantidad(textViewCantidad.getText().toString());
                    nuevoDetalle.setPrecioUnitario(editTextPrecioUnitario.getText().toString());
                    nuevoDetalle.setPrecioTotalSinImpuesto(textViewPrecioTotal.getText().toString());
                    if (editTextDescuento.getText() != null && !editTextDescuento.getText().toString().trim().equals("")) {
                        nuevoDetalle.setDescuento(editTextDescuento.getText().toString());
                    }
                    //TODO Elegir que impuesto tomar, impuesto por defecto IVA 14%
                    List<ImpuestoComprobanteElectronico> impuestosComprobanteElectronico = new ArrayList<ImpuestoComprobanteElectronico>();
                    ImpuestoComprobanteElectronico impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
                    impuestoComprobanteElectronico.setCodigo("1");
                    impuestoComprobanteElectronico.setCodigoPorcentaje("3");
                    impuestoComprobanteElectronico.setBaseImponible(textViewPrecioTotal.getText().toString());
                    impuestoComprobanteElectronico.setTarifa("14");
                    impuestoComprobanteElectronico.setValor(Calculos.obtenerValor(textViewPrecioTotal.getText().toString(), "14"));
                    impuestosComprobanteElectronico.add(impuestoComprobanteElectronico);
                    nuevoDetalle.setImpuestos(impuestosComprobanteElectronico);
                    enviarDetalle();

                } else {
                    Toast.makeText(Detalle.this, "Debe seleccionar un producto para agregar el detalle.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarDetalle();
            }
        });
    }

    private void enviarDetalle() {
        Intent intent = new Intent(getApplicationContext(), InformacionFactura.class);
        //TODO definir que se envía como extra al continuar
        if (nuevoDetalle != null) {
            intent.putExtra(String.valueOf(Codigos.CODIGO_DETALLE_NUEVO_AGREGADO), nuevoDetalle);
            setResult(RESULT_OK, intent);
            finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Codigos.CODIGO_PRODUCTO_SELECCIONADO_ACTUALIZADO) {
            if (resultCode == RESULT_OK) {
                cargarProductos();
            }
        }
    }

    private void cargarProductos() {

        Call<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> call_producto = servicioProducto.obtenerProductosPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa());
        call_producto.enqueue(new Callback<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>>() {
            @Override
            public void onResponse(Call<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> call, Response<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> response) {
                if (response.isSuccessful()) {
                    List<ec.bigdata.facturaelectronicamovil.modelo.Producto> productos = response.body();
                    if (productos != null && !productos.isEmpty()) {
                        //Se asigna el origen de datos
                        arrayAdapterProducto = new ArrayAdapterProducto(getApplicationContext(), R.layout.activity_cliente, R.id.tv_cliente_filtrado, productos);
                        //Se setea el adaptador
                        autoCompleteTextViewProductos.setAdapter(arrayAdapterProducto);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> call, Throwable t) {

            }
        });
    }

}
