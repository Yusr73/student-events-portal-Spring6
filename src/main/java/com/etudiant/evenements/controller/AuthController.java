package com.etudiant.evenements.controller;

import com.etudiant.evenements.entity.User;
import com.etudiant.evenements.service.UserService;
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
import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    // ========== REGISTRATION ==========
    @GetMapping("/inscription")
    public String showInscription(Model model) {
        model.addAttribute("user", new User());
        return "inscription";
    }

    @PostMapping("/inscription")
    public String processInscription(@Valid @ModelAttribute("user") User user,
                                     BindingResult result,
                                     Model model) {
        // Check validation errors
        if (result.hasErrors()) {
            return "inscription";
        }

        // Check if email already exists
        if (userService.emailExists(user.getEmail())) {
            String errorMsg = messageSource.getMessage("error.email.exists", null, LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMsg);
            return "inscription";
        }

        // Register the user
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

    // ========== LOGIN ==========
    @GetMapping("/connexion")
    public String showConnexion(Model model) {
        model.addAttribute("user", new User());
        return "connexion";
    }

    @PostMapping("/connexion")
    public String processConnexion(@Valid @ModelAttribute("user") User user,
                                   BindingResult result,
                                   HttpSession session,
                                   Model model) {
        // Check validation errors (email format, password not empty)
        if (result.hasErrors()) {
            return "connexion";
        }

        // Check credentials against database
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());

        if (loggedInUser != null) {
            session.setAttribute("userId", loggedInUser.getEmail());
            session.setAttribute("userNom", loggedInUser.getNom());
            session.setMaxInactiveInterval(3600);
            return "redirect:/";
        } else {
            String errorMsg = messageSource.getMessage("error.login", null, LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMsg);
            return "connexion";
        }
    }

    // ========== LOGOUT ==========
    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}