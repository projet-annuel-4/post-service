package com.example.postservice.domain.model;

public class UserModel {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Integer nbFollowers;
    private Integer nbSubscription;

    public Long getId() {
        return id;
    }

    public UserModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public UserModel setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserModel setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getNbFollowers() {
        return nbFollowers;
    }

    public UserModel setNbFollowers(Integer nbFollowers) {
        this.nbFollowers = nbFollowers;
        return this;
    }

    public Integer getNbSubscription() {
        return nbSubscription;
    }

    public UserModel setNbSubscription(Integer nbSubscription) {
        this.nbSubscription = nbSubscription;
        return this;
    }
}
