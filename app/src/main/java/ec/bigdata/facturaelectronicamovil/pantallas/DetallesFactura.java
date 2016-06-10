package ec.bigdata.facturaelectronicamovil.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.comprobanteelectronico.esquema.factura.Detalle;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ExpandableDetalleAdapter;
import ec.bigdata.facturaelectronicamovil.utilidades.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidades.Codigos;

public class DetallesFactura extends AppCompatActivity {

    private Toolbar toolbar;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ExpandableDetalleAdapter expandableDetalleAdapter;

    private List<ec.bigdata.comprobanteelectronico.esquema.factura.Detalle> detalles;

    private ExpandableListView expandableListViewDetalles;

    private TextView textViewDetalleVacio;

    private Button buttonContinuar;

    private Button buttonNuevoDetalle;

    private Detalle detalleFacturaSeleccionado;

    private ArrayList<ImpuestoComprobanteElectronico> impuestosDetalleSeleccionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_factura);

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

        textViewDetalleVacio = (TextView) findViewById(R.id.text_view_detalles_vacio);

        expandableListViewDetalles = (ExpandableListView) findViewById(R.id.expandable_list_view_detalles);

        //Cabecera
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.header_expandable_list_view_detalles, expandableListViewDetalles, false);

        expandableListViewDetalles.addHeaderView(headerView, null, false);


        //Se muestra el ExpandableListView vacío
        detalles =
                new ArrayList<Detalle>();
        expandableDetalleAdapter = new ExpandableDetalleAdapter(detalles, this);

        expandableListViewDetalles.setAdapter(expandableDetalleAdapter);

        expandableListViewDetalles.setEmptyView(textViewDetalleVacio);

        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO definir que se envía como extra al continuar
            }
        });
        buttonNuevoDetalle = (Button) findViewById(R.id.button_nuevo_detalle);
        buttonNuevoDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ec.bigdata.facturaelectronicamovil.pantallas.Detalle.class);
                startActivityForResult(intent, Codigos.CODIGO_NUEVO_DETALLE_FACTURA);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
        expandableListViewDetalles.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        detalles.get(groupPosition).getCodigoPrincipal() + " List Expanded.",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        expandableListViewDetalles.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
               /* Toast.makeText(getApplicationContext(),
                        detalles.get(groupPosition).getCodigoPrincipal() + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        expandableListViewDetalles.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                /*Toast.makeText(
                        getApplicationContext(),
                        detalles.get(groupPosition).getCodigoPrincipal()
                                + " -> "
                                + detalles.get(
                                groupPosition).getImpuestos().get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();*/
                return false;
            }
        });
        expandableListViewDetalles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                detalleFacturaSeleccionado = (Detalle) parent.getItemAtPosition(position);
                impuestosDetalleSeleccionado = new ArrayList<ImpuestoComprobanteElectronico>(detalleFacturaSeleccionado.getImpuestos());
                PopupMenu popup = new PopupMenu(DetallesFactura.this, view, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.menu_detalle_factura, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_borrar:
                                detalles.remove(detalleFacturaSeleccionado);
                                expandableDetalleAdapter.notifyDataSetChanged();
                                break;
                            case R.id.item_impuestos:
                                Intent intent = new Intent(getApplicationContext(), Impuesto.class);
                                intent.putExtra(String.valueOf(Codigos.CODIGO_ID_DETALLE), position - 1);
                                intent.putExtra(String.valueOf(Codigos.CODIGO_LISTA_IMPUESTOS), impuestosDetalleSeleccionado);
                                startActivityForResult(intent, Codigos.CODIGO_IMPUESTO_ACTUALIZADO);
                        }
                        return true;
                    }

                });
                popup.show();
                return true;
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Codigos.CODIGO_NUEVO_DETALLE_FACTURA) {
            if (resultCode == RESULT_OK) {
                Detalle detalle = (Detalle) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_DETALLE_NUEVO_AGREGADO));
                detalles.add(detalle);
                expandableDetalleAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == Codigos.CODIGO_IMPUESTO_ACTUALIZADO) {
            if (resultCode == RESULT_OK) {
                int posicionDetalle = data.getExtras().getInt(String.valueOf(Codigos.CODIGO_ID_DETALLE));
                ArrayList<ImpuestoComprobanteElectronico> impuestosComprobanteElectronicoActualizada = (ArrayList<ImpuestoComprobanteElectronico>) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_LISTA_IMPUESTOS));
                detalles.get(posicionDetalle).setImpuestos(impuestosComprobanteElectronicoActualizada);
                expandableDetalleAdapter.notifyDataSetChanged();
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
