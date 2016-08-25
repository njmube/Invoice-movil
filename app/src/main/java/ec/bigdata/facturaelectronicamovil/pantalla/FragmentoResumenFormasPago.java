package ec.bigdata.facturaelectronicamovil.pantalla;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.factura.Pagos;
import ec.bigdata.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.AdaptadorFormasPago;

/**
 * Created by DavidLeonardo on 21/8/2016.
 */
public class FragmentoResumenFormasPago extends Fragment {

    private ListView listViewFormasPago;
    private ImplementacionFactura implementacionFactura;

    public FragmentoResumenFormasPago() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resumen_formas_pago_fragment, container, false);
        listViewFormasPago = (ListView) view.findViewById(R.id.list_view_formas_pago_factura);
//Cabecera
        ViewGroup headerView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.cabecera_list_view_formas_pago, listViewFormasPago, false);

        listViewFormasPago.addHeaderView(headerView, null, false);

        //Se carga la vista vacía.

        View viewVistaVacia = view.findViewById(R.id.text_view_vista_vacia);
        List<Pagos> pagosList = implementacionFactura.getInformacionFactura().getPagos();
        if (pagosList == null || pagosList.isEmpty()) {
            pagosList = new ArrayList<>();
        }
        AdaptadorFormasPago adaptadorFormasPago = new AdaptadorFormasPago(getActivity(), pagosList);
        listViewFormasPago.setAdapter(adaptadorFormasPago);

        listViewFormasPago.setEmptyView(viewVistaVacia);

        return view;
    }

    public static FragmentoResumenFormasPago newInstance(ImplementacionFactura implementacionFactura) {
        FragmentoResumenFormasPago fragmentoResumenFormasPago = new FragmentoResumenFormasPago();

        Bundle args = new Bundle();
        args.putSerializable("factura", implementacionFactura);

        fragmentoResumenFormasPago.setArguments(args);
        return fragmentoResumenFormasPago;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            implementacionFactura = (ImplementacionFactura) getArguments().getSerializable("factura");
        }
    }

}
