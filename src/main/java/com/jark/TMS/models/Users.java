package com.jark.TMS.models;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="USERS")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long user_id;
    @Column
    private String first_name;
    @Column
    private String second_name;
    @Column
    private String family_name;
    @Column
    private String login;
    @Column
    private String passwordHash;
    @Transient
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comments> comments;

    @Column
    private String role;
    @Column
    private LocalDateTime timestamp_create;
    @Column
    private LocalDateTime timestamp_edit;
    public Users(){
    }

    public Users(String first_name, String second_name, String family_name, String login, String passwordHash, String password, String role, LocalDateTime timestamp_create, LocalDateTime timestamp_edit) {
        this.first_name = first_name;
        this.second_name = second_name;
        this.family_name = family_name;
        this.login = login;
        this.passwordHash=passwordHash;
        this.password = password;
        this.role=role;
        this.timestamp_create = timestamp_create;
        this.timestamp_edit = timestamp_edit;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }


    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPasswordHash(String password) {
        if (password != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.passwordHash = passwordEncoder.encode(password);
        }
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public List<Comments> getComments() {
        return comments;
    }
    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public LocalDateTime getTimestamp_create() {
        return timestamp_create;
    }

    public void setTimestamp_create(LocalDateTime timestamp_create) {
        this.timestamp_create = timestamp_create;
    }

    public LocalDateTime getTimestamp_edit() {
        return timestamp_edit;
    }

    public void setTimestamp_edit(LocalDateTime timestamp_edit) {
        this.timestamp_edit = timestamp_edit;
    }

    public StringBuilder getFullName(){
        StringBuilder fullName = new StringBuilder().append(getSecond_name()).append(" ").append(getFirst_name());
        return fullName;
    }
}
