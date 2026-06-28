package tuti.desi.entidades;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La direccion es obligatoria")
    @Column(nullable = false, length = 200)
    private String direccion;

    @NotNull(message = "La ciudad es obligatoria")
    @ManyToOne(optional = false)
    private Ciudad ciudad;

    @NotNull(message = "El tipo de propiedad es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoPropiedad tipo;

    @NotNull(message = "La cantidad de ambientes es obligatoria")
    @Min(value = 1, message = "La cantidad de ambientes debe ser positiva")
    @Column(nullable = false)
    private Integer cantidadAmbientes;

    @NotNull(message = "Los metros cuadrados son obligatorios")
    @DecimalMin(value = "0.01", message = "Los metros cuadrados deben ser positivos")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal metrosCuadrados;

    @NotBlank(message = "La descripcion es obligatoria")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String comodidades;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoDisponibilidad estado = EstadoDisponibilidad.DISPONIBLE;

    @NotNull(message = "El propietario es obligatorio")
    @ManyToOne(optional = false)
    private Persona propietario;

    @Column(nullable = false)
    private Boolean eliminado = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public TipoPropiedad getTipo() {
        return tipo;
    }

    public void setTipo(TipoPropiedad tipo) {
        this.tipo = tipo;
    }

    public Integer getCantidadAmbientes() {
        return cantidadAmbientes;
    }

    public void setCantidadAmbientes(Integer cantidadAmbientes) {
        this.cantidadAmbientes = cantidadAmbientes;
    }

    public BigDecimal getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(BigDecimal metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComodidades() {
        return comodidades;
    }

    public void setComodidades(String comodidades) {
        this.comodidades = comodidades;
    }

    public EstadoDisponibilidad getEstado() {
        return estado;
    }

    public void setEstado(EstadoDisponibilidad estado) {
        this.estado = estado;
    }

    public Persona getPropietario() {
        return propietario;
    }

    public void setPropietario(Persona propietario) {
        this.propietario = propietario;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
