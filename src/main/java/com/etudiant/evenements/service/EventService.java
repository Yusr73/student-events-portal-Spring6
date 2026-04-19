package com.etudiant.evenements.service;

import com.etudiant.evenements.model.Event;
import com.etudiant.evenements.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> searchEvents(String keyword) {
        if (keyword == null || keyword.trim().length() < 2) {
            return eventRepository.findAll();
        }
        return eventRepository.search(keyword.toLowerCase().trim());
    }

    // Add this missing method
    public List<Event> getEventsByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return eventRepository.findAll();
        }
        return eventRepository.findByType(type);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}