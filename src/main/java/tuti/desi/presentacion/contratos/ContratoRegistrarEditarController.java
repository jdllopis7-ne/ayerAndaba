package tuti.desi.presentacion.contratos;

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
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.ContratoService;
import tuti.desi.servicios.PersonaService;
import tuti.desi.servicios.PropiedadService;

@Controller
@RequestMapping("/contratoEditar")
public class ContratoRegistrarEditarController {

    @Autowired
    private ContratoService service;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private PersonaService personaService;

    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.GET)
    public String preparaForm(Model modelo, @PathVariable("id") Optional<Long> id) {
        if (id.isPresent()) {
            modelo.addAttribute("formBean", new ContratoForm(service.getById(id.get())));
        } else {
            modelo.addAttribute("formBean", new ContratoForm());
        }
        return "contratoEditar";
    }

    @ModelAttribute("allPropiedades")
    public List<Propiedad> getAllPropiedades() { return propiedadService.getAll(); }

    @ModelAttribute("allPersonas")
    public List<Persona> getAllPersonas() { return personaService.getAll(); }

    @ModelAttribute("allEstados")
    public EstadoContrato[] getAllEstados() { return EstadoContrato.values(); }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@ModelAttribute("formBean") @Valid ContratoForm formBean, BindingResult result,
            ModelMap modelo, @RequestParam String action) {
        if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/contratosBuscar";
        }
        if (!action.equals("actionAceptar")) {
            return "redirect:/";
        }
        if (result.hasErrors()) {
            modelo.addAttribute("formBean", formBean);
            return "contratoEditar";
        }
        try {
            Contrato contrato = formBean.toPojo();
            contrato.setPropiedad(propiedadService.getById(formBean.getIdPropiedad()));
            contrato.setInquilino(personaService.getPersonaById(formBean.getIdInquilino()));
            service.save(contrato);
            return "redirect:/contratosBuscar";
        } catch (Excepcion e) {
            if (e.getAtributo() == null) {
                result.addError(new ObjectError("formBean", e.getMessage()));
            } else {
                result.addError(new FieldError("formBean", e.getAtributo(), e.getMessage()));
            }
            modelo.addAttribute("formBean", formBean);
            return "contratoEditar";
        }
    }
}
