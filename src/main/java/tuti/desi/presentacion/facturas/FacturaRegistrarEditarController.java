package tuti.desi.presentacion.facturas;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoFactura;
import tuti.desi.entidades.Factura;
import tuti.desi.entidades.MedioPago;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.ContratoService;
import tuti.desi.servicios.FacturaService;

@Controller
@RequestMapping("/facturaEditar")
public class FacturaRegistrarEditarController {

    @Autowired
    private FacturaService service;

    @Autowired
    private ContratoService contratoService;

    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.GET)
    public String preparaForm(Model modelo, @PathVariable("id") Optional<Long> id) {
        if (id.isPresent()) {
            modelo.addAttribute("formBean", new FacturaForm(service.getById(id.get())));
        } else {
            modelo.addAttribute("formBean", new FacturaForm());
        }
        return "facturaEditar";
    }

    @ModelAttribute("allContratos")
    public List<Contrato> getAllContratos() { return contratoService.filter(new tuti.desi.presentacion.contratos.ContratosBuscarForm()); }

    @ModelAttribute("allEstados")
    public EstadoFactura[] getAllEstados() { return EstadoFactura.values(); }

    @ModelAttribute("allMediosPago")
    public MedioPago[] getAllMediosPago() { return MedioPago.values(); }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@ModelAttribute("formBean") @Valid FacturaForm formBean, BindingResult result,
            ModelMap modelo, @RequestParam String action) {
        if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/facturasBuscar";
        }
        if (!action.equals("actionAceptar")) {
            return "redirect:/";
        }
        if (result.hasErrors()) {
            modelo.addAttribute("formBean", formBean);
            return "facturaEditar";
        }
        try {
            Factura factura = formBean.toPojo();
            factura.setContrato(contratoService.getById(formBean.getIdContrato()));
            service.save(factura);
            return "redirect:/facturasBuscar";
        } catch (Excepcion e) {
            if (e.getAtributo() == null) {
                result.addError(new ObjectError("formBean", e.getMessage()));
            } else {
                result.addError(new FieldError("formBean", e.getAtributo(), e.getMessage()));
            }
            modelo.addAttribute("formBean", formBean);
            return "facturaEditar";
        }
    }
}
