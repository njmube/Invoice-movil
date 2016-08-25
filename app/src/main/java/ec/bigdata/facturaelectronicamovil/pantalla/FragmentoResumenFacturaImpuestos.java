package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.comprobanteelectronico.esquema.factura.InformacionFactura;
import ec.bigdata.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.facturacion.Calculos;
import ec.bigdata.facturaelectronicamovil.interfaz.ResumenFacturaComunicacionFragmentInterface;

/**
 * Created by DavidLeonardo on 22/8/2016.
 */
public class FragmentoResumenFacturaImpuestos extends Fragment {

    private TextView textViewSubTotal12;

    private TextView textViewSubTotal14;

    private TextView textViewSubTotal0;

    private TextView textViewSubTotalNoObjetoIVA;

    private TextView textViewSubTotalExentoIVA;

    private TextView textViewSubTotalSinImpuestos;

    private TextView textViewTotalDescuento;

    private TextView textViewICE;

    private TextView textViewIVA12;

    private TextView textViewIVA14;

    private TextView textViewIRBPNR;

    private Switch switchPropina;

    private TextView textViewValorPropina;

    private TextView textViewValorTotal;

    private ImplementacionFactura implementacionFactura;

    private InformacionFactura informacionFactura;

    private ResumenFacturaComunicacionFragmentInterface resumenFacturaComunicacionFragmentInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resumen_factura_electronica_impuestos_fragment, container, false);
        textViewSubTotal12 = (TextView) view.findViewById(R.id.text_view_valor_subtotal_12);

        textViewSubTotal14 = (TextView) view.findViewById(R.id.text_view_valor_subtotal_14);

        textViewSubTotal0 = (TextView) view.findViewById(R.id.text_view_valor_subtotal_0);

        textViewSubTotalNoObjetoIVA = (TextView) view.findViewById(R.id.text_view_valor_subtototal_no_objeto_iva);

        textViewSubTotalExentoIVA = (TextView) view.findViewById(R.id.text_view_valor_subtototal_exento_iva);

        textViewSubTotalSinImpuestos = (TextView) view.findViewById(R.id.text_view_valor_subtototal_sin_impuestos);

        textViewTotalDescuento = (TextView) view.findViewById(R.id.text_view_valor_total_descuento);

        textViewICE = (TextView) view.findViewById(R.id.text_view_valor_ice);

        textViewIVA12 = (TextView) view.findViewById(R.id.text_view_valor_iva_12);

        textViewIVA14 = (TextView) view.findViewById(R.id.text_view_valor_iva_14);

        textViewIRBPNR = (TextView) view.findViewById(R.id.text_view_valor_irbpnr);

        switchPropina = (Switch) view.findViewById(R.id.switch_propina);

        switchPropina.setTextOn("SI");

        switchPropina.setTextOff("NO");

        textViewValorPropina = (TextView) view.findViewById(R.id.text_view_valor_propina);

        textViewValorTotal = (TextView) view.findViewById(R.id.text_view_valor_importe_total);
        informacionFactura = implementacionFactura.getInformacionFactura();

        textViewSubTotalSinImpuestos.setText(informacionFactura.getTotalSinImpuestos());

        textViewTotalDescuento.setText(informacionFactura.getTotalDescuento());

        textViewValorTotal.setText(informacionFactura.getImporteTotal());

        List<ImpuestoComprobanteElectronico> impuestoComprobanteElectronicoList = informacionFactura.getTotalConImpuesto();
        ImpuestoComprobanteElectronico impuestoComprobante = null;
        for (int i = 0; i < impuestoComprobanteElectronicoList.size(); i++) {
            impuestoComprobante = impuestoComprobanteElectronicoList.get(i);
            if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("0")) {
                textViewSubTotal0.setText(impuestoComprobante.getBaseImponible());
            } else if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("2")) {
                textViewSubTotal12.setText(impuestoComprobante.getBaseImponible());
                textViewIVA12.setText(impuestoComprobante.getValor());
            } else if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("3")) {
                textViewSubTotal14.setText(impuestoComprobante.getBaseImponible());
                textViewIVA14.setText(impuestoComprobante.getValor());
            } else if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("6")) {
                textViewSubTotalNoObjetoIVA.setText(impuestoComprobante.getBaseImponible());

            } else if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("7")) {
                textViewSubTotalExentoIVA.setText(impuestoComprobante.getBaseImponible());

            } else if (impuestoComprobante.getCodigo().equals("3")) {
                textViewICE.setText(impuestoComprobante.getBaseImponible());
            }

        }
        if (informacionFactura.getPropina() != null && !informacionFactura.getPropina().equals("0")) {
            switchPropina.setChecked(true);
            textViewValorPropina.setText(informacionFactura.getPropina());
        }
        switchPropina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BigDecimal valorPropina = BigDecimal.ZERO;
                BigDecimal importeTotal = BigDecimal.ZERO;
                importeTotal = new BigDecimal(informacionFactura.getImporteTotal());
                valorPropina = Calculos.calcularPropina(informacionFactura.getTotalSinImpuestos());
                if (informacionFactura.getPropina() == null || informacionFactura.getPropina().equals("0")) {
                    if (isChecked) {

                        importeTotal = importeTotal.add(valorPropina);
                        informacionFactura.setPropina(valorPropina.toString());
                        informacionFactura.setImporteTotal(importeTotal.toString());
                        implementacionFactura.setInformacionFactura(informacionFactura);
                        textViewValorPropina.setText(valorPropina.toString());
                        textViewValorTotal.setText(importeTotal.toString());

                    } else {
                        importeTotal = importeTotal.subtract(valorPropina);
                        informacionFactura.setPropina("0");
                        informacionFactura.setImporteTotal(importeTotal.toString());
                        implementacionFactura.setInformacionFactura(informacionFactura);
                        textViewValorPropina.setText(getResources().getString(R.string.valor_cero));
                        textViewValorTotal.setText(importeTotal.toString());
                    }
                }
                resumenFacturaComunicacionFragmentInterface.actualizarInformacionFactura(implementacionFactura);
            }
        });

        return view;
    }

    public static FragmentoResumenFacturaImpuestos newInstance(ImplementacionFactura implementacionFactura) {
        FragmentoResumenFacturaImpuestos fragmentoResumenFacturaImpuestos = new FragmentoResumenFacturaImpuestos();

        Bundle args = new Bundle();
        args.putSerializable("factura", implementacionFactura);
        fragmentoResumenFacturaImpuestos.setArguments(args);
        return fragmentoResumenFacturaImpuestos;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            implementacionFactura = (ImplementacionFactura) getArguments().getSerializable("factura");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resumenFacturaComunicacionFragmentInterface = (ResumenFacturaComunicacionFragmentInterface) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        resumenFacturaComunicacionFragmentInterface = null;
    }

}
