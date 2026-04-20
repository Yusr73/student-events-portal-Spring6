package com.etudiant.evenements.controller;

import com.etudiant.evenements.entity.User;
import com.etudiant.evenements.service.UserService;
import com.etudiant.evenements.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/inscription")
    public String showInscription(Model model) {
        model.addAttribute("user", new User());
        return "inscription";
    }

    @PostMapping("/inscription")
    public String processInscription(@ModelAttribute("user") User user,
                                     BindingResult result,
                                     Model model) {
        userValidator.validate(user, result);

        if (result.hasErrors()) {
            return "inscription";
        }

        boolean success = userService.register(user.getNom(), user.getEmail(), user.getPassword());

        if (success) {
            String successMsg = messageSource.getMessage("register.success", null, LocaleContextHolder.getLocale());
            model.addAttribute("success", successMsg);
            return "connexion";
        } else {
            String errorMsg = messageSource.getMessage("register.error", null, LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMsg);
            return "inscription";
        }
    }

    @GetMapping("/connexion")
    public String showConnexion() {
        return "connexion";
    }

    @PostMapping("/connexion")
    public String processConnexion(@RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   HttpSession session,
                                   Model model) {
        User user = userService.login(email, password);

        if (user != null) {
            session.setAttribute("userId", user.getEmail());
            session.setAttribute("userNom", user.getNom());
            session.setMaxInactiveInterval(3600);
            return "redirect:/";
        } else {
            String errorMsg = messageSource.getMessage("error.login", null, LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMsg);
            return "connexion";
        }
    }

    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}