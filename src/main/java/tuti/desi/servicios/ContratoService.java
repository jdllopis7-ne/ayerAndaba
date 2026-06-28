package tuti.desi.servicios;

import java.util.List;

import tuti.desi.entidades.Contrato;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.contratos.ContratosBuscarForm;

public interface ContratoService {

    List<Contrato> filter(ContratosBuscarForm filter);

    List<Contrato> getActivos();

    Contrato getById(Long id);

    void save(Contrato contrato) throws Excepcion;

    void deleteById(Long id) throws Excepcion;
}
