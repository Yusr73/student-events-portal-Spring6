package com.etudiant.evenements.repository;

import com.etudiant.evenements.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EventRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        createTable();
    }

    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS events (
                id INT AUTO_INCREMENT PRIMARY KEY,
                titre VARCHAR(200) NOT NULL,
                description VARCHAR(500),
                type VARCHAR(50),
                date VARCHAR(50),
                lieu VARCHAR(200)
            )
        """;
        jdbcTemplate.execute(sql);
    }

    // WORKING SEARCH - simple version
    public List<Event> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }

        String sql = "SELECT * FROM events WHERE " +
                "LOWER(titre) LIKE ? OR " +
                "LOWER(description) LIKE ? OR " +
                "LOWER(type) LIKE ?";

        String searchPattern = "%" + keyword.toLowerCase().trim() + "%";
        return jdbcTemplate.query(sql, new EventRowMapper(),
                searchPattern, searchPattern, searchPattern);
    }

    public List<Event> findByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return findAll();
        }
        String sql = "SELECT * FROM events WHERE LOWER(type) = ? ORDER BY date ASC";
        return jdbcTemplate.query(sql, new EventRowMapper(), type.toLowerCase());
    }

    public List<Event> findAll() {
        String sql = "SELECT * FROM events ORDER BY date ASC";
        return jdbcTemplate.query(sql, new EventRowMapper());
    }

    private static class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event event = new Event();
            event.setId(rs.getInt("id"));
            event.setTitre(rs.getString("titre"));
            event.setDescription(rs.getString("description"));
            event.setType(rs.getString("type"));
            event.setDate(rs.getString("date"));
            event.setLieu(rs.getString("lieu"));
            return event;
        }
    }
}