package tuti.desi.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.desi.accesoDatos.IFacturaRepo;
import tuti.desi.accesoDatos.IHistorialEstadoFacturaRepo;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.EstadoFactura;
import tuti.desi.entidades.Factura;
import tuti.desi.entidades.HistorialEstadoFactura;
import tuti.desi.excepciones.EntidadNoEncontradaException;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.facturas.FacturasBuscarForm;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private IFacturaRepo repo;

    @Autowired
    private IHistorialEstadoFacturaRepo historialRepo;

    @Override
    public List<Factura> filter(FacturasBuscarForm filter) {
        return repo.filter(filter.getContratoSeleccionado(), filter.getPropiedadSeleccionada(),
                filter.getInquilinoSeleccionado(), filter.getEstado(), filter.getVencimientoDesde(),
                filter.getVencimientoHasta());
    }

    @Override
    public Factura getById(Long id) {
        Factura factura = repo.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("la factura", id));
        if (Boolean.TRUE.equals(factura.getEliminado())) {
            throw new EntidadNoEncontradaException("la factura", id);
        }
        return factura;
    }

    @Override
    public void save(Factura factura) throws Excepcion {
        if (factura.getEstado() == null) {
            factura.setEstado(EstadoFactura.PENDIENTE);
        }
        factura.setEliminado(false);

        if (factura.getFechaEmision() != null && factura.getFechaVencimiento() != null
                && factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
            throw new Excepcion("La fecha de vencimiento debe ser igual o posterior a la fecha de emision",
                    "fechaVencimiento");
        }

        EstadoFactura estadoAnterior = null;
        if (factura.getId() != null) {
            Factura actual = getById(factura.getId());
            estadoAnterior = actual.getEstado();
            factura.setContrato(actual.getContrato());
            if (estadoAnterior == EstadoFactura.ANULADA || estadoAnterior == EstadoFactura.PAGADA) {
                throw new Excepcion("No se puede modificar una factura anulada o pagada");
            }
            if (!transicionValida(estadoAnterior, factura.getEstado())) {
                throw new Excepcion("El cambio de estado de la factura no es coherente", "estado");
            }
        } else if (factura.getContrato().getEstado() != EstadoContrato.ACTIVO) {
            throw new Excepcion("Solo se pueden crear facturas para contratos activos", "idContrato");
        }

        validarPago(factura);
        Factura guardada = repo.save(factura);
        if (estadoAnterior == null || estadoAnterior != guardada.getEstado()) {
            registrarHistorial(guardada);
        }
    }

    @Override
    public void deleteById(Long id) throws Excepcion {
        Factura factura = getById(id);
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new Excepcion("No se puede eliminar una factura pagada");
        }
        factura.setEliminado(true);
        repo.save(factura);
    }

    private boolean transicionValida(EstadoFactura anterior, EstadoFactura nuevo) {
        if (anterior == nuevo) {
            return true;
        }
        if (anterior == EstadoFactura.PENDIENTE) {
            return nuevo == EstadoFactura.PAGADA || nuevo == EstadoFactura.VENCIDA || nuevo == EstadoFactura.ANULADA;
        }
        if (anterior == EstadoFactura.VENCIDA) {
            return nuevo == EstadoFactura.PAGADA;
        }
        return false;
    }

    private void validarPago(Factura factura) throws Excepcion {
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            if (factura.getFechaPago() == null) {
                throw new Excepcion("La fecha de pago es obligatoria para una factura pagada", "fechaPago");
            }
            if (factura.getMedioPago() == null) {
                throw new Excepcion("El medio de pago es obligatorio para una factura pagada", "medioPago");
            }
            if (factura.getImportePagado() == null || factura.getImportePagado().compareTo(BigDecimal.ZERO) <= 0) {
                throw new Excepcion("El importe pagado debe ser positivo", "importePagado");
            }
            if (factura.getInteresPagado() != null && factura.getInteresPagado().compareTo(BigDecimal.ZERO) < 0) {
                throw new Excepcion("El interes pagado no puede ser negativo", "interesPagado");
            }
            return;
        }

        if (factura.getEstado() == EstadoFactura.ANULADA
                && (factura.getFechaPago() != null || factura.getMedioPago() != null
                        || factura.getImportePagado() != null || factura.getInteresPagado() != null)) {
            throw new Excepcion("No se pueden registrar datos de pago en una factura anulada");
        }

        factura.setFechaPago(null);
        factura.setMedioPago(null);
        factura.setImportePagado(null);
        factura.setInteresPagado(null);
    }

    private void registrarHistorial(Factura factura) {
        HistorialEstadoFactura historial = new HistorialEstadoFactura();
        historial.setFactura(factura);
        historial.setEstado(factura.getEstado());
        historial.setFechaCambio(LocalDateTime.now());
        historialRepo.save(historial);
    }
}
