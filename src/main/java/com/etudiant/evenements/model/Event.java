package com.etudiant.evenements.model;

public class Event {
    private int id;
    private String titre;
    private String description;
    private String type;
    private String date;
    private String lieu;

    // Constructors
    public Event() {}

    public Event(String titre, String description, String type, String date, String lieu) {
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.date = date;
        this.lieu = lieu;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
}