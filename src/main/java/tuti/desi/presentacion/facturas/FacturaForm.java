package tuti.desi.presentacion.facturas;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tuti.desi.entidades.EstadoFactura;
import tuti.desi.entidades.Factura;
import tuti.desi.entidades.MedioPago;

public class FacturaForm {
    private Long id;

    @NotNull(message = "El contrato es obligatorio")
    private Long idContrato;

    @NotBlank(message = "El concepto es obligatorio")
    private String conceptoFacturado;

    @NotNull(message = "La fecha de emision es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEmision;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;

    @NotNull(message = "El importe es obligatorio")
    @DecimalMin(value = "0.01", message = "El importe debe ser positivo")
    private BigDecimal importe;

    @NotNull(message = "El estado es obligatorio")
    private EstadoFactura estado = EstadoFactura.PENDIENTE;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPago;
    private MedioPago medioPago;
    private BigDecimal importePagado;
    private BigDecimal interesPagado;

    public FacturaForm() {
        fechaEmision = LocalDate.now();
        fechaVencimiento = LocalDate.now();
    }

    public FacturaForm(Factura factura) {
        this.id = factura.getId();
        this.idContrato = factura.getContrato().getId();
        this.conceptoFacturado = factura.getConceptoFacturado();
        this.fechaEmision = factura.getFechaEmision();
        this.fechaVencimiento = factura.getFechaVencimiento();
        this.importe = factura.getImporte();
        this.estado = factura.getEstado();
        this.fechaPago = factura.getFechaPago();
        this.medioPago = factura.getMedioPago();
        this.importePagado = factura.getImportePagado();
        this.interesPagado = factura.getInteresPagado();
    }

    public Factura toPojo() {
        Factura factura = new Factura();
        factura.setId(id);
        factura.setConceptoFacturado(conceptoFacturado == null ? null : conceptoFacturado.trim());
        factura.setFechaEmision(fechaEmision);
        factura.setFechaVencimiento(fechaVencimiento);
        factura.setImporte(importe);
        factura.setEstado(estado);
        factura.setFechaPago(fechaPago);
        factura.setMedioPago(medioPago);
        factura.setImportePagado(importePagado);
        factura.setInteresPagado(interesPagado);
        return factura;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdContrato() { return idContrato; }
    public void setIdContrato(Long idContrato) { this.idContrato = idContrato; }
    public String getConceptoFacturado() { return conceptoFacturado; }
    public void setConceptoFacturado(String conceptoFacturado) { this.conceptoFacturado = conceptoFacturado; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
    public MedioPago getMedioPago() { return medioPago; }
    public void setMedioPago(MedioPago medioPago) { this.medioPago = medioPago; }
    public BigDecimal getImportePagado() { return importePagado; }
    public void setImportePagado(BigDecimal importePagado) { this.importePagado = importePagado; }
    public BigDecimal getInteresPagado() { return interesPagado; }
    public void setInteresPagado(BigDecimal interesPagado) { this.interesPagado = interesPagado; }
}
