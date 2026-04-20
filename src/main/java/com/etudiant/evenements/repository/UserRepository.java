package com.etudiant.evenements.repository;

import com.etudiant.evenements.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class UserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(User user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    public User findByEmail(String email) {
        String hql = "FROM User WHERE email = :email";
        TypedQuery<User> query = sessionFactory.getCurrentSession()
                .createQuery(hql, User.class);
        query.setParameter("email", email);

        List<User> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public boolean existsByEmail(String email) {
        String hql = "SELECT COUNT(*) FROM User WHERE email = :email";
        Long count = sessionFactory.getCurrentSession()
                .createQuery(hql, Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

    public List<User> findAll() {
        String hql = "FROM User";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, User.class)
                .getResultList();
    }
}