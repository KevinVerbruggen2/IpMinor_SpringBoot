package be.ucll.ipminor341t.web;

import be.ucll.ipminor341t.domain.Team;
import be.ucll.ipminor341t.domain.service.TeamService;
import be.ucll.ipminor341t.generic.ServiceException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
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
@RequestMapping("/api/team")
public class TeamRestController {


    @Autowired
    private TeamService service;

    @GetMapping("/overview")
    public List<Team> getAllTeams() {
        return service.getAllTeams();
    }

    @PostMapping("/add")
    public Team addTeam(@Valid @RequestBody TeamDto team){
        return service.createTeam(team);
    }

    @PutMapping("/update/{id}")
    public Team updateTeam(@PathVariable long id, @RequestBody TeamDto updatedTeam) {
        return service.updateTeam(id, updatedTeam);
    }

    @DeleteMapping("/delete/{id}")
    public Team deleteTeam(@PathVariable long id) {
        return service.deleteTeam(id);
    }

    @GetMapping("/search/{number}")
    public List<Team> getTeamsWithLessParticipants(@PathVariable int number) {
        return service.findByNumberOfCrewLessThanOrderByNumberOfCrewDesc(number);
    }

    @GetMapping("/search")
    public List<Team> getTeamsByCategory(@RequestParam String category) {
        return service.findByCategoryIgnoreCase(category);
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
