package be.ucll.ipminor341t.web;

import be.ucll.ipminor341t.domain.Boat;
import be.ucll.ipminor341t.domain.service.BoatService;
import be.ucll.ipminor341t.domain.service.StorageService;
import be.ucll.ipminor341t.generic.ServiceException;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/storage-boat")
public class StorageBoatRestController {

    @Autowired
    private StorageService service;

    @Autowired
    private BoatService boatService;

    @GetMapping("/boats")
    public List<Boat> getBoatsFromStorage(@RequestParam("storageId") Long storageId) {
        return service.getBoatsFromStorage(storageId);
    }

    @PostMapping("/add/boat/{boatId}/to/storage/{storageId}")
    public Boat addBoatToStorage(@PathVariable("boatId") Long boatId, @PathVariable("storageId") Long storageId) {
        return service.addBoatToStorage(storageId, boatService.getBoat(boatId));
    }

    @PostMapping("/remove/boat/{boatId}/from/storage/{storageId}")
    public Boat removeBoatToStorage(@PathVariable("boatId") Long boatId, @PathVariable("storageId") Long storageId) {
        return service.removeBoatFromStorage(storageId, boatService.getBoat(boatId));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ServiceException.class, ResponseStatusException.class, PersistenceException.class})
    public Map<String, String> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        }
        else if (ex instanceof ServiceException) {
            errors.put(((ServiceException) ex).getAction(), ex.getMessage());
        } else {
            errors.put(((ResponseStatusException) ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }
}
