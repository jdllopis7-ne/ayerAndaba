package tuti.desi.servicios;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.desi.accesoDatos.IHistorialEstadoPublicacionRepo;
import tuti.desi.accesoDatos.IPublicacionRepo;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.HistorialEstadoPublicacion;
import tuti.desi.entidades.Publicacion;
import tuti.desi.excepciones.EntidadNoEncontradaException;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.publicaciones.PublicacionesBuscarForm;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    @Autowired
    private IPublicacionRepo repo;

    @Autowired
    private IHistorialEstadoPublicacionRepo historialRepo;

    @Override
    public java.util.List<Publicacion> filter(PublicacionesBuscarForm filter) {
        return repo.filter(filter.getPropiedadSeleccionada(), filter.getCiudadSeleccionada(), filter.getEstado(),
                filter.getPrecioDesde(), filter.getPrecioHasta());
    }

    @Override
    public Publicacion getById(Long id) {
        Publicacion publicacion = repo.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("la publicacion", id));
        if (Boolean.TRUE.equals(publicacion.getEliminado())) {
            throw new EntidadNoEncontradaException("la publicacion", id);
        }
        return publicacion;
    }

    @Override
    public void save(Publicacion publicacion) throws Excepcion {
        if (publicacion.getEstado() == null) {
            publicacion.setEstado(EstadoPublicacion.ACTIVA);
        }
        publicacion.setEliminado(false);

        EstadoPublicacion estadoAnterior = null;
        String condicionesAnteriores = null;
        if (publicacion.getId() != null) {
            Publicacion actual = getById(publicacion.getId());
            estadoAnterior = actual.getEstado();
            condicionesAnteriores = actual.getCondicionesAlquiler();
            publicacion.setPropiedad(actual.getPropiedad());
        }

        if (publicacion.getEstado() == EstadoPublicacion.ACTIVA) {
            if (publicacion.getPropiedad().getEstado() != EstadoDisponibilidad.DISPONIBLE) {
                throw new Excepcion("Solo se puede activar una publicacion de una propiedad disponible", "estado");
            }
            if (repo.existsActivaParaPropiedad(publicacion.getPropiedad().getId(), publicacion.getId())) {
                throw new Excepcion("Ya existe una publicacion activa para la propiedad seleccionada", "idPropiedad");
            }
        }

        if (estadoAnterior == EstadoPublicacion.FINALIZADA
                && condicionesAnteriores != null
                && !condicionesAnteriores.equals(publicacion.getCondicionesAlquiler())) {
            throw new Excepcion("No se pueden modificar condiciones de una publicacion finalizada",
                    "condicionesAlquiler");
        }

        Publicacion guardada = repo.save(publicacion);
        if (estadoAnterior == null || estadoAnterior != guardada.getEstado()) {
            registrarHistorial(guardada);
        }
    }

    @Override
    public void deleteById(Long id) throws Excepcion {
        Publicacion publicacion = getById(id);
        if (publicacion.getEstado() != EstadoPublicacion.ACTIVA) {
            throw new Excepcion("Solo pueden eliminarse publicaciones activas");
        }
        publicacion.setEliminado(true);
        repo.save(publicacion);
    }

    private void registrarHistorial(Publicacion publicacion) {
        HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();
        historial.setPublicacion(publicacion);
        historial.setEstado(publicacion.getEstado());
        historial.setFechaCambio(LocalDateTime.now());
        historialRepo.save(historial);
    }
}
