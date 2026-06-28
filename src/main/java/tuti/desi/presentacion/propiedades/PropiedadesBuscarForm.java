package tuti.desi.presentacion.propiedades;

import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.TipoPropiedad;

public class PropiedadesBuscarForm {
    private String direccion;
    private Long ciudadSeleccionada;
    private TipoPropiedad tipo;
    private EstadoDisponibilidad estado;

    public String getDireccion() {
        return direccion == null || direccion.isBlank() ? null : direccion.trim();
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Long ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public TipoPropiedad getTipo() {
        return tipo;
    }

    public void setTipo(TipoPropiedad tipo) {
        this.tipo = tipo;
    }

    public EstadoDisponibilidad getEstado() {
        return estado;
    }

    public void setEstado(EstadoDisponibilidad estado) {
        this.estado = estado;
    }
}
