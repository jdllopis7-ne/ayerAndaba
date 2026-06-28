package tuti.desi.entidades;

public enum CategoriaIncidente {
    PLOMERIA("Plomeria"),
    ELECTRICIDAD("Electricidad"),
    GAS("Gas"),
    GENERAL("General");

    private final String descripcion;

    CategoriaIncidente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}