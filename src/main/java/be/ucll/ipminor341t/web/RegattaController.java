package be.ucll.ipminor341t.web;

import be.ucll.ipminor341t.domain.Regatta;
import be.ucll.ipminor341t.domain.RegattaRepository;
import be.ucll.ipminor341t.domain.service.RegattaService;
import be.ucll.ipminor341t.generic.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Controller
public class RegattaController {

    private static Logger LOGGER = LoggerFactory.getLogger(RegattaRepository.class);

    private final RegattaService regattaService;

    @Autowired
    public RegattaController(RegattaService regattaService) {
        this.regattaService = regattaService;
    }

    private void createSampleData() {
        RegattaDto racingEvent = new RegattaDto();
        racingEvent.setName("Racing event");
        racingEvent.setOrganizingClub("UCLL");
        racingEvent.setDate(new Date(System.currentTimeMillis() + 1000000));
        racingEvent.setMaxTeams("10");
        racingEvent.setCategory("A");

        RegattaDto tripEvent = new RegattaDto();
        tripEvent.setName("Trip event");
        tripEvent.setOrganizingClub("UCLL");
        tripEvent.setDate(new Date(System.currentTimeMillis() + 1000000));
        tripEvent.setMaxTeams("10");
        tripEvent.setCategory("B");

        regattaService.createRegatta(racingEvent);
        regattaService.createRegatta(tripEvent);
    }

    @GetMapping(value = {"/regatta/overview", "/regatta/overview/{page}"})
    public String overview(@PathVariable(required = false) String page, @RequestParam(defaultValue = "5") String size, HttpServletRequest request, Model model) {

        page = Objects.requireNonNullElse(page, "0");

        Page<Regatta> allRegattas = regattaService.getRegattas(size, page, Sort.unsorted());
        model.addAttribute("page", allRegattas.getPageable());

        model.addAttribute("regattas", allRegattas);
        request.getSession().setAttribute("sizeRegattas", size);
        return "overviewRegatta";
    }

    @GetMapping("/regatta/overview/reset")
    public String reset(HttpServletRequest request) {
        request.getSession().removeAttribute("sizeRegattas");
        return "redirect:/regatta/overview";
    }

    @GetMapping("/regatta/add")
    public String add(Model model) {
        model.addAttribute("regattaDto", new RegattaDto());
        return "addRegatta";
    }

    @PostMapping("/regatta/add")
    public String add(@Valid RegattaDto regatta, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("regattaDto", regatta);
                return "addRegatta";
            }

            regattaService.createRegatta(regatta);
            return "redirect:/regatta/overview";
        }catch(DataIntegrityViolationException exc) {
            model.addAttribute("errorUnique", true);
            return "addRegatta";
        }
    }

    @GetMapping("/regatta/update")
    public String update(@RequestParam("id") long id, Model model) {
        try {
            Regatta regatta = regattaService.getRegatta(id);
            model.addAttribute("regattaDto", toDto(regatta));
        } catch (RuntimeException exc) {
            model.addAttribute("error", exc.getMessage());
        }

        return "updateRegatta";
    }

    private RegattaDto toDto(Regatta regatta) {
        RegattaDto dto = new RegattaDto();

        dto.setId(regatta.getId());
        dto.setName(regatta.getName());
        dto.setOrganizingClub(regatta.getOrganizingClub());
        dto.setDate(regatta.getDate());
        dto.setMaxTeams(regatta.getMaxTeams());
        dto.setCategory(regatta.getCategory());
        return dto;
    }

    @PostMapping("/regatta/update")
    public String update(@RequestParam("id") long id, @Valid RegattaDto regatta, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                LOGGER.error("ERRORS UPDATING");

                regatta.setId(id);
                model.addAttribute("regattaDto", regatta);
                return "updateRegatta";
            }

            regattaService.updateRegatta(id, regatta);
            return "redirect:/regatta/overview";
        } catch(DataIntegrityViolationException exc) {
            model.addAttribute("errorUnique", true);
        }  catch (ServiceException exc) {
            model.addAttribute("error", exc.getMessage());
        }
        return "updateRegatta";
    }

    @GetMapping("/regatta/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        return "deleteRegatta";
    }

    @PostMapping("/regatta/delete/{id}")
    public String deleteConfirm(@PathVariable("id") long id, @RequestParam("action") String action, Model model) {
        try {
            if(Objects.equals(action, "submit")) {
                regattaService.deleteRegatta(id);
            }
        } catch (ServiceException exc) {
            model.addAttribute("error", exc.getMessage());
        } catch (DataIntegrityViolationException exc) {
            model.addAttribute("errorUnique", true);
        }
        return "redirect:/regatta/overview";
    }


    @GetMapping("/regatta/sort/{sort}")
    public String sort(@PathVariable("sort") String sort, Model model, HttpServletRequest request) {

        String size = (String) request.getSession().getAttribute("sizeRegattas");
        Sort.Order order = (Objects.equals(sort, "name") ?  new Sort.Order(Sort.Direction.ASC, "name").ignoreCase() : Sort.Order.asc("date"));
        Page<Regatta> allRegattas = regattaService.getRegattas(size, "0", Sort.by(order));
        model.addAttribute("page", allRegattas.getPageable());
        model.addAttribute("regattas", allRegattas);
        return "overviewRegatta";
    }


    @GetMapping("/regatta/search")
    public String search(@RequestParam("dateBefore") String dateBefore, @RequestParam("dateAfter") String dateAfter, @RequestParam("category") String category, Model model, HttpServletRequest request) {
        String size = (String) request.getSession().getAttribute("sizeRegattas");
        Page<Regatta> allRegattas;

        try {
            if(dateBefore.isBlank() && dateAfter.isBlank()) {
                allRegattas = regattaService.getRegattaByCategoryContainsIgnoreCase(category, size);
            } else if(dateAfter.isBlank()){
                allRegattas = regattaService.getRegattaByEndDateAndCategoryContainsIgnoreCase(new SimpleDateFormat("yyyy-MM-dd").parse(dateBefore), category, size);
            } else if(dateBefore.isBlank()) {
                allRegattas = regattaService.getRegattaByStartDateAndCategoryContainsIgnoreCase(new SimpleDateFormat("yyyy-MM-dd").parse(dateAfter), category, size);
            } else {
                allRegattas = regattaService.getRegattaByDateAndCategoryContainsIgnoreCase(new SimpleDateFormat("yyyy-MM-dd").parse(dateAfter),new SimpleDateFormat("yyyy-MM-dd").parse(dateBefore), category, size);
            }
        } catch (ParseException e) {
            allRegattas = regattaService.getRegattas(size, "0", Sort.unsorted());
        }

        if(allRegattas.getTotalElements() <= 0) {
            allRegattas = regattaService.getRegattas(size, "0", Sort.unsorted());
            model.addAttribute("errorNotFound", true);
        }

        model.addAttribute("page", allRegattas.getPageable());
        model.addAttribute("regattas", allRegattas);
        return "overviewRegatta";
    }



}