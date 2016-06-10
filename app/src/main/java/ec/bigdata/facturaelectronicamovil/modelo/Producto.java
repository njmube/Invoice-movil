package ec.bigdata.facturaelectronicamovil.modelo;

import java.io.Serializable;

/**
 * Created by DavidLeonardo on 20/5/2016.
 */
public class Producto implements Serializable {
    private Integer idProducto;
    private ClienteEmpresa clienteEmpresa;
    private String codigoPrincipalProducto;
    private String codigoAuxiliarProducto;
    private String descripcionProducto;
    private String precioUnitarioProducto;

    public Producto() {
    }

    public Producto(ClienteEmpresa clienteEmpresa, String codigoPrincipalProducto, String descripcionProducto) {
        this.clienteEmpresa = clienteEmpresa;
        this.codigoPrincipalProducto = codigoPrincipalProducto;
        this.descripcionProducto = descripcionProducto;
    }

    public Producto(ClienteEmpresa clienteEmpresa, String codigoPrincipalProducto, String codigoAuxiliarProducto, String descripcionProducto, String precioUnitarioProducto) {
        this.clienteEmpresa = clienteEmpresa;
        this.codigoPrincipalProducto = codigoPrincipalProducto;
        this.codigoAuxiliarProducto = codigoAuxiliarProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioUnitarioProducto = precioUnitarioProducto;
    }

    public Integer getIdProducto() {
        return this.idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public ClienteEmpresa getClienteEmpresa() {
        return this.clienteEmpresa;
    }

    public void setClienteEmpresa(ClienteEmpresa clienteEmpresa) {
        this.clienteEmpresa = clienteEmpresa;
    }

    public String getCodigoPrincipalProducto() {
        return this.codigoPrincipalProducto;
    }

    public void setCodigoPrincipalProducto(String codigoPrincipalProducto) {
        this.codigoPrincipalProducto = codigoPrincipalProducto;
    }

    public String getCodigoAuxiliarProducto() {
        return this.codigoAuxiliarProducto;
    }

    public void setCodigoAuxiliarProducto(String codigoAuxiliarProducto) {
        this.codigoAuxiliarProducto = codigoAuxiliarProducto;
    }

    public String getDescripcionProducto() {
        return this.descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getPrecioUnitarioProducto() {
        return this.precioUnitarioProducto;
    }

    public void setPrecioUnitarioProducto(String precioUnitarioProducto) {
        this.precioUnitarioProducto = precioUnitarioProducto;
    }


}
