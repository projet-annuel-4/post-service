package com.example.postservice.data.request;

public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private Integer nbFollowers;
    private Integer nbSubscription;

    public String getFirstName() {
        return firstName;
    }

    public UserRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRequest setLastName(String lastName) {
        this.lastName = lastName;
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
