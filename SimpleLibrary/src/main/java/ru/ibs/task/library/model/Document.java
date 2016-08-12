package ru.ibs.task.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Document implements Serializable {

    private Long id;
    private String title;
    private String ISBN;
    private List<User> users;

    public Document() {}

    public Document(String title, String ISBN) {
        this.title = title;
        this.ISBN = ISBN;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "isbn")
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    @ManyToMany(mappedBy = "userDocs")
    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", ISBN='" + ISBN + '\'' +
                '}';
    }

    @PreRemove
    private void removeDocumentFromUsers() {
        for (User u : getUsers()) {
            u.getUserDocs().remove(this);
        }
    }

}