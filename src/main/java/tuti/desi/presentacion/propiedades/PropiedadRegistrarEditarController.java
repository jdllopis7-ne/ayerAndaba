package tuti.desi.presentacion.propiedades;

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
import tuti.desi.entidades.Ciudad;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.TipoPropiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.CiudadService;
import tuti.desi.servicios.PersonaService;
import tuti.desi.servicios.PropiedadService;

@Controller
@RequestMapping("/propiedadEditar")
public class PropiedadRegistrarEditarController {

    @Autowired
    private PropiedadService service;

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private PersonaService personaService;

    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.GET)
    public String preparaForm(Model modelo, @PathVariable("id") Optional<Long> id) {
        if (id.isPresent()) {
            modelo.addAttribute("formBean", new PropiedadForm(service.getById(id.get())));
        } else {
            modelo.addAttribute("formBean", new PropiedadForm());
        }
        return "propiedadEditar";
    }

    @ModelAttribute("allCiudades")
    public List<Ciudad> getAllCiudades() {
        return ciudadService.getAll();
    }

    @ModelAttribute("allPersonas")
    public List<Persona> getAllPersonas() {
        return personaService.getAll();
    }

    @ModelAttribute("allTipos")
    public TipoPropiedad[] getAllTipos() {
        return TipoPropiedad.values();
    }

    @ModelAttribute("allEstados")
    public EstadoDisponibilidad[] getAllEstados() {
        return EstadoDisponibilidad.values();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@ModelAttribute("formBean") @Valid PropiedadForm formBean, BindingResult result,
            ModelMap modelo, @RequestParam String action) {
        if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/propiedadesBuscar";
        }
        if (!action.equals("actionAceptar")) {
            return "redirect:/";
        }
        if (result.hasErrors()) {
            modelo.addAttribute("formBean", formBean);
            return "propiedadEditar";
        }
        try {
            Propiedad propiedad = formBean.toPojo();
            propiedad.setCiudad(ciudadService.getById(formBean.getIdCiudad()));
            propiedad.setPropietario(personaService.getPersonaById(formBean.getIdPropietario()));
            service.save(propiedad);
            return "redirect:/propiedadesBuscar";
        } catch (Excepcion e) {
            if (e.getAtributo() == null) {
                result.addError(new ObjectError("formBean", e.getMessage()));
            } else {
                result.addError(new FieldError("formBean", e.getAtributo(), e.getMessage()));
            }
            modelo.addAttribute("formBean", formBean);
            return "propiedadEditar";
        }
    }
}
