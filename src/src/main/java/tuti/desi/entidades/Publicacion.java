package tuti.desi.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La propiedad es obligatoria")
    @ManyToOne(optional = false)
    private Propiedad propiedad;

    @NotNull(message = "El precio mensual es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio mensual debe ser positivo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioMensual;

    @NotBlank(message = "Las condiciones de alquiler son obligatorias")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String condicionesAlquiler;

    @NotBlank(message = "La descripcion es obligatoria")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "La fecha de publicacion es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaPublicacion;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoPublicacion estado = EstadoPublicacion.ACTIVA;

    @Column(nullable = false)
    private Boolean eliminado = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public BigDecimal getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(BigDecimal precioMensual) {
        this.precioMensual = precioMensual;
    }

    public String getCondicionesAlquiler() {
        return condicionesAlquiler;
    }

    public void setCondicionesAlquiler(String condicionesAlquiler) {
        this.condicionesAlquiler = condicionesAlquiler;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPublicacion estado) {
        this.estado = estado;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
