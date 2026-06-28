package tuti.desi.entidades;

public enum MedioPago {
    TRANSFERENCIA("Transferencia"),
    EFECTIVO("Efectivo"),
    DEBITO("Debito"),
    CREDITO("Credito");

    private final String descripcion;

    MedioPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
