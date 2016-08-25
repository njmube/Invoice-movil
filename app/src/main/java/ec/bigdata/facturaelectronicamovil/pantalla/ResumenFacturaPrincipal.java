package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import ec.bigdata.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ViewPagerAdapter;
import ec.bigdata.facturaelectronicamovil.interfaz.ResumenFacturaComunicacionFragmentInterface;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;

public class ResumenFacturaPrincipal extends AppCompatActivity implements ResumenFacturaComunicacionFragmentInterface {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private Button buttonContinuarRIDE;

    private ViewPagerAdapter viewPagerAdapter;

    private ImplementacionFactura implementacionFactura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_resumen_factura);
        buttonContinuarRIDE = (Button) findViewById(R.id.button_continuar_ride_factura);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);

        tituloToolbar.setText(getResources().getString(R.string.titulo_resumen_comprobante_electronico));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            implementacionFactura = (ImplementacionFactura) bundle.getSerializable(String.valueOf(Codigos.CODIGO_EXTRA_FACTURA_ELECTRONICA));
        }
        viewPager = (ViewPager) findViewById(R.id.view_pager_resumen_factura);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_resumen_factura);
        tabLayout.setupWithViewPager(viewPager);
        buttonContinuarRIDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RIDE.class);
                intent.putExtra(String.valueOf(Codigos.CODIGO_EXTRA_COMPROBANTE_ELECTRONICO), implementacionFactura);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        FragmentoResumenEmisorReceptorComprobante resumenEmisorReceptorComprobante = FragmentoResumenEmisorReceptorComprobante.newInstance(implementacionFactura);
        viewPagerAdapter.addFrag(resumenEmisorReceptorComprobante, "Emisor-Receptor");

        FragmentoResumenDetallesComprobante resumenDetallesComprobante = FragmentoResumenDetallesComprobante.newInstance(implementacionFactura);
        viewPagerAdapter.addFrag(resumenDetallesComprobante, "Detalles");

        FragmentoResumenFacturaImpuestos fragmentoResumenFacturaImpuestos = FragmentoResumenFacturaImpuestos.newInstance(implementacionFactura);
        viewPagerAdapter.addFrag(fragmentoResumenFacturaImpuestos, "Totales");

        FragmentoResumenFormasPago fragmentoResumenFormasPago = FragmentoResumenFormasPago.newInstance(implementacionFactura);
        viewPagerAdapter.addFrag(fragmentoResumenFormasPago, "Formas de pago");

        ArrayList<InformacionAdicional> informacionAdicionalArrayList = (ArrayList<InformacionAdicional>) implementacionFactura.getInformacionAdicional();
        if (informacionAdicionalArrayList == null) {
            informacionAdicionalArrayList = new ArrayList<>();
        }
        FragmentoResumenInformacionAdicional fragmentoResumenInformacionAdicional = FragmentoResumenInformacionAdicional.newInstance(informacionAdicionalArrayList);
        viewPagerAdapter.addFrag(fragmentoResumenInformacionAdicional, "Información adicional");

        viewPager.setAdapter(viewPagerAdapter);


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
        Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
        intent.putExtra(String.valueOf(Codigos.CODIGO_EXTRA_FACTURA_ELECTRONICA), implementacionFactura);
        setResult(RESULT_OK, intent);
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    public Fragment getFragment(int pos) {
        return viewPagerAdapter.getItem(pos);
    }

    @Override
    public void actualizarInformacionFactura(ImplementacionFactura implementacionFactura) {

        this.implementacionFactura = implementacionFactura;
    }
}
