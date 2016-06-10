package ec.bigdata.facturaelectronicamovil.modelo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DavidLeonardo on 27/4/2016.
 */
public class Recurso implements java.io.Serializable {


    private Integer idRecurso;
    private Recurso recurso;
    private String nombreRecurso;
    private String descripcionRecurso;
    private Boolean pestaniaRecurso;
    private String paginaRecurso;
    private String icono;
    private Set recursos = new HashSet(0);
    private Set permisos = new HashSet(0);

    public Recurso() {
    }

    public Recurso(Recurso recurso, String nombreRecurso, String descripcionRecurso, Boolean pestaniaRecurso, String paginaRecurso, String icono, Set recursos, Set permisos) {
        this.recurso = recurso;
        this.nombreRecurso = nombreRecurso;
        this.descripcionRecurso = descripcionRecurso;
        this.pestaniaRecurso = pestaniaRecurso;
        this.paginaRecurso = paginaRecurso;
        this.icono = icono;
        this.recursos = recursos;
        this.permisos = permisos;
    }

    public Integer getIdRecurso() {
        return this.idRecurso;
    }

    public void setIdRecurso(Integer idRecurso) {
        this.idRecurso = idRecurso;
    }

    public Recurso getRecurso() {
        return this.recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public String getNombreRecurso() {
        return this.nombreRecurso;
    }

    public void setNombreRecurso(String nombreRecurso) {
        this.nombreRecurso = nombreRecurso;
    }

    public String getDescripcionRecurso() {
        return this.descripcionRecurso;
    }

    public void setDescripcionRecurso(String descripcionRecurso) {
        this.descripcionRecurso = descripcionRecurso;
    }

    public Boolean getPestaniaRecurso() {
        return this.pestaniaRecurso;
    }

    public void setPestaniaRecurso(Boolean pestaniaRecurso) {
        this.pestaniaRecurso = pestaniaRecurso;
    }

    public String getPaginaRecurso() {
        return this.paginaRecurso;
    }

    public void setPaginaRecurso(String paginaRecurso) {
        this.paginaRecurso = paginaRecurso;
    }

    public String getIcono() {
        return this.icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Set getRecursos() {
        return this.recursos;
    }

    public void setRecursos(Set recursos) {
        this.recursos = recursos;
    }

    public Set getPermisos() {
        return this.permisos;
    }

    public void setPermisos(Set permisos) {
        this.permisos = permisos;
    }

}
