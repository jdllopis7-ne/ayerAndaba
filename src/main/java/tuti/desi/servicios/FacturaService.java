package tuti.desi.servicios;

import java.util.List;

import tuti.desi.entidades.Factura;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.facturas.FacturasBuscarForm;

public interface FacturaService {

    List<Factura> filter(FacturasBuscarForm filter);

    Factura getById(Long id);

    void save(Factura factura) throws Excepcion;

    void deleteById(Long id) throws Excepcion;
}
