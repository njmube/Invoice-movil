package ec.bigdata.facturaelectronicamovil.pantalla;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.factura.Detalle;
import ec.bigdata.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ExpandableDetalleAdapter;

public class FragmentoResumenDetallesComprobante extends Fragment {

    private ExpandableListView expandableListViewDetallesFactura;

    private ImplementacionFactura implementacionFactura;

    public FragmentoResumenDetallesComprobante() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resumen_detalles_comprobante_fragment, container, false);
        expandableListViewDetallesFactura = (ExpandableListView) view.findViewById(R.id.expandable_list_view_detalles_factura);
        List<Detalle> detallesFactura = implementacionFactura.getDetalles();

        //Cabecera
        ViewGroup headerView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.cabecera_expandable_list_view_detalles, expandableListViewDetallesFactura, false);

        expandableListViewDetallesFactura.addHeaderView(headerView, null, false);

        ExpandableDetalleAdapter expandableDetalleAdapter = new ExpandableDetalleAdapter(detallesFactura, getActivity().getApplicationContext());
        expandableListViewDetallesFactura.setAdapter(expandableDetalleAdapter);
        return view;
    }

    public static FragmentoResumenDetallesComprobante newInstance(ImplementacionFactura implementacionFactura) {
        FragmentoResumenDetallesComprobante fragmentoResumenEmisorReceptorComprobante = new FragmentoResumenDetallesComprobante();

        Bundle args = new Bundle();
        args.putSerializable("factura", implementacionFactura);

        fragmentoResumenEmisorReceptorComprobante.setArguments(args);
        return fragmentoResumenEmisorReceptorComprobante;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            implementacionFactura = (ImplementacionFactura) getArguments().getSerializable("factura");
        }
    }
}
