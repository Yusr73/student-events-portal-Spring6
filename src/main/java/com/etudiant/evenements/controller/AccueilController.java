package com.etudiant.evenements.controller;

import com.etudiant.evenements.entity.Event;
import com.etudiant.evenements.service.EventService;
import com.etudiant.evenements.service.UserService;
import com.etudiant.evenements.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import java.util.List;

@Controller
public class AccueilController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String accueil(HttpSession session, HttpServletRequest request, Model model) {
        // Check if user is already in session
        String userNom = (String) session.getAttribute("userNom");

        // If not in session, check for remember me cookie
        if (userNom == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("rememberMe")) {
                        String email = cookie.getValue();
                        User user = userService.getUserByEmail(email);
                        if (user != null) {
                            session.setAttribute("userId", user.getEmail());
                            session.setAttribute("userNom", user.getNom());
                            session.setMaxInactiveInterval(30 * 24 * 60 * 60);
                            userNom = user.getNom();
                        }
                        break;
                    }
                }
            }
        }

        if (userNom != null) {
            model.addAttribute("user", userNom);
        }

        // Load all events from database
        List<Event> allEvents = eventService.getAllEvents();
        model.addAttribute("allEvents", allEvents);

        // Count events by type
        long conferenceCount = allEvents.stream().filter(e -> "conference".equals(e.getType())).count();
        long workshopCount = allEvents.stream().filter(e -> "workshop".equals(e.getType())).count();
        long socialCount = allEvents.stream().filter(e -> "social".equals(e.getType())).count();

        model.addAttribute("conferenceCount", conferenceCount);
        model.addAttribute("workshopCount", workshopCount);
        model.addAttribute("socialCount", socialCount);

        return "accueil";
    }
}