package be.ucll.ipminor341t.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LanguageController {

    @GetMapping
    public String main() {
        return "index";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }
    @GetMapping("/lng/")
    public String language(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }

}
