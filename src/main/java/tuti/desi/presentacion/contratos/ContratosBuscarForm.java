package tuti.desi.presentacion.contratos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import tuti.desi.entidades.EstadoContrato;

public class ContratosBuscarForm {
    private Long propiedadSeleccionada;
    private Long inquilinoSeleccionado;
    private EstadoContrato estado;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    public Long getPropiedadSeleccionada() { return propiedadSeleccionada; }
    public void setPropiedadSeleccionada(Long propiedadSeleccionada) { this.propiedadSeleccionada = propiedadSeleccionada; }
    public Long getInquilinoSeleccionado() { return inquilinoSeleccionado; }
    public void setInquilinoSeleccionado(Long inquilinoSeleccionado) { this.inquilinoSeleccionado = inquilinoSeleccionado; }
    public EstadoContrato getEstado() { return estado; }
    public void setEstado(EstadoContrato estado) { this.estado = estado; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
}
