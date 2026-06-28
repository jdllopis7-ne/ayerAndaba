package tuti.desi.presentacion.propiedades;

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

import jakarta.validation.Valid;
import tuti.desi.entidades.Ciudad;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.TipoPropiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.CiudadService;
import tuti.desi.servicios.PropiedadService;

@Controller
@RequestMapping("/propiedadesBuscar")
public class PropiedadesBuscarController {

    @Autowired
    private PropiedadService service;

    @Autowired
    private CiudadService ciudadService;

    @RequestMapping(method = RequestMethod.GET)
    public String preparaForm(Model modelo) {
        modelo.addAttribute("formBean", new PropiedadesBuscarForm());
        return "propiedadesBuscar";
    }

    @ModelAttribute("allCiudades")
    public List<Ciudad> getAllCiudades() {
        return ciudadService.getAll();
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
    public String submit(@ModelAttribute("formBean") @Valid PropiedadesBuscarForm formBean,
            BindingResult result, ModelMap modelo, @RequestParam String action) {
        if (action.equals("actionBuscar")) {
            List<Propiedad> propiedades = service.filter(formBean);
            modelo.addAttribute("resultados", propiedades);
            modelo.addAttribute("formBean", formBean);
            return "propiedadesBuscar";
        }
        if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/";
        }
        if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/propiedadEditar";
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam Long id, @ModelAttribute("formBean") PropiedadesBuscarForm formBean,
            BindingResult result, ModelMap modelo) {
        try {
            service.deleteById(id);
            return "redirect:/propiedadesBuscar";
        } catch (Excepcion e) {
            result.addError(new ObjectError("formBean", e.getMessage()));
            modelo.addAttribute("resultados", service.filter(formBean));
            return "propiedadesBuscar";
        }
    }
}
