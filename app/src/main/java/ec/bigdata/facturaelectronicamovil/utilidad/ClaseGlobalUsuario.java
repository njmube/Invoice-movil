package ec.bigdata.facturaelectronicamovil.utilidad;

import android.app.Application;

import java.util.LinkedHashMap;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.modelo.Cliente;
import ec.bigdata.facturaelectronicamovil.modelo.Producto;
import ec.bigdata.facturaelectronicamovil.modelo.Secuencial;

/**
 * Created by DavidLeonardo on 21/3/2016.
 */
public class ClaseGlobalUsuario extends Application {

    //Atributos para manejo de información de personas
    private String idUsuario;
    private String identificacionUsuario;

    private String nombres;
    private String apellidos;
    private String correoAdicional;
    private String telefonoAdicional;

    //Atributos para manejo de información de empresas
    private String idEmpresa;
    private String razonSocial;
    private String nombreComercial;
    private String direccionMatriz;
    private Boolean obligadoLlevarContabilidad;
    private String numeroResolucion;

    //Atributos compartidos
    private String nombreUsuario;
    private String claveUsuario;
    private String correoPrincipal;
    private String telefonoPrincipal;
    private String idPerfil;
    private String tipoPerfil;
    private String tipoUsuario;

    private Cliente clienteAFacturar;
    private Producto productoAFacturar;
    private Secuencial secuencialAFacturar;

    //Ambiente de la aplicación Pruebas=1;Producción=2
    private String ambiente;

    //Tipo de emisión de la aplicación: Normal=1;Contingencia=2
    private String tipoEmision;

    //Tipos de comprobantes
    private LinkedHashMap<String, String> tiposComprobantes;

    //Impuestos

    private LinkedHashMap<String, String> impuestos;

    //Lista immpuestos por detalle
    private List<ImpuestoComprobanteElectronico> impuestoComprobanteElectronicoList;

    //Moneda
    private String moneda;

    public ClaseGlobalUsuario() {

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdentificacionUsuario() {
        return identificacionUsuario;
    }

    public void setIdentificacionUsuario(String identificacionUsuario) {
        this.identificacionUsuario = identificacionUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoAdicional() {
        return correoAdicional;
    }

    public void setCorreoAdicional(String correoAdicional) {
        this.correoAdicional = correoAdicional;
    }

    public String getTelefonoAdicional() {
        return telefonoAdicional;
    }

    public void setTelefonoAdicional(String telefonoAdicional) {
        this.telefonoAdicional = telefonoAdicional;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getDireccionMatriz() {
        return direccionMatriz;
    }

    public void setDireccionMatriz(String direccionMatriz) {
        this.direccionMatriz = direccionMatriz;
    }

    public Boolean isObligadoLlevarContabilidad() {
        return obligadoLlevarContabilidad;
    }

    public void setObligadoLlevarContabilidad(Boolean obligadoLlevarContabilidad) {
        this.obligadoLlevarContabilidad = obligadoLlevarContabilidad;
    }

    public String getNumeroResolucion() {
        return numeroResolucion;
    }

    public void setNumeroResolucion(String numeroResolucion) {
        this.numeroResolucion = numeroResolucion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public String getCorreoPrincipal() {
        return correoPrincipal;
    }

    public void setCorreoPrincipal(String correoPrincipal) {
        this.correoPrincipal = correoPrincipal;
    }

    public String getTelefonoPrincipal() {
        return telefonoPrincipal;
    }

    public void setTelefonoPrincipal(String telefonoPrincipal) {
        this.telefonoPrincipal = telefonoPrincipal;
    }

    public String getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(String idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(String tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Cliente getClienteAFacturar() {
        return clienteAFacturar;
    }

    public void setClienteAFacturar(Cliente clienteAFacturar) {
        this.clienteAFacturar = clienteAFacturar;
    }

    public Producto getProductoAFacturar() {
        return productoAFacturar;
    }

    public void setProductoAFacturar(Producto productoAFacturar) {
        this.productoAFacturar = productoAFacturar;
    }

    public Secuencial getSecuencialAFacturar() {
        return secuencialAFacturar;
    }

    public void setSecuencialAFacturar(Secuencial secuencialAFacturar) {
        this.secuencialAFacturar = secuencialAFacturar;
    }

    public LinkedHashMap<String, String> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(LinkedHashMap<String, String> impuestos) {
        this.impuestos = impuestos;
    }

    public List<ImpuestoComprobanteElectronico> getImpuestoComprobanteElectronicoList() {
        return impuestoComprobanteElectronicoList;
    }

    public void setImpuestoComprobanteElectronicoList(List<ImpuestoComprobanteElectronico> impuestoComprobanteElectronicoList) {
        this.impuestoComprobanteElectronicoList = impuestoComprobanteElectronicoList;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public LinkedHashMap<String, String> getTiposComprobantes() {
        return tiposComprobantes;
    }

    public void setTiposComprobantes(LinkedHashMap<String, String> tiposComprobantes) {
        this.tiposComprobantes = tiposComprobantes;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
