package be.ucll.ipminor341t.web;

import be.ucll.ipminor341t.domain.Storage;
import be.ucll.ipminor341t.domain.service.StorageService;
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

import java.util.Objects;

@Controller
public class StorageController {

    private static Logger LOGGER = LoggerFactory.getLogger(StorageController.class);

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }
    private void createSampleData() {
        StorageDto storage = new StorageDto();
        storage.setName("Storage 1");
        storage.setPlace("2000");
        storage.setAvailableSpace("100");
        storage.setHeight("150");
        StorageDto storage1 = new StorageDto();
        storage1.setName("Storage 2");
        storage1.setPlace("3000");
        storage1.setAvailableSpace("200");
        storage1.setHeight("200");
        storageService.createStorage(storage);
        storageService.createStorage(storage1);
    }

    @GetMapping(value = {"/storage/overview/{page}", "/storage/overview"})
    public String overview(Model model, @PathVariable(required = false) String page, @RequestParam(defaultValue = "5") String size, HttpServletRequest request) {

        Sort sort = (Sort) request.getSession().getAttribute("sortStorage");
        String search = (String) request.getSession().getAttribute("sortSearch");
        page = Objects.requireNonNullElse(page, "0");

        Page<Storage> allStorages = search == null ? storageService.getStorages(page, size, sort == null ? Sort.unsorted() : sort) : storageService.getStoragesByName(search, size, page, sort == null ? Sort.unsorted() : sort);
        model.addAttribute("searchValue", search);
        model.addAttribute("page", allStorages.getPageable());
        model.addAttribute("storages", allStorages);
        request.getSession().setAttribute("sizeStorage", size);
        return "overviewStorage";
    }

    @GetMapping("/storage/add")
    public String addStorage(Model model) {
        model.addAttribute("storageDto", new StorageDto());
        return "addStorage";
    }

    @PostMapping("/storage/add")
    public String addStorage(@Valid StorageDto storage, BindingResult result, Model model) {

        try {
            if(result.hasErrors()) {
                model.addAttribute("storageDto", storage);
                return "addStorage";
            }

            storageService.createStorage(storage);
            return "redirect:overview";

        } catch(DataIntegrityViolationException ex) {
            model.addAttribute("errorUnique", true);
            return "addStorage";
        }
    }

    private StorageDto toDto(Storage storage) {
        StorageDto dto = new StorageDto();
        dto.setId(storage.getId());
        dto.setName(storage.getName());
        dto.setPlace(storage.getPlace());
        dto.setAvailableSpace(storage.getAvailableSpace());
        dto.setHeight(storage.getHeight());
        return dto;
    }

    @GetMapping("/error")
    public String error() {
        return "error.html";
    }

    @GetMapping("/storage/update/{id}")
    public String updateGet(@PathVariable("id") long id, Model model) {
        try {
            Storage storage = storageService.getStorage(id);
            model.addAttribute("storageDto", toDto(storage));
        } catch (RuntimeException exc) {
            model.addAttribute("error", exc.getMessage());
        }
        return "updateStorage";
    }

    @PostMapping("/storage/update/{id}")
    public String updatePost(@PathVariable("id") long id, @Valid StorageDto storage, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                LOGGER.error("ERRORS UPDATING");

                storage.setId(id);
                model.addAttribute("storageDto", storage);
                return "updateStorage";
            }

            storageService.updateStorage(id, storage);
            return "redirect:/storage/overview";
        }catch (DataIntegrityViolationException | ServiceException ex) {
            if(ex instanceof DataIntegrityViolationException) {
                model.addAttribute("errorUnique", true);
            } else {
                model.addAttribute("error", ex.getMessage());
            }
            return "updateStorage";
        }
    }

    @GetMapping("/storage/delete")
    public String deleteGet(@RequestParam("id") Long id, Model model) {
        model.addAttribute("storageDto", storageService.getStorage(id));
        return "deleteStorage";
    }

    @PostMapping("/storage/delete")
    public String deletePost(@RequestParam("id") Long id, @RequestParam("action") String action ,Model model) {
        if(Objects.equals(action, "submit")) {
            try {
                storageService.deleteStorage(id);
            } catch (ServiceException exc) {
                model.addAttribute("error", exc.getMessage());
                model.addAttribute("storageDto", storageService.getStorage(id));
                return "deleteStorage";
            }
        }
        return "redirect:/storage/overview";
    }

    @GetMapping("/storage/overview/reset")
    public String reset(HttpServletRequest request) {
        request.getSession().removeAttribute("sortStorage");
        request.getSession().removeAttribute("sortSearch");
        return "redirect:/storage/overview";
    }


    @GetMapping("/storage/sort/{sort}")
    public String sort(@PathVariable("sort") String sortMethod, Model model, HttpServletRequest request) {

        String size = (String) request.getSession().getAttribute("sizeStorage");
        String search = (String) request.getSession().getAttribute("sortSearch");

        Sort.Order order = (Objects.equals(sortMethod, "name") ?  new Sort.Order(Sort.Direction.ASC, "name").ignoreCase() : Sort.Order.desc("height"));
        request.getSession().setAttribute("sortStorage", Sort.by(order));
        Page<Storage> allStorages = search == null ? storageService.getStorages("0", size, Sort.by(order)) : storageService.getStoragesByName(search, size, "0", Sort.by(order));

        model.addAttribute("searchValue", search);
        model.addAttribute("page", allStorages.getPageable());
        model.addAttribute("storages", allStorages);
        return "overviewStorage";
    }

    @GetMapping("/storage/search")
    public String search(@RequestParam("searchValue") String searchValue, Model model, HttpServletRequest request) {
        String size = (String) request.getSession().getAttribute("sizeStorage");
        Page<Storage> storages = storageService.getStoragesByName(searchValue, size, "0", Sort.unsorted());
        if(storages.getTotalElements() <= 0) {
            storages = storageService.getStorages("0", size, Sort.unsorted());
            model.addAttribute("errorSearch", true);
            request.getSession().removeAttribute("sortSearch");
        }

        if (searchValue.isBlank()) {
            request.getSession().removeAttribute("sortSearch");
        } else {
            request.getSession().setAttribute("sortSearch", searchValue);
        }
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("page", storages.getPageable());
        model.addAttribute("storages", storages);
        return "overviewStorage";
    }


}