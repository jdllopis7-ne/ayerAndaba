package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.HistorialEstadoFactura;

@Repository
public interface IHistorialEstadoFacturaRepo extends JpaRepository<HistorialEstadoFactura, Long> {
}
