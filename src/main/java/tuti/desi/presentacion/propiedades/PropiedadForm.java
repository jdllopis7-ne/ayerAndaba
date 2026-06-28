package tuti.desi.presentacion.propiedades;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.TipoPropiedad;

public class PropiedadForm {
    private Long id;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;

    @NotNull(message = "La ciudad es obligatoria")
    private Long idCiudad;

    @NotNull(message = "El tipo es obligatorio")
    private TipoPropiedad tipo;

    @NotNull(message = "La cantidad de ambientes es obligatoria")
    @Min(value = 1, message = "La cantidad de ambientes debe ser positiva")
    private Integer cantidadAmbientes;

    @NotNull(message = "Los metros cuadrados son obligatorios")
    @DecimalMin(value = "0.01", message = "Los metros cuadrados deben ser positivos")
    private BigDecimal metrosCuadrados;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    private String comodidades;

    @NotNull(message = "El estado es obligatorio")
    private EstadoDisponibilidad estado = EstadoDisponibilidad.DISPONIBLE;

    @NotNull(message = "El propietario es obligatorio")
    private Long idPropietario;

    public PropiedadForm() {
    }

    public PropiedadForm(Propiedad propiedad) {
        this.id = propiedad.getId();
        this.direccion = propiedad.getDireccion();
        this.idCiudad = propiedad.getCiudad().getId();
        this.tipo = propiedad.getTipo();
        this.cantidadAmbientes = propiedad.getCantidadAmbientes();
        this.metrosCuadrados = propiedad.getMetrosCuadrados();
        this.descripcion = propiedad.getDescripcion();
        this.comodidades = propiedad.getComodidades();
        this.estado = propiedad.getEstado();
        this.idPropietario = propiedad.getPropietario().getId();
    }

    public Propiedad toPojo() {
        Propiedad propiedad = new Propiedad();
        propiedad.setId(id);
        propiedad.setDireccion(direccion == null ? null : direccion.trim());
        propiedad.setTipo(tipo);
        propiedad.setCantidadAmbientes(cantidadAmbientes);
        propiedad.setMetrosCuadrados(metrosCuadrados);
        propiedad.setDescripcion(descripcion == null ? null : descripcion.trim());
        propiedad.setComodidades(comodidades == null ? null : comodidades.trim());
        propiedad.setEstado(estado);
        return propiedad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Long getIdCiudad() { return idCiudad; }
    public void setIdCiudad(Long idCiudad) { this.idCiudad = idCiudad; }
    public TipoPropiedad getTipo() { return tipo; }
    public void setTipo(TipoPropiedad tipo) { this.tipo = tipo; }
    public Integer getCantidadAmbientes() { return cantidadAmbientes; }
    public void setCantidadAmbientes(Integer cantidadAmbientes) { this.cantidadAmbientes = cantidadAmbientes; }
    public BigDecimal getMetrosCuadrados() { return metrosCuadrados; }
    public void setMetrosCuadrados(BigDecimal metrosCuadrados) { this.metrosCuadrados = metrosCuadrados; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getComodidades() { return comodidades; }
    public void setComodidades(String comodidades) { this.comodidades = comodidades; }
    public EstadoDisponibilidad getEstado() { return estado; }
    public void setEstado(EstadoDisponibilidad estado) { this.estado = estado; }
    public Long getIdPropietario() { return idPropietario; }
    public void setIdPropietario(Long idPropietario) { this.idPropietario = idPropietario; }
}
