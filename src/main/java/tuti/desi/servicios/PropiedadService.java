package tuti.desi.servicios;

import java.util.List;

import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.propiedades.PropiedadesBuscarForm;

public interface PropiedadService {

    List<Propiedad> getAll();

    List<Propiedad> getDisponibles();

    Propiedad getById(Long id);

    List<Propiedad> filter(PropiedadesBuscarForm filter);

    void save(Propiedad propiedad) throws Excepcion;

    void deleteById(Long id) throws Excepcion;
}
