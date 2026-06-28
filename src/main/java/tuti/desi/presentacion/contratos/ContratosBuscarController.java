package tuti.desi.presentacion.contratos;

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
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.ContratoService;
import tuti.desi.servicios.PersonaService;
import tuti.desi.servicios.PropiedadService;

@Controller
@RequestMapping("/contratosBuscar")
public class ContratosBuscarController {

    @Autowired
    private ContratoService service;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private PersonaService personaService;

    @RequestMapping(method = RequestMethod.GET)
    public String preparaForm(Model modelo) {
        modelo.addAttribute("formBean", new ContratosBuscarForm());
        return "contratosBuscar";
    }

    @ModelAttribute("allPropiedades")
    public List<Propiedad> getAllPropiedades() { return propiedadService.getAll(); }

    @ModelAttribute("allPersonas")
    public List<Persona> getAllPersonas() { return personaService.getAll(); }

    @ModelAttribute("allEstados")
    public EstadoContrato[] getAllEstados() { return EstadoContrato.values(); }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@ModelAttribute("formBean") ContratosBuscarForm formBean, BindingResult result,
            ModelMap modelo, @RequestParam String action) {
        if (action.equals("actionBuscar")) {
            List<Contrato> contratos = service.filter(formBean);
            modelo.addAttribute("resultados", contratos);
            modelo.addAttribute("formBean", formBean);
            return "contratosBuscar";
        }
        if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/";
        }
        if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/contratoEditar";
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam Long id, @ModelAttribute("formBean") ContratosBuscarForm formBean,
            BindingResult result, ModelMap modelo) {
        try {
            service.deleteById(id);
            return "redirect:/contratosBuscar";
        } catch (Excepcion e) {
            result.addError(new ObjectError("formBean", e.getMessage()));
            modelo.addAttribute("resultados", service.filter(formBean));
            return "contratosBuscar";
        }
    }
}
