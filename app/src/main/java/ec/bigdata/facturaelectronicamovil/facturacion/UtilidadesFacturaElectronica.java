package ec.bigdata.facturaelectronicamovil.facturacion;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ec.bigdata.claveAcceso.ClaveAcceso;
import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionTributariaComprobanteElectronico;
import ec.bigdata.comprobanteelectronico.esquema.factura.Detalle;
import ec.bigdata.comprobanteelectronico.esquema.factura.InformacionFactura;
import ec.bigdata.comprobanteelectronico.esquema.factura.Pagos;
import ec.bigdata.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import ec.bigdata.facturaelectronicamovil.modelo.Cliente;
import ec.bigdata.facturaelectronicamovil.modelo.Secuencial;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.utilidades.Validaciones;

/**
 * Created by DavidLeonardo on 21/6/2016.
 */
public class UtilidadesFacturaElectronica {

    private ClaseGlobalUsuario claseGlobalUsuario;

    private Secuencial secuencialSeleccionado;

    private Cliente clienteSeleccionado;

    private ArrayList<Pagos> pagosArrayList;

    private ArrayList<InformacionAdicional> informacionAdicionalArrayList;

    public UtilidadesFacturaElectronica(ClaseGlobalUsuario claseGlobalUsuario, Secuencial secuencialSeleccionado, Cliente clienteSeleccionado, ArrayList<Pagos> pagosArrayList, ArrayList<InformacionAdicional> informacionAdicionalArrayList) {
        this.claseGlobalUsuario = claseGlobalUsuario;
        this.secuencialSeleccionado = secuencialSeleccionado;
        this.clienteSeleccionado = clienteSeleccionado;
        this.pagosArrayList = pagosArrayList;
        this.informacionAdicionalArrayList = informacionAdicionalArrayList;
    }

    /**
     * Información Tributaria para generar la factura electrónica
     *
     * @return Objeto de tipo InformacionTributariaComprobanteElectronico
     */
    private InformacionTributariaComprobanteElectronico cargarInformacionTributaria() {

        InformacionTributariaComprobanteElectronico informacionTributaria = new InformacionTributariaComprobanteElectronico();
        informacionTributaria.setAmbiente(claseGlobalUsuario.getAmbiente());
        informacionTributaria.setCodDoc(claseGlobalUsuario.getTiposComprobantes().get("FACTURA"));
        informacionTributaria.setDirMatriz(claseGlobalUsuario.getDireccionMatriz());
        informacionTributaria.setRazonSocial(claseGlobalUsuario.getRazonSocial());
        informacionTributaria.setCodigoEstablecimiento(secuencialSeleccionado.getCodigoEstablecimientoSecuencial());
        informacionTributaria.setPuntoEmision(secuencialSeleccionado.getPuntoEmisionSecuencial());
        informacionTributaria.setSecuencial(Validaciones.completarSecuencial(secuencialSeleccionado.getSecuencialFacturaSecuencial()));
        informacionTributaria.setNombreComercial(claseGlobalUsuario.getNombreComercial());
        informacionTributaria.setRuc(claseGlobalUsuario.getIdEmpresa());
        informacionTributaria.setTipoEmision(claseGlobalUsuario.getTipoEmision());

        return informacionTributaria;
    }

    /**
     * Información de la factura
     *
     * @param detallesFactura Lista de detalles
     * @return Objeto de tipo InformacionFactura
     */
    private InformacionFactura cargarInformacionFactura(ArrayList<Detalle> detallesFactura) {
        Calculos calculos = new Calculos();
        InformacionFactura informacionFactura = new InformacionFactura();
        informacionFactura.setFechaEmision(ec.bigdata.utilidades.Utilidades.obtenerFechaFormatoddMMyyyy(new Date()));
        informacionFactura.setDirEstablecimiento(secuencialSeleccionado.getDireccionSecuencial());
        if (claseGlobalUsuario.getNumeroResolucion() != null && !claseGlobalUsuario.getNumeroResolucion().equals("")) {
            informacionFactura.setContribuyenteEspecial(claseGlobalUsuario.getNumeroResolucion());
        }
        informacionFactura.setObligadoContabilidad(Validaciones.obtenerObligadoContabilidad(claseGlobalUsuario.isObligadoLlevarContabilidad().toString()));

        informacionFactura.setTipoIdentificacionComprador(Validaciones.comprTipoId(clienteSeleccionado.getIdentificacionCliente()));
        informacionFactura.setRazonSocialComprador(clienteSeleccionado.getRazonSocialCliente());
        informacionFactura.setIdentificacionComprador(clienteSeleccionado.getIdentificacionCliente());
        if (clienteSeleccionado.getDireccionCliente() != null && !clienteSeleccionado.getDireccionCliente().equals("")) {
            informacionFactura.setDireccionComprador(clienteSeleccionado.getDireccionCliente());
        }
        if (pagosArrayList != null && !pagosArrayList.isEmpty()) {
            informacionFactura.setPagos(pagosArrayList);
        }

        informacionFactura.setTotalSinImpuestos(Calculos.obtenerTotalSinImpuestos(detallesFactura).toString());
        informacionFactura.setTotalDescuento(Calculos.obtenerTotalDescuento(detallesFactura).toString());

        List<ImpuestoComprobanteElectronico> totalConImpuestos = calculos.obtenerTotalConImpuestos(detallesFactura);
        informacionFactura.setTotalConImpuesto(totalConImpuestos);
        informacionFactura.setImporteTotal(calculos.getImporteTotal().toString());
        informacionFactura.setPropina("0");
        informacionFactura.setMoneda(claseGlobalUsuario.getMoneda());

        return informacionFactura;
    }

    public ImplementacionFactura generarFacturaElectronica(ArrayList<Detalle> detalles) {
        ClaveAcceso claveAcceso = new ClaveAcceso();
        ImplementacionFactura facturaElectronica = new ImplementacionFactura();

        InformacionTributariaComprobanteElectronico informacionTributariaComprobanteElectronico = this.cargarInformacionTributaria();

        InformacionFactura informacionFactura = this.cargarInformacionFactura(detalles);

        facturaElectronica.setInformacionTributariaComprobanteElectronico(informacionTributariaComprobanteElectronico);
        facturaElectronica.setInformacionFactura(informacionFactura);
        facturaElectronica.setDetalles(detalles);
        facturaElectronica.setInformacionAdicional(informacionAdicionalArrayList);
        informacionTributariaComprobanteElectronico.setClaveAcceso(claveAcceso.obtenerClaveDeAcceso(facturaElectronica, RandomStringUtils.randomNumeric(8)));
        facturaElectronica.setInformacionTributariaComprobanteElectronico(informacionTributariaComprobanteElectronico);
        return facturaElectronica;
    }


}
