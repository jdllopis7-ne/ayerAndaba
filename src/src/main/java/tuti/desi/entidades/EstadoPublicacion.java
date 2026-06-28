package tuti.desi.entidades;

public enum EstadoPublicacion {
    ACTIVA("Activa"),
    PAUSADA("Pausada"),
    FINALIZADA("Finalizada");

    private final String descripcion;

    EstadoPublicacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
