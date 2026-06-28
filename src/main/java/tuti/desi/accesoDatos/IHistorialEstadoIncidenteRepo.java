package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.HistorialEstadoIncidente;

@Repository
public interface IHistorialEstadoIncidenteRepo extends JpaRepository<HistorialEstadoIncidente, Long> {
}