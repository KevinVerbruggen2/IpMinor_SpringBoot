package be.ucll.ipminor341t.web;


import be.ucll.ipminor341t.domain.Team;
import be.ucll.ipminor341t.domain.service.RegattaService;
import be.ucll.ipminor341t.generic.ServiceException;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api/regatta-team")
public class RegattaTeamRestController {

    @Autowired
    private RegattaService regattaService;

    @PostMapping("/add/team/{teamId}/to/regatta/{regattaId}")
    public Team addTeamToRegatta(@PathVariable Long teamId, @PathVariable Long regattaId) {
        return regattaService.addTeamToRegatta(regattaId, teamId);
    }

    @PostMapping("/remove/team/{teamId}/from/regatta/{regattaId}")
    public Team removeTeamFromRegatta(@PathVariable Long teamId, @PathVariable Long regattaId) {
        return regattaService.removeTeamFromRegatta(regattaId, teamId);
    }

    @GetMapping("/teams")
    public Set<Team> getTeamsFromRegatta(@RequestParam Long regattaId) {
        return regattaService.getTeamsFromRegatta(regattaId);
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
        }  else if(ex.getCause() instanceof ConstraintViolationException) {
            errors.put("databaseError", "Deze team is niet uniek (Bestaat al)");
        }
        else {
            errors.put(((ResponseStatusException) ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }

}
