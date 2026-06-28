package tuti.desi.entidades;

public enum EstadoDisponibilidad {
    DISPONIBLE("Disponible"),
    RESERVADA("Reservada"),
    ALQUILADA("Alquilada"),
    INACTIVA("Inactiva");

    private final String descripcion;

    EstadoDisponibilidad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}