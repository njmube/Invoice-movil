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

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterInformacionAdicional;

/**
 * Created by DavidLeonardo on 21/8/2016.
 */
public class FragmentoResumenInformacionAdicional extends Fragment {

    private ListView listViewInformacionAdicional;

    private List<InformacionAdicional> informacionAdicionalList;

    public FragmentoResumenInformacionAdicional() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resumen_informacion_adicional_fragment, container, false);
        listViewInformacionAdicional = (ListView) view.findViewById(R.id.list_view_resumen_informacion_adicional);


//Cabecera
        ViewGroup headerView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.cabecera_list_view_informacion_adicional, listViewInformacionAdicional, false);

        listViewInformacionAdicional.addHeaderView(headerView, null, false);

        if (informacionAdicionalList == null) {
            informacionAdicionalList = new ArrayList<>();
        }

        View viewVistaVacia = view.findViewById(R.id.text_view_vista_vacia);

        ArrayAdapterInformacionAdicional arrayAdapterInformacionAdicional = new ArrayAdapterInformacionAdicional(getActivity(), informacionAdicionalList);
        listViewInformacionAdicional.setAdapter(arrayAdapterInformacionAdicional);

        //Se carga la vista vacía.
        listViewInformacionAdicional.setEmptyView(viewVistaVacia);

        return view;
    }

    public static FragmentoResumenInformacionAdicional newInstance(ArrayList<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> informacionAdicional) {
        FragmentoResumenInformacionAdicional fragmentoResumenInformacionAdicional = new FragmentoResumenInformacionAdicional();

        Bundle args = new Bundle();
        args.putSerializable("infoAdicional", informacionAdicional);

        fragmentoResumenInformacionAdicional.setArguments(args);
        return fragmentoResumenInformacionAdicional;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            informacionAdicionalList = (ArrayList<InformacionAdicional>) getArguments().getSerializable("infoAdicional");
        }
    }
}
