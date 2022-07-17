package com.example.postservice.data.request;

public class UserRequest {

    private String firstname;
    private String lastname;
    private String email;
    private Integer nbFollowers;
    private Integer nbSubscription;

    public String getFirstname() {
        return firstname;
    }

    public UserRequest setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserRequest setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getNbFollowers() {
        return nbFollowers;
    }

    public UserRequest setNbFollowers(Integer nbFollowers) {
        this.nbFollowers = nbFollowers;
        return this;
    }

    public Integer getNbSubscription() {
        return nbSubscription;
    }

    public UserRequest setNbSubscription(Integer nbSubscription) {
        this.nbSubscription = nbSubscription;
        return this;
    }
}
