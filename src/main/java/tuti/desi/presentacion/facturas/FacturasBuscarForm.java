package tuti.desi.presentacion.facturas;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import tuti.desi.entidades.EstadoFactura;

public class FacturasBuscarForm {
    private Long contratoSeleccionado;
    private Long propiedadSeleccionada;
    private Long inquilinoSeleccionado;
    private EstadoFactura estado;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate vencimientoDesde;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate vencimientoHasta;

    public Long getContratoSeleccionado() { return contratoSeleccionado; }
    public void setContratoSeleccionado(Long contratoSeleccionado) { this.contratoSeleccionado = contratoSeleccionado; }
    public Long getPropiedadSeleccionada() { return propiedadSeleccionada; }
    public void setPropiedadSeleccionada(Long propiedadSeleccionada) { this.propiedadSeleccionada = propiedadSeleccionada; }
    public Long getInquilinoSeleccionado() { return inquilinoSeleccionado; }
    public void setInquilinoSeleccionado(Long inquilinoSeleccionado) { this.inquilinoSeleccionado = inquilinoSeleccionado; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    public LocalDate getVencimientoDesde() { return vencimientoDesde; }
    public void setVencimientoDesde(LocalDate vencimientoDesde) { this.vencimientoDesde = vencimientoDesde; }
    public LocalDate getVencimientoHasta() { return vencimientoHasta; }
    public void setVencimientoHasta(LocalDate vencimientoHasta) { this.vencimientoHasta = vencimientoHasta; }
}
