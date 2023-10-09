package be.ucll.ipminor341t.web;

import be.ucll.ipminor341t.domain.Boat;
import be.ucll.ipminor341t.domain.service.BoatService;
import be.ucll.ipminor341t.generic.ServiceException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/boat")
public class BoatRestController {


    @Autowired
    private BoatService service;

    @GetMapping("/overview")
    public Iterable<Boat> getAll(){
        return service.getBoats();
    }

    @GetMapping("/search/{height}/{width}")
    public Iterable<Boat> findAllByHeightAndWidth(Model model, @PathVariable("height") int height, @PathVariable("width") int width) { return service.getBoatsByHeightAndWidth(height, width);}

    @GetMapping("/search")
    public Iterable<Boat> findAllByInsurance(@RequestParam("insurance") String insurance) { return service.getBoatsByInsurance(insurance);}


    @PostMapping("/add")
    public Boat addBoat(@Valid @RequestBody Boat boat) {
        return service.createBoat(boat);
    }

    @PutMapping("/update")
    public Boat updateBoat(@RequestParam("id") Long id, @Valid @RequestBody Boat boat) {
        return service.updateBoat(id, boat);
    }

    @DeleteMapping("/delete")
    public Boat deleteBoat(Model model, @RequestParam("id") Long id) {
        return service.deleteBoat(id);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ServiceException.class, ResponseStatusException.class, PersistenceException.class})
    public Map<String, String> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                //String localizedErrorMessage = messageSource.getMessage(errorMessage, null, LocaleContextHolder.getLocale());
                errors.put(fieldName, errorMessage);
            });
        }
        else if (ex instanceof ServiceException) {
            errors.put(((ServiceException) ex).getAction(), ex.getMessage());
        } else if(ex.getCause() instanceof ConstraintViolationException) {
            errors.put("databaseError", "Deze boot is niet uniek (Bestaat al)");
        } else {
            errors.put(((ResponseStatusException) ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }
}