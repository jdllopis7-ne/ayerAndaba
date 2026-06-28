package tuti.desi.presentacion.publicaciones;

import java.math.BigDecimal;

import tuti.desi.entidades.EstadoPublicacion;

public class PublicacionesBuscarForm {
    private Long propiedadSeleccionada;
    private Long ciudadSeleccionada;
    private EstadoPublicacion estado;
    private BigDecimal precioDesde;
    private BigDecimal precioHasta;

    public Long getPropiedadSeleccionada() { return propiedadSeleccionada; }
    public void setPropiedadSeleccionada(Long propiedadSeleccionada) { this.propiedadSeleccionada = propiedadSeleccionada; }
    public Long getCiudadSeleccionada() { return ciudadSeleccionada; }
    public void setCiudadSeleccionada(Long ciudadSeleccionada) { this.ciudadSeleccionada = ciudadSeleccionada; }
    public EstadoPublicacion getEstado() { return estado; }
    public void setEstado(EstadoPublicacion estado) { this.estado = estado; }
    public BigDecimal getPrecioDesde() { return precioDesde; }
    public void setPrecioDesde(BigDecimal precioDesde) { this.precioDesde = precioDesde; }
    public BigDecimal getPrecioHasta() { return precioHasta; }
    public void setPrecioHasta(BigDecimal precioHasta) { this.precioHasta = precioHasta; }
}
