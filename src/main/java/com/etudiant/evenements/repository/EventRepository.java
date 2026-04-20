package com.etudiant.evenements.repository;

import com.etudiant.evenements.entity.Event;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class EventRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Event event) {
        sessionFactory.getCurrentSession().persist(event);
    }

    public List<Event> findAll() {
        String hql = "FROM Event ORDER BY date ASC";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Event.class)
                .getResultList();
    }

    public List<Event> search(String keyword) {
        if (keyword == null || keyword.trim().length() < 2) {
            return findAll();
        }

        String searchTerm = keyword.toLowerCase().trim();

        // HQL query with case-insensitive search
        String hql = "FROM Event e WHERE " +
                "LOWER(e.titre) LIKE :pattern1 OR " +
                "LOWER(e.description) LIKE :pattern1 OR " +
                "LOWER(e.type) LIKE :pattern1";

        String pattern = "%" + searchTerm + "%";

        TypedQuery<Event> query = sessionFactory.getCurrentSession()
                .createQuery(hql, Event.class);
        query.setParameter("pattern1", pattern);

        return query.getResultList();
    }

    public List<Event> findByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return findAll();
        }

        String hql = "FROM Event WHERE LOWER(type) = :type ORDER BY date ASC";

        TypedQuery<Event> query = sessionFactory.getCurrentSession()
                .createQuery(hql, Event.class);
        query.setParameter("type", type.toLowerCase());

        return query.getResultList();
    }

    public Event findById(Long id) {
        return sessionFactory.getCurrentSession().get(Event.class, id);
    }

    public void deleteById(Long id) {
        Event event = findById(id);
        if (event != null) {
            sessionFactory.getCurrentSession().remove(event);
        }
    }
}