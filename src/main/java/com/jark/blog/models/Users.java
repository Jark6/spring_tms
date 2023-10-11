package com.jark.blog.models;

import jakarta.persistence.*;

@Entity
@Table(name="USERS")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;
    @Column
    private String first_name;
    @Column
    private String second_name;
    @Column
    private String family_name;
    @Column
    private String login;

    public Users(){
    }

    public Users(String first_name, String second_name, String family_name, String login) {
        this.first_name = first_name;
        this.second_name = second_name;
        this.family_name = family_name;
        this.login = login;
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
}
