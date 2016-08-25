package ec.bigdata.facturaelectronicamovil.pantalla;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ec.bigdata.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import ec.bigdata.facturaelectronicamovil.R;

public class FragmentoResumenEmisorReceptorComprobante extends Fragment {

    private TextView textViewNumeroComprobante;

    private TextView textViewEmisorComprobante;

    private TextView textViewReceptorComprobante;

    private ImplementacionFactura implementacionFactura;

    public FragmentoResumenEmisorReceptorComprobante() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resumen_emisor_receptor_comprobante_fragment, container, false);
        textViewNumeroComprobante = (TextView) view.findViewById(R.id.text_view_valor_numero_comprobante);
        textViewEmisorComprobante = (TextView) view.findViewById(R.id.text_view_valor_emisor_comprobante);
        textViewReceptorComprobante = (TextView) view.findViewById(R.id.text_view_valor_receptor_comprobante);

        textViewNumeroComprobante.setText(implementacionFactura.getInformacionTributariaComprobanteElectronico().getCodigoEstablecimiento() + "-" + implementacionFactura.getInformacionTributariaComprobanteElectronico().getPuntoEmision()
                + "-" + implementacionFactura.getInformacionTributariaComprobanteElectronico().getSecuencial());
        textViewEmisorComprobante.setText(implementacionFactura.getInformacionTributariaComprobanteElectronico().getRuc() + "-" + implementacionFactura.getInformacionTributariaComprobanteElectronico().getRazonSocial());
        textViewReceptorComprobante.setText(implementacionFactura.getInformacionFactura().getIdentificacionComprador() + "-" + implementacionFactura.getInformacionFactura().getRazonSocialComprador());


        return view;
    }

    public static FragmentoResumenEmisorReceptorComprobante newInstance(ImplementacionFactura implementacionFactura) {
        FragmentoResumenEmisorReceptorComprobante fragmentoResumenEmisorReceptorComprobante = new FragmentoResumenEmisorReceptorComprobante();

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
