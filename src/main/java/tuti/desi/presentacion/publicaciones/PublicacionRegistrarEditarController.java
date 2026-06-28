package tuti.desi.presentacion.publicaciones;

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
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Publicacion;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.PropiedadService;
import tuti.desi.servicios.PublicacionService;

@Controller
@RequestMapping("/publicacionEditar")
public class PublicacionRegistrarEditarController {

    @Autowired
    private PublicacionService service;

    @Autowired
    private PropiedadService propiedadService;

    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.GET)
    public String preparaForm(Model modelo, @PathVariable("id") Optional<Long> id) {
        if (id.isPresent()) {
            modelo.addAttribute("formBean", new PublicacionForm(service.getById(id.get())));
        } else {
            modelo.addAttribute("formBean", new PublicacionForm());
        }
        return "publicacionEditar";
    }

    @ModelAttribute("allPropiedades")
    public List<Propiedad> getAllPropiedades() { return propiedadService.getAll(); }

    @ModelAttribute("allEstados")
    public EstadoPublicacion[] getAllEstados() { return EstadoPublicacion.values(); }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@ModelAttribute("formBean") @Valid PublicacionForm formBean, BindingResult result,
            ModelMap modelo, @RequestParam String action) {
        if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/publicacionesBuscar";
        }
        if (!action.equals("actionAceptar")) {
            return "redirect:/";
        }
        if (result.hasErrors()) {
            modelo.addAttribute("formBean", formBean);
            return "publicacionEditar";
        }
        try {
            Publicacion publicacion = formBean.toPojo();
            publicacion.setPropiedad(propiedadService.getById(formBean.getIdPropiedad()));
            service.save(publicacion);
            return "redirect:/publicacionesBuscar";
        } catch (Excepcion e) {
            if (e.getAtributo() == null) {
                result.addError(new ObjectError("formBean", e.getMessage()));
            } else {
                result.addError(new FieldError("formBean", e.getAtributo(), e.getMessage()));
            }
            modelo.addAttribute("formBean", formBean);
            return "publicacionEditar";
        }
    }
}
