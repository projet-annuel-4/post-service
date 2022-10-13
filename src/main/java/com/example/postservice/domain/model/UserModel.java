package com.example.postservice.domain.model;

public class UserModel {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer nbFollowers;
    private Integer nbSubscriptions;

    public Long getId() {
        return id;
    }

    public UserModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserModel setLastName(String lastName) {
        this.lastName = lastName;
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

    public Integer getNbSubscriptions() {
        return nbSubscriptions;
    }

    public UserModel setNbSubscriptions(Integer nbSubscriptions) {
        this.nbSubscriptions = nbSubscriptions;
        return this;
    }
}
