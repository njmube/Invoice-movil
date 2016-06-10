package ec.bigdata.facturaelectronicamovil.utilidades;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by DavidLeonardo on 5/6/2016.
 */
public class Calculos {

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


}
