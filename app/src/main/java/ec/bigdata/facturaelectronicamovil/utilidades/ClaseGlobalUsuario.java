package ec.bigdata.facturaelectronicamovil.utilidades;

import android.app.Application;

import java.util.LinkedHashMap;

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
    private boolean obligadoLlevarContabilidad;
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

    //Impuestos

    private LinkedHashMap<String, String> impuestos;

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

    public boolean isObligadoLlevarContabilidad() {
        return obligadoLlevarContabilidad;
    }

    public void setObligadoLlevarContabilidad(boolean obligadoLlevarContabilidad) {
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
}
