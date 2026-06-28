package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.HistorialEstadoPropiedad;

@Repository
public interface IHistorialEstadoPropiedadRepo extends JpaRepository<HistorialEstadoPropiedad, Long> {
}
