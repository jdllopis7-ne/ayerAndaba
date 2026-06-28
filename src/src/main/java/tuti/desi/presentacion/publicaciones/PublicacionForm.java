package tuti.desi.presentacion.publicaciones;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.Publicacion;

public class PublicacionForm {
    private Long id;

    @NotNull(message = "La propiedad es obligatoria")
    private Long idPropiedad;

    @NotNull(message = "El precio mensual es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio mensual debe ser positivo")
    private BigDecimal precioMensual;

    @NotBlank(message = "Las condiciones son obligatorias")
    private String condicionesAlquiler;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotNull(message = "La fecha de publicacion es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPublicacion;

    @NotNull(message = "El estado es obligatorio")
    private EstadoPublicacion estado = EstadoPublicacion.ACTIVA;

    public PublicacionForm() {
        this.fechaPublicacion = LocalDate.now();
    }

    public PublicacionForm(Publicacion publicacion) {
        this.id = publicacion.getId();
        this.idPropiedad = publicacion.getPropiedad().getId();
        this.precioMensual = publicacion.getPrecioMensual();
        this.condicionesAlquiler = publicacion.getCondicionesAlquiler();
        this.descripcion = publicacion.getDescripcion();
        this.fechaPublicacion = publicacion.getFechaPublicacion();
        this.estado = publicacion.getEstado();
    }

    public Publicacion toPojo() {
        Publicacion publicacion = new Publicacion();
        publicacion.setId(id);
        publicacion.setPrecioMensual(precioMensual);
        publicacion.setCondicionesAlquiler(condicionesAlquiler == null ? null : condicionesAlquiler.trim());
        publicacion.setDescripcion(descripcion == null ? null : descripcion.trim());
        publicacion.setFechaPublicacion(fechaPublicacion);
        publicacion.setEstado(estado);
        return publicacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Long idPropiedad) { this.idPropiedad = idPropiedad; }
    public BigDecimal getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(BigDecimal precioMensual) { this.precioMensual = precioMensual; }
    public String getCondicionesAlquiler() { return condicionesAlquiler; }
    public void setCondicionesAlquiler(String condicionesAlquiler) { this.condicionesAlquiler = condicionesAlquiler; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    public EstadoPublicacion getEstado() { return estado; }
    public void setEstado(EstadoPublicacion estado) { this.estado = estado; }
}
