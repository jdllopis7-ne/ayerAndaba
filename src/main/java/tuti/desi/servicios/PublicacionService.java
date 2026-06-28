package tuti.desi.servicios;

import java.util.List;

import tuti.desi.entidades.Publicacion;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.publicaciones.PublicacionesBuscarForm;

public interface PublicacionService {

    List<Publicacion> filter(PublicacionesBuscarForm filter);

    Publicacion getById(Long id);

    void save(Publicacion publicacion) throws Excepcion;

    void deleteById(Long id) throws Excepcion;
}
