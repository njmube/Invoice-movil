package ec.bigdata.facturaelectronicamovil.facturacion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.comprobanteelectronico.esquema.factura.Detalle;

/**
 * Created by DavidLeonardo on 5/6/2016.
 */
public class Calculos {

    BigDecimal totalIVACero = BigDecimal.ZERO;
    BigDecimal totalIVADoce = BigDecimal.ZERO;
    BigDecimal totalIVACatorce = BigDecimal.ZERO;
    BigDecimal totalIVANoObjetoImpuesto = BigDecimal.ZERO;
    BigDecimal totalIVAExento = BigDecimal.ZERO;
    BigDecimal totalICE = BigDecimal.ZERO;
    BigDecimal totalIRBPNR = BigDecimal.ZERO;
    BigDecimal importeTotal = BigDecimal.ZERO;
    BigDecimal subTotalSinImpuestos = BigDecimal.ZERO;

    public static String obtenerValor(String _valor, String _porcentaje) {
        BigDecimal valor_total_sin_impuestos = new BigDecimal(_valor);
        BigDecimal valor = valor_total_sin_impuestos.multiply(new BigDecimal(_porcentaje).divide(new BigDecimal(100.00))).setScale(2, RoundingMode.HALF_EVEN);
        return valor.toString();
    }

    public static String actualizarPrecioTotal(String cantidad, String precioUnitario, String descuento) {
        BigDecimal bigDecimalCantidad = BigDecimal.ZERO;
        BigDecimal bigDecimalPrecioUnitario = BigDecimal.ZERO;
        if (cantidad != null && !cantidad.equals("")) {
            bigDecimalCantidad = new BigDecimal(cantidad);
        }
        if (precioUnitario != null && !precioUnitario.equals("")) {
            bigDecimalPrecioUnitario = new BigDecimal(precioUnitario);
        }
        BigDecimal bigDecimalValorTotal = BigDecimal.ZERO;
        bigDecimalValorTotal = bigDecimalPrecioUnitario.multiply(bigDecimalCantidad);
        bigDecimalValorTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        if (descuento != null && !descuento.equals("")) {
            BigDecimal bigDecimalDescuento = new BigDecimal(descuento);
            bigDecimalValorTotal = bigDecimalValorTotal.subtract(bigDecimalDescuento);
        }
        return bigDecimalValorTotal.toString();
    }

    public static String calcularValorImpuestoICE(String baseImponible, String porcentaje) {
        BigDecimal bigDecimalBaseImponible = BigDecimal.ZERO;
        BigDecimal bigDecimalPorcentaje = BigDecimal.ZERO;
        BigDecimal bigDecimalValor = BigDecimal.ZERO;
        if (baseImponible != null & !baseImponible.equals("")) {
            bigDecimalBaseImponible = new BigDecimal(baseImponible);
        }
        if (porcentaje != null && !porcentaje.equals("")) {
            bigDecimalPorcentaje = new BigDecimal(porcentaje);
        }
        bigDecimalValor = bigDecimalBaseImponible.multiply(bigDecimalPorcentaje).divide(new BigDecimal(100.00));
        bigDecimalValor = bigDecimalValor.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return bigDecimalValor.toString();
    }

    public static BigDecimal obtenerTotalSinImpuestos(List<Detalle> detalles) {
        BigDecimal totalSinImpuestos = BigDecimal.ZERO;
        if (detalles != null && !detalles.isEmpty()) {

            for (Detalle d : detalles) {

                totalSinImpuestos = totalSinImpuestos.add(new BigDecimal(d.getPrecioTotalSinImpuesto()));

            }
            totalSinImpuestos.setScale(2, RoundingMode.HALF_EVEN);
        }
        return totalSinImpuestos;
    }

    public static BigDecimal obtenerTotalDescuento(List<Detalle> detalles) {
        BigDecimal totalDescuento = BigDecimal.ZERO;
        if (detalles != null && !detalles.isEmpty()) {

            for (Detalle d : detalles) {
                if (d.getDescuento() != null && !d.getDescuento().equals("")) {
                    BigDecimal bdDescuento = new BigDecimal(d.getDescuento());
                    totalDescuento = totalDescuento.add(bdDescuento);
                }
            }
        }
        return totalDescuento;
    }

    public List<ImpuestoComprobanteElectronico> obtenerTotalConImpuestos(List<Detalle> _detalles) {
        List<ImpuestoComprobanteElectronico> totalImpuestos = new ArrayList<ImpuestoComprobanteElectronico>();
        BigDecimal valorIva0 = BigDecimal.ZERO;
        BigDecimal valorIva12 = BigDecimal.ZERO;
        BigDecimal valorIva14 = BigDecimal.ZERO;
        BigDecimal valorIvaNoObjetoImpuesto = BigDecimal.ZERO;
        BigDecimal valorIvaExento = BigDecimal.ZERO;

        BigDecimal porcentajeIVA0 = BigDecimal.ZERO;
        BigDecimal porcentajeIVA12 = BigDecimal.ZERO;
        BigDecimal porcentajeIVA14 = BigDecimal.ZERO;
        BigDecimal porcentajeIvaNoObjetoImpuesto = BigDecimal.ZERO;
        BigDecimal porcentajeExentoIVA = BigDecimal.ZERO;

        BigDecimal valorIce = BigDecimal.ZERO;
        BigDecimal valorIrbpnr = BigDecimal.ZERO;

        String codigoIce = null;
        String codigoIrbpnr = null;

        BigDecimal porcentajeIce = BigDecimal.ZERO;
        BigDecimal porcentajeIrbpnr = BigDecimal.ZERO;

        if (_detalles != null && !_detalles.isEmpty()) {
            for (Detalle d : _detalles) {
                importeTotal = importeTotal.add(new BigDecimal(d.getPrecioTotalSinImpuesto()));
                List<ImpuestoComprobanteElectronico> impuestos_detalle = d.getImpuestos();
                for (ImpuestoComprobanteElectronico ice : impuestos_detalle) {
                    if (ice.getCodigo().equals("2")) {
                        if (ice.getCodigoPorcentaje().equals("0")) {
                            totalIVACero = totalIVACero.add(new BigDecimal(ice.getBaseImponible()));
                            porcentajeIVA0 = new BigDecimal(ice.getTarifa());
                        } else if (ice.getCodigoPorcentaje().equals("2")) {
                            totalIVADoce = totalIVADoce.add(new BigDecimal(ice.getBaseImponible()));
                            porcentajeIVA12 = new BigDecimal(ice.getTarifa());
                        } else if (ice.getCodigoPorcentaje().equals("3")) {
                            totalIVACatorce = totalIVACatorce.add(new BigDecimal(ice.getBaseImponible()));
                            porcentajeIVA14 = new BigDecimal(ice.getTarifa());
                        } else if (ice.getCodigoPorcentaje().equals("6")) {
                            totalIVANoObjetoImpuesto = totalIVANoObjetoImpuesto.add(new BigDecimal(ice.getBaseImponible()));
                            porcentajeIvaNoObjetoImpuesto = new BigDecimal(ice.getTarifa());
                        } else if (ice.getCodigoPorcentaje().equals("7")) {
                            totalIVAExento = totalIVAExento.add(new BigDecimal(ice.getBaseImponible()));
                            porcentajeExentoIVA = new BigDecimal(ice.getTarifa());
                        }

                    } else if (ice.getCodigo().equals("3")) {
                        totalICE = totalICE.add(new BigDecimal(ice.getBaseImponible()));
                        codigoIce = ice.getCodigoPorcentaje();
                        porcentajeIce = new BigDecimal(ice.getTarifa());
                    } else if (ice.getCodigo().equals("5")) {
                        codigoIrbpnr = ice.getCodigoPorcentaje();
                        totalIRBPNR = totalIRBPNR.add(new BigDecimal(ice.getBaseImponible()));
                        porcentajeIrbpnr = new BigDecimal(ice.getTarifa());
                    }
                }
            }
            ImpuestoComprobanteElectronico ice = new ImpuestoComprobanteElectronico();

            if (!totalIVACero.equals(BigDecimal.ZERO)) {

                totalIVACero.setScale(2, RoundingMode.HALF_EVEN);

                ice.setCodigo("2");
                ice.setCodigoPorcentaje("0");
                totalIVACero.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(totalIVACero.toString());

                BigDecimal porcentaje = porcentajeIVA0.divide(new BigDecimal(100.00));
                valorIva0 = totalIVACero.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valorIva0.toString());
                totalImpuestos.add(ice);

            }
            if (!totalIVADoce.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();
                totalIVADoce.setScale(2, RoundingMode.HALF_EVEN);

                ice.setCodigo("2");
                ice.setCodigoPorcentaje("2");
                totalIVADoce.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(totalIVADoce.toString());

                BigDecimal porcentaje = porcentajeIVA12.divide(new BigDecimal(100.00));
                valorIva12 = totalIVADoce.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valorIva12.toString());
                totalImpuestos.add(ice);

            }

            if (!totalIVACatorce.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();

                totalIVACatorce.setScale(2, RoundingMode.HALF_EVEN);

                ice.setCodigo("2");
                ice.setCodigoPorcentaje("3");
                totalIVACatorce.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(totalIVACatorce.toString());

                BigDecimal porcentaje = porcentajeIVA14.divide(new BigDecimal(100.00));
                valorIva14 = totalIVACatorce.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valorIva14.toString());
                totalImpuestos.add(ice);

            }

            if (!totalIVANoObjetoImpuesto.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();
                totalIVANoObjetoImpuesto.setScale(2, RoundingMode.HALF_EVEN);

                ice.setCodigo("2");
                ice.setCodigoPorcentaje("6");
                totalIVANoObjetoImpuesto.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(totalIVANoObjetoImpuesto.toString());

                BigDecimal porcentaje = porcentajeIvaNoObjetoImpuesto.divide(new BigDecimal(100.00));
                valorIvaNoObjetoImpuesto = totalIVANoObjetoImpuesto.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valorIvaNoObjetoImpuesto.toString());
                totalImpuestos.add(ice);

            }
            if (!totalIVAExento.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();
                totalIVAExento.setScale(2, RoundingMode.HALF_EVEN);

                ice.setCodigo("2");
                ice.setCodigoPorcentaje("7");
                totalIVAExento.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(totalIVAExento.toString());

                BigDecimal porcentaje = porcentajeExentoIVA.divide(new BigDecimal(100.00));
                valorIvaExento = totalIVAExento.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valorIvaExento.toString());
                totalImpuestos.add(ice);

            }
            if (!totalIRBPNR.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();
                totalIRBPNR.setScale(2, RoundingMode.HALF_EVEN);

                ice.setCodigo("5");
                ice.setCodigoPorcentaje(codigoIrbpnr);
                totalIRBPNR.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(totalIRBPNR.toString());

                BigDecimal porcentaje = porcentajeIrbpnr.divide(new BigDecimal(100.00));
                valorIrbpnr = totalIRBPNR.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valorIrbpnr.toString());
                totalImpuestos.add(ice);
            }
            if (!totalICE.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();
                totalICE.setScale(2, RoundingMode.HALF_EVEN);

                ice.setCodigo("3");
                ice.setCodigoPorcentaje(codigoIce);
                totalICE.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(totalICE.toString());

                BigDecimal porcentaje = porcentajeIce.divide(new BigDecimal(100.00));
                valorIce = totalICE.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valorIce.toString());
                totalImpuestos.add(ice);
            }
            importeTotal = importeTotal.add(valorIva0).add(valorIva12).add(valorIva14).add(valorIvaExento).add(valorIvaNoObjetoImpuesto).add(valorIce).add(valorIrbpnr);

        }
        return totalImpuestos;

    }

    public static BigDecimal calcularPropina(String totalSinImpuestos) {
        BigDecimal bigDecimalPropina = BigDecimal.ZERO;
        BigDecimal bigDecimalTotalSinImpuestos = BigDecimal.ZERO;
        bigDecimalTotalSinImpuestos = new BigDecimal(totalSinImpuestos);
        bigDecimalPropina = bigDecimalTotalSinImpuestos.multiply(new BigDecimal(10.00)).divide(new BigDecimal(100.00));
        bigDecimalPropina = bigDecimalPropina.setScale(2, RoundingMode.HALF_EVEN);
        return bigDecimalPropina;
    }

    public BigDecimal getTotalIVACero() {
        return totalIVACero;
    }

    public void setTotalIVACero(BigDecimal totalIVACero) {
        this.totalIVACero = totalIVACero;
    }

    public BigDecimal getTotalIVADoce() {
        return totalIVADoce;
    }

    public void setTotalIVADoce(BigDecimal totalIVADoce) {
        this.totalIVADoce = totalIVADoce;
    }

    public BigDecimal getTotalIVACatorce() {
        return totalIVACatorce;
    }

    public void setTotalIVACatorce(BigDecimal totalIVACatorce) {
        this.totalIVACatorce = totalIVACatorce;
    }

    public BigDecimal getTotalIVANoObjetoImpuesto() {
        return totalIVANoObjetoImpuesto;
    }

    public void setTotalIVANoObjetoImpuesto(BigDecimal totalIVANoObjetoImpuesto) {
        this.totalIVANoObjetoImpuesto = totalIVANoObjetoImpuesto;
    }

    public BigDecimal getTotalIVAExento() {
        return totalIVAExento;
    }

    public void setTotalIVAExento(BigDecimal totalIVAExento) {
        this.totalIVAExento = totalIVAExento;
    }

    public BigDecimal getTotalICE() {
        return totalICE;
    }

    public void setTotalICE(BigDecimal totalICE) {
        this.totalICE = totalICE;
    }

    public BigDecimal getTotalIRBPNR() {
        return totalIRBPNR;
    }

    public void setTotalIRBPNR(BigDecimal totalIRBPNR) {
        this.totalIRBPNR = totalIRBPNR;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }
}
