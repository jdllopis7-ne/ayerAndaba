package tuti.desi.presentacion.facturas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoFactura;
import tuti.desi.entidades.Factura;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.ContratoService;
import tuti.desi.servicios.FacturaService;
import tuti.desi.servicios.PersonaService;
import tuti.desi.servicios.PropiedadService;

@Controller
@RequestMapping("/facturasBuscar")
public class FacturasBuscarController {

    @Autowired
    private FacturaService service;

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private PersonaService personaService;

    @RequestMapping(method = RequestMethod.GET)
    public String preparaForm(Model modelo) {
        modelo.addAttribute("formBean", new FacturasBuscarForm());
        return "facturasBuscar";
    }

    @ModelAttribute("allContratos")
    public List<Contrato> getAllContratos() { return contratoService.filter(new tuti.desi.presentacion.contratos.ContratosBuscarForm()); }

    @ModelAttribute("allPropiedades")
    public List<Propiedad> getAllPropiedades() { return propiedadService.getAll(); }

    @ModelAttribute("allPersonas")
    public List<Persona> getAllPersonas() { return personaService.getAll(); }

    @ModelAttribute("allEstados")
    public EstadoFactura[] getAllEstados() { return EstadoFactura.values(); }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@ModelAttribute("formBean") FacturasBuscarForm formBean, BindingResult result,
            ModelMap modelo, @RequestParam String action) {
        if (action.equals("actionBuscar")) {
            List<Factura> facturas = service.filter(formBean);
            modelo.addAttribute("resultados", facturas);
            modelo.addAttribute("formBean", formBean);
            return "facturasBuscar";
        }
        if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/";
        }
        if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/facturaEditar";
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam Long id, @ModelAttribute("formBean") FacturasBuscarForm formBean,
            BindingResult result, ModelMap modelo) {
        try {
            service.deleteById(id);
            return "redirect:/facturasBuscar";
        } catch (Excepcion e) {
            result.addError(new ObjectError("formBean", e.getMessage()));
            modelo.addAttribute("resultados", service.filter(formBean));
            return "facturasBuscar";
        }
    }
}
