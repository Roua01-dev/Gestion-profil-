package com.example.gestionprofil;



public class Contact {
    private int id;
    private String nom;
    private String pseudo;
    private String phone;

    public Contact(int id, String nom, String pseudo, String phone) {
        this.id = id;
        this.nom = nom;
        this.pseudo = pseudo;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getPhone() {
        return phone;
    }
}
