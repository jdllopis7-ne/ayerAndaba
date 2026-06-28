package tuti.desi.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.desi.accesoDatos.IContratoRepo;
import tuti.desi.accesoDatos.IHistorialEstadoPropiedadRepo;
import tuti.desi.accesoDatos.IPropiedadRepo;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.HistorialEstadoPropiedad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.EntidadNoEncontradaException;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.propiedades.PropiedadesBuscarForm;

@Service
public class PropiedadServiceImpl implements PropiedadService {

    @Autowired
    private IPropiedadRepo repo;

    @Autowired
    private IContratoRepo contratoRepo;

    @Autowired
    private IHistorialEstadoPropiedadRepo historialRepo;

    @Override
    public List<Propiedad> getAll() {
        return repo.findByEliminadoFalseOrderByDireccionAsc();
    }

    @Override
    public List<Propiedad> getDisponibles() {
        return repo.findByEliminadoFalseAndEstadoOrderByDireccionAsc(EstadoDisponibilidad.DISPONIBLE);
    }

    @Override
    public Propiedad getById(Long id) {
        Propiedad propiedad = repo.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("la propiedad", id));
        if (Boolean.TRUE.equals(propiedad.getEliminado())) {
            throw new EntidadNoEncontradaException("la propiedad", id);
        }
        return propiedad;
    }

    @Override
    public List<Propiedad> filter(PropiedadesBuscarForm filter) {
        return repo.filter(filter.getDireccion(), filter.getCiudadSeleccionada(), filter.getTipo(), filter.getEstado());
    }

    @Override
    public void save(Propiedad propiedad) throws Excepcion {
        if (propiedad.getEstado() == null) {
            propiedad.setEstado(EstadoDisponibilidad.DISPONIBLE);
        }
        propiedad.setEliminado(false);

        EstadoDisponibilidad estadoAnterior = null;
        if (propiedad.getId() != null) {
            Propiedad actual = getById(propiedad.getId());
            estadoAnterior = actual.getEstado();
            if (contratoRepo.existsByPropiedadIdAndEstadoAndEliminadoFalse(propiedad.getId(), EstadoContrato.ACTIVO)
                    && (propiedad.getEstado() == EstadoDisponibilidad.DISPONIBLE
                            || propiedad.getEstado() == EstadoDisponibilidad.INACTIVA)) {
                throw new Excepcion("No se puede pasar a disponible o inactiva una propiedad con contrato activo",
                        "estado");
            }
        }

        if (repo.existsActivaDuplicada(propiedad.getDireccion().trim(), propiedad.getCiudad().getId(), propiedad.getId())) {
            throw new Excepcion("Ya existe una propiedad activa con la misma direccion y ciudad", "direccion");
        }

        Propiedad guardada = repo.save(propiedad);
        if (estadoAnterior == null || estadoAnterior != guardada.getEstado()) {
            registrarHistorial(guardada);
        }
    }

    @Override
    public void deleteById(Long id) throws Excepcion {
        Propiedad propiedad = getById(id);
        if (contratoRepo.existsByPropiedadIdAndEstadoAndEliminadoFalse(id, EstadoContrato.ACTIVO)) {
            throw new Excepcion("No se puede eliminar una propiedad con contrato activo vigente");
        }
        propiedad.setEliminado(true);
        repo.save(propiedad);
    }

    private void registrarHistorial(Propiedad propiedad) {
        HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
        historial.setPropiedad(propiedad);
        historial.setEstado(propiedad.getEstado());
        historial.setFechaCambio(LocalDateTime.now());
        historialRepo.save(historial);
    }
}
