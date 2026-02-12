package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;

@Entity
@Table(name = "annonce")
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Titre nécessaire")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 200)
    private String adress;

    @Column(nullable = false, length = 100)
    private String mail;

    @Column(nullable = false)
    private Timestamp date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    public Annonce() {
    }

    public Annonce(String title, String description, String adress, String mail) {
        this.title = title;
        this.description = description;
        this.adress = adress;
        this.mail = mail;
        this.date = new Timestamp(System.currentTimeMillis());
        this.status = Status.DRAFT;
    }

    public Annonce(int id, String title, String description, String adress, String mail, Timestamp date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.adress = adress;
        this.mail = mail;
        this.date = date;
        this.status = Status.DRAFT;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Annonce{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", adress='" + adress + '\'' +
                ", mail='" + mail + '\'' +
                ", date=" + date +
                '}';
    }
}
