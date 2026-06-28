package tuti.desi.presentacion.publicaciones;

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

import tuti.desi.entidades.Ciudad;
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Publicacion;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.CiudadService;
import tuti.desi.servicios.PropiedadService;
import tuti.desi.servicios.PublicacionService;

@Controller
@RequestMapping("/publicacionesBuscar")
public class PublicacionesBuscarController {

    @Autowired
    private PublicacionService service;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private CiudadService ciudadService;

    @RequestMapping(method = RequestMethod.GET)
    public String preparaForm(Model modelo) {
        modelo.addAttribute("formBean", new PublicacionesBuscarForm());
        return "publicacionesBuscar";
    }

    @ModelAttribute("allPropiedades")
    public List<Propiedad> getAllPropiedades() { return propiedadService.getAll(); }

    @ModelAttribute("allCiudades")
    public List<Ciudad> getAllCiudades() { return ciudadService.getAll(); }

    @ModelAttribute("allEstados")
    public EstadoPublicacion[] getAllEstados() { return EstadoPublicacion.values(); }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@ModelAttribute("formBean") PublicacionesBuscarForm formBean, BindingResult result,
            ModelMap modelo, @RequestParam String action) {
        if (action.equals("actionBuscar")) {
            List<Publicacion> publicaciones = service.filter(formBean);
            modelo.addAttribute("resultados", publicaciones);
            modelo.addAttribute("formBean", formBean);
            return "publicacionesBuscar";
        }
        if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/";
        }
        if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/publicacionEditar";
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam Long id, @ModelAttribute("formBean") PublicacionesBuscarForm formBean,
            BindingResult result, ModelMap modelo) {
        try {
            service.deleteById(id);
            return "redirect:/publicacionesBuscar";
        } catch (Excepcion e) {
            result.addError(new ObjectError("formBean", e.getMessage()));
            modelo.addAttribute("resultados", service.filter(formBean));
            return "publicacionesBuscar";
        }
    }
}
