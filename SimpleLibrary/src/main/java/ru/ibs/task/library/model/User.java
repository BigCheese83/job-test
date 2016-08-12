package ru.ibs.task.library.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class User implements Serializable {

    private Long id;
    private String name;
    private List<Document> userDocs;

    public User() {}

    public User(String name, List<Document> docs) {
        this.name = name;
        this.userDocs = docs;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    @JoinTable(name = "USER_DOC",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "DOC_ID", referencedColumnName="id"))
    public List<Document> getUserDocs() {
        return userDocs;
    }

    public void setUserDocs(List<Document> userDocs) {
        this.userDocs = userDocs;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userDocs=" + userDocs +
                '}';
    }

    @PreRemove
    private void removeUserFromDocuments() {
        for (Document d : getUserDocs()) {
            d.getUsers().remove(this);
        }
    }
}
