package tuti.desi.accesoDatos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.EstadoFactura;
import tuti.desi.entidades.Factura;

@Repository
public interface IFacturaRepo extends JpaRepository<Factura, Long> {

    @Query("""
            SELECT f FROM Factura f
            WHERE f.eliminado = false
              AND (:idContrato IS NULL OR f.contrato.id = :idContrato)
              AND (:idPropiedad IS NULL OR f.contrato.propiedad.id = :idPropiedad)
              AND (:idInquilino IS NULL OR f.contrato.inquilino.id = :idInquilino)
              AND (:estado IS NULL OR f.estado = :estado)
              AND (:vencimientoDesde IS NULL OR f.fechaVencimiento >= :vencimientoDesde)
              AND (:vencimientoHasta IS NULL OR f.fechaVencimiento <= :vencimientoHasta)
            ORDER BY f.fechaVencimiento DESC, f.id DESC
            """)
    List<Factura> filter(
            @Param("idContrato") Long idContrato,
            @Param("idPropiedad") Long idPropiedad,
            @Param("idInquilino") Long idInquilino,
            @Param("estado") EstadoFactura estado,
            @Param("vencimientoDesde") LocalDate vencimientoDesde,
            @Param("vencimientoHasta") LocalDate vencimientoHasta);
}
