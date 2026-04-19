package com.etudiant.evenements.controller;

import com.etudiant.evenements.model.Event;
import com.etudiant.evenements.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/recherche")
    public String rechercher(@RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "type", required = false) String type,
                             HttpSession session,
                             Model model) {
        // Check if user is logged in
        if (session.getAttribute("userId") == null) {
            return "redirect:/connexion";
        }

        List<Event> events;

        if (type != null && !type.isEmpty()) {
            events = eventService.getEventsByType(type);
            model.addAttribute("searchType", type);
        } else {
            events = eventService.searchEvents(keyword);
            model.addAttribute("keyword", keyword);
        }

        model.addAttribute("events", events);
        model.addAttribute("userNom", session.getAttribute("userNom"));

        return "recherche";
    }
}