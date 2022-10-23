package com.example.postservice.controller;

import com.example.postservice.domain.mapper.UserMapper;
import com.example.postservice.service.FollowerService;
import com.example.postservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.stream.Collectors.toList;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/post/followLink")
public class FollowerController {

    private final UserService userService;
    private final FollowerService  followerService;

    public FollowerController(UserService userService, FollowerService followerService) {
        this.userService = userService;
        this.followerService = followerService;
    }

    /**
     * User1 follow the user2
     * @param idUser1 : Id of the user who wants to follow the user 2
     * @param idUser2 : id user
     */
    @PostMapping("{idUser1}/follow/{idUser2}")
    public ResponseEntity<?> follow(@PathVariable Long idUser1, @PathVariable Long idUser2){
        var user1 = userService.getById(idUser1);
        if(user1.isEmpty()) return new ResponseEntity<>("User " + idUser1 + " not found", HttpStatus.NOT_FOUND);

        var user2 = userService.getById(idUser2);
        if(user2.isEmpty()) return new ResponseEntity<>("User " + idUser2 + " not found", HttpStatus.NOT_FOUND);


        var alreadyFollow = followerService.getByFollowerAndUser(user1.get(), user2.get());
        if(alreadyFollow != null) return new ResponseEntity<>("You already follow this user", HttpStatus.FORBIDDEN);

        followerService.follow(user1.get(), user2.get());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("{idUser1}/follow/{idUser2}")
    public ResponseEntity<String> isFollowing(@PathVariable Long idUser1, @PathVariable Long idUser2){
        var user1 = userService.getById(idUser1);
        var user2 = userService.getById(idUser2);

        var alreadyFollow = followerService.getByFollowerAndUser(user1.get(), user2.get());
        if(alreadyFollow != null) {
            return new ResponseEntity<String>(HttpStatus.OK);
        }
        return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
    }

    /**
     * User1 unfollow the user2
     * @param idUser1 : Id of the user who wants to unfollow the user 2
     * @param idUser2 : id user
     */
    @PostMapping("{idUser1}/unfollow/{idUser2}")
    public ResponseEntity<?> unfollow(@PathVariable Long idUser1, @PathVariable Long idUser2){
        var user1 = userService.getById(idUser1);
        if(user1.isEmpty()) return new ResponseEntity<>("User " + idUser1 + " not found", HttpStatus.NOT_FOUND);

        var user2 = userService.getById(idUser2);
        if(user2.isEmpty()) return new ResponseEntity<>("User " + idUser2 + " not found", HttpStatus.NOT_FOUND);

        followerService.unfollow(user1.get(), user2.get());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("{userId}/followers")
    public ResponseEntity<?> getAllFollowers(@PathVariable Long userId){
        var user = userService.getById(userId);
        if(user.isEmpty()) return new ResponseEntity<>("User " + userId + " not found", HttpStatus.NOT_FOUND);


        var followers = userService.getAllFollowers(userId)
                .stream()
                .map(UserMapper::modelToResponse)
                .collect(toList());

        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @GetMapping("{userId}/subscriptions")
    public ResponseEntity<?> getAllSubscriptions(@PathVariable Long userId){
        var user = userService.getById(userId);
        if(user.isEmpty()) return new ResponseEntity<>("User " + userId + " not found", HttpStatus.NOT_FOUND);


        var followers = userService.getAllSubscriptions(userId)
                .stream()
                .map(UserMapper::modelToResponse)
                .collect(toList());

        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

}
