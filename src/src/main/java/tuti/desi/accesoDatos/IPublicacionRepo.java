package tuti.desi.accesoDatos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.Publicacion;

@Repository
public interface IPublicacionRepo extends JpaRepository<Publicacion, Long> {

    @Query("""
            SELECT p FROM Publicacion p
            WHERE p.eliminado = false
              AND (:idPropiedad IS NULL OR p.propiedad.id = :idPropiedad)
              AND (:idCiudad IS NULL OR p.propiedad.ciudad.id = :idCiudad)
              AND (:estado IS NULL OR p.estado = :estado)
              AND (:precioDesde IS NULL OR p.precioMensual >= :precioDesde)
              AND (:precioHasta IS NULL OR p.precioMensual <= :precioHasta)
            ORDER BY p.fechaPublicacion DESC, p.id DESC
            """)
    List<Publicacion> filter(
            @Param("idPropiedad") Long idPropiedad,
            @Param("idCiudad") Long idCiudad,
            @Param("estado") EstadoPublicacion estado,
            @Param("precioDesde") BigDecimal precioDesde,
            @Param("precioHasta") BigDecimal precioHasta);

    @Query("""
            SELECT COUNT(p) > 0 FROM Publicacion p
            WHERE p.eliminado = false
              AND p.estado = tuti.desi.entidades.EstadoPublicacion.ACTIVA
              AND p.propiedad.id = :idPropiedad
              AND (:id IS NULL OR p.id <> :id)
            """)
    boolean existsActivaParaPropiedad(@Param("idPropiedad") Long idPropiedad, @Param("id") Long id);
}
