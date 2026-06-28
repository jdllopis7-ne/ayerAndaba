package tuti.desi.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.desi.accesoDatos.IContratoRepo;
import tuti.desi.accesoDatos.IHistorialEstadoContratoRepo;
import tuti.desi.accesoDatos.IHistorialEstadoPropiedadRepo;
import tuti.desi.accesoDatos.IPropiedadRepo;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.HistorialEstadoContrato;
import tuti.desi.entidades.HistorialEstadoPropiedad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.EntidadNoEncontradaException;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.contratos.ContratosBuscarForm;

@Service
public class ContratoServiceImpl implements ContratoService {

    @Autowired
    private IContratoRepo repo;

    @Autowired
    private IPropiedadRepo propiedadRepo;

    @Autowired
    private IHistorialEstadoContratoRepo historialRepo;

    @Autowired
    private IHistorialEstadoPropiedadRepo historialPropiedadRepo;

    @Override
    public List<Contrato> filter(ContratosBuscarForm filter) {
        return repo.filter(filter.getPropiedadSeleccionada(), filter.getInquilinoSeleccionado(), filter.getEstado(),
                filter.getFechaInicio());
    }

    @Override
    public List<Contrato> getActivos() {
        ContratosBuscarForm filter = new ContratosBuscarForm();
        filter.setEstado(EstadoContrato.ACTIVO);
        return filter(filter);
    }

    @Override
    public Contrato getById(Long id) {
        Contrato contrato = repo.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("el contrato", id));
        if (Boolean.TRUE.equals(contrato.getEliminado())) {
            throw new EntidadNoEncontradaException("el contrato", id);
        }
        return contrato;
    }

    @Override
    public void save(Contrato contrato) throws Excepcion {
        if (contrato.getEstado() == null) {
            contrato.setEstado(EstadoContrato.BORRADOR);
        }
        contrato.setEliminado(false);

        EstadoContrato estadoAnterior = null;
        if (contrato.getId() != null) {
            Contrato actual = getById(contrato.getId());
            estadoAnterior = actual.getEstado();
            if (!transicionValida(estadoAnterior, contrato.getEstado())) {
                throw new Excepcion("El cambio de estado del contrato no es coherente", "estado");
            }
        }

        if (contrato.getEstado() == EstadoContrato.ACTIVO) {
            if (repo.existsActivoParaPropiedad(contrato.getPropiedad().getId(), contrato.getId())) {
                throw new Excepcion("La propiedad ya tiene un contrato activo", "idPropiedad");
            }
            if (contrato.getPropiedad().getEstado() != EstadoDisponibilidad.DISPONIBLE
                    && estadoAnterior != EstadoContrato.ACTIVO) {
                throw new Excepcion("No se puede activar un contrato si la propiedad no esta disponible",
                        "idPropiedad");
            }
        }

        Contrato guardado = repo.save(contrato);
        if (estadoAnterior == null || estadoAnterior != guardado.getEstado()) {
            registrarHistorial(guardado);
            sincronizarPropiedad(guardado);
        }
    }

    @Override
    public void deleteById(Long id) throws Excepcion {
        Contrato contrato = getById(id);
        if (contrato.getEstado() != EstadoContrato.BORRADOR) {
            throw new Excepcion("Solo pueden eliminarse contratos en estado borrador");
        }
        contrato.setEliminado(true);
        repo.save(contrato);
    }

    private boolean transicionValida(EstadoContrato anterior, EstadoContrato nuevo) {
        if (anterior == nuevo) {
            return true;
        }
        if (anterior == EstadoContrato.BORRADOR) {
            return nuevo == EstadoContrato.ACTIVO || nuevo == EstadoContrato.BORRADOR;
        }
        if (anterior == EstadoContrato.ACTIVO) {
            return nuevo == EstadoContrato.FINALIZADO || nuevo == EstadoContrato.RESCINDIDO
                    || nuevo == EstadoContrato.ACTIVO;
        }
        return false;
    }

    private void sincronizarPropiedad(Contrato contrato) {
        if (contrato.getEstado() != EstadoContrato.ACTIVO && contrato.getEstado() != EstadoContrato.FINALIZADO
                && contrato.getEstado() != EstadoContrato.RESCINDIDO) {
            return;
        }

        Propiedad propiedad = contrato.getPropiedad();
        EstadoDisponibilidad nuevoEstado = contrato.getEstado() == EstadoContrato.ACTIVO
                ? EstadoDisponibilidad.ALQUILADA
                : EstadoDisponibilidad.DISPONIBLE;
        if (propiedad.getEstado() != nuevoEstado) {
            propiedad.setEstado(nuevoEstado);
            propiedadRepo.save(propiedad);

            HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
            historial.setPropiedad(propiedad);
            historial.setEstado(nuevoEstado);
            historial.setFechaCambio(LocalDateTime.now());
            historialPropiedadRepo.save(historial);
        }
    }

    private void registrarHistorial(Contrato contrato) {
        HistorialEstadoContrato historial = new HistorialEstadoContrato();
        historial.setContrato(contrato);
        historial.setEstado(contrato.getEstado());
        historial.setFechaCambio(LocalDateTime.now());
        historialRepo.save(historial);
    }
}
