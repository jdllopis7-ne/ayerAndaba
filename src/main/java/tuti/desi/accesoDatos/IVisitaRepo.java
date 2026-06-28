package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Visita;

@Repository
public interface IVisitaRepo extends JpaRepository<Visita, Long> {
}