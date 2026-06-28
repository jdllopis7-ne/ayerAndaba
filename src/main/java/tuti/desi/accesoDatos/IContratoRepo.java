package tuti.desi.accesoDatos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;

@Repository
public interface IContratoRepo extends JpaRepository<Contrato, Long> {

    boolean existsByPropiedadIdAndEstadoAndEliminadoFalse(Long idPropiedad, EstadoContrato estado);

    @Query("""
            SELECT COUNT(c) > 0 FROM Contrato c
            WHERE c.eliminado = false
              AND c.estado = tuti.desi.entidades.EstadoContrato.ACTIVO
              AND c.propiedad.id = :idPropiedad
              AND (:id IS NULL OR c.id <> :id)
            """)
    boolean existsActivoParaPropiedad(@Param(" ") Long idPropiedad, @Param("id") Long id);

    @Query("""
            SELECT c FROM Contrato c
            WHERE c.eliminado = false
              AND (:idPropiedad IS NULL OR c.propiedad.id = :idPropiedad)
              AND (:idInquilino IS NULL OR c.inquilino.id = :idInquilino)
              AND (:estado IS NULL OR c.estado = :estado)
              AND (:fechaInicio IS NULL OR c.fechaInicio = :fechaInicio)
            ORDER BY c.fechaInicio DESC, c.id DESC
            """)
    List<Contrato> filter(
            @Param("idPropiedad") Long idPropiedad,
            @Param("idInquilino") Long idInquilino,
            @Param("estado") EstadoContrato estado,
            @Param("fechaInicio") LocalDate fechaInicio);
}
