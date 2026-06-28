package tuti.desi.presentacion.contratos;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;

public class ContratoForm {
    private Long id;

    @NotNull(message = "La propiedad es obligatoria")
    private Long idPropiedad;

    @NotNull(message = "El inquilino es obligatorio")
    private Long idInquilino;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @NotNull(message = "La duracion es obligatoria")
    @Min(value = 1, message = "La duracion debe ser positiva")
    private Integer duracionMeses;

    @NotNull(message = "El importe mensual es obligatorio")
    @DecimalMin(value = "0.01", message = "El importe mensual debe ser positivo")
    private BigDecimal importeMensual;

    @NotNull(message = "El dia de vencimiento es obligatorio")
    @Min(value = 1, message = "El dia de vencimiento debe ser entre 1 y 31")
    @Max(value = 31, message = "El dia de vencimiento debe ser entre 1 y 31")
    private Integer diaVencimientoMensual;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotNull(message = "El estado es obligatorio")
    private EstadoContrato estado = EstadoContrato.BORRADOR;

    public ContratoForm() {
        fechaInicio = LocalDate.now();
    }

    public ContratoForm(Contrato contrato) {
        this.id = contrato.getId();
        this.idPropiedad = contrato.getPropiedad().getId();
        this.idInquilino = contrato.getInquilino().getId();
        this.fechaInicio = contrato.getFechaInicio();
        this.duracionMeses = contrato.getDuracionMeses();
        this.importeMensual = contrato.getImporteMensual();
        this.diaVencimientoMensual = contrato.getDiaVencimientoMensual();
        this.descripcion = contrato.getDescripcion();
        this.estado = contrato.getEstado();
    }

    public Contrato toPojo() {
        Contrato contrato = new Contrato();
        contrato.setId(id);
        contrato.setFechaInicio(fechaInicio);
        contrato.setDuracionMeses(duracionMeses);
        contrato.setImporteMensual(importeMensual);
        contrato.setDiaVencimientoMensual(diaVencimientoMensual);
        contrato.setDescripcion(descripcion == null ? null : descripcion.trim());
        contrato.setEstado(estado);
        return contrato;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Long idPropiedad) { this.idPropiedad = idPropiedad; }
    public Long getIdInquilino() { return idInquilino; }
    public void setIdInquilino(Long idInquilino) { this.idInquilino = idInquilino; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public Integer getDuracionMeses() { return duracionMeses; }
    public void setDuracionMeses(Integer duracionMeses) { this.duracionMeses = duracionMeses; }
    public BigDecimal getImporteMensual() { return importeMensual; }
    public void setImporteMensual(BigDecimal importeMensual) { this.importeMensual = importeMensual; }
    public Integer getDiaVencimientoMensual() { return diaVencimientoMensual; }
    public void setDiaVencimientoMensual(Integer diaVencimientoMensual) { this.diaVencimientoMensual = diaVencimientoMensual; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public EstadoContrato getEstado() { return estado; }
    public void setEstado(EstadoContrato estado) { this.estado = estado; }
}
