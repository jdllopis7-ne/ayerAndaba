package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Incidente;

@Repository
public interface IIncidenteRepo extends JpaRepository<Incidente, Long> {
}