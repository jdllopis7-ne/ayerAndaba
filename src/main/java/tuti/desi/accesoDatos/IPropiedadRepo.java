package tuti.desi.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.TipoPropiedad;

@Repository
public interface IPropiedadRepo extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByEliminadoFalseOrderByDireccionAsc();

    List<Propiedad> findByEliminadoFalseAndEstadoOrderByDireccionAsc(EstadoDisponibilidad estado);

    @Query("""
            SELECT p FROM Propiedad p
            WHERE p.eliminado = false
              AND (:direccion IS NULL OR LOWER(p.direccion) LIKE LOWER(CONCAT('%', :direccion, '%')))
              AND (:idCiudad IS NULL OR p.ciudad.id = :idCiudad)
              AND (:tipo IS NULL OR p.tipo = :tipo)
              AND (:estado IS NULL OR p.estado = :estado)
            ORDER BY p.direccion
            """)
    List<Propiedad> filter(
            @Param("direccion") String direccion,
            @Param("idCiudad") Long idCiudad,
            @Param("tipo") TipoPropiedad tipo,
            @Param("estado") EstadoDisponibilidad estado);

    @Query("""
            SELECT COUNT(p) > 0 FROM Propiedad p
            WHERE p.eliminado = false
              AND p.estado <> tuti.desi.entidades.EstadoDisponibilidad.INACTIVA
              AND LOWER(p.direccion) = LOWER(:direccion)
              AND p.ciudad.id = :idCiudad
              AND (:id IS NULL OR p.id <> :id)
            """)
    boolean existsActivaDuplicada(
            @Param("direccion") String direccion,
            @Param("idCiudad") Long idCiudad,
            @Param("id") Long id);
}
