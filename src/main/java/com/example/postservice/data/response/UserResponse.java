package com.example.postservice.data.response;


public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer nbFollowers;
    private Integer nbSubscription;

    public Long getId() {
        return id;
    }
    public UserResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstname() {
        return firstName;
    }

    public UserResponse setFirstname(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastname() {
        return lastName;
    }

    public UserResponse setLastname(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getNbFollowers() {
        return nbFollowers;
    }

    public UserResponse setNbFollowers(Integer nbFollowers) {
        this.nbFollowers = nbFollowers;
        return this;
    }

    public Integer getNbSubscription() {
        return nbSubscription;
    }

    public UserResponse setNbSubscription(Integer nbSubscription) {
        this.nbSubscription = nbSubscription;
        return this;
    }
}
