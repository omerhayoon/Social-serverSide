package com.ashcollege.controllers;

import com.ashcollege.entities.Post;
import com.ashcollege.entities.User;
import com.ashcollege.responses.*;
import com.ashcollege.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ashcollege.utils.Errors.*;

@RestController
public class GeneralController {

    @Autowired
    private DbUtils dbUtils;

    @RequestMapping("/")
    public String test() {
        return "server is running OK";
    }

    @RequestMapping("/sign-in")
    public LoginResponse checkUser(String username, String password) {
        boolean success;
        success = dbUtils.signIn(username, password);
        return new LoginResponse(success);
    }

    @RequestMapping("/sign-up")
    public RegisterResponse register(String username, String password) {
        boolean success = false;
        Integer errorCode = null;
        Integer id = null;
        if (username != null) {
            if (password != null) {
                if (usernameAvailable(username).isAvailable()) {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    dbUtils.registerUser(user);
                    id = user.getId();
                    success = true;
                } else {
                    errorCode = ERROR_USERNAME_NOT_AVAILABLE;
                }
            } else {
                errorCode = ERROR_MISSING_PASSWORD;
            }
        } else {
            errorCode = ERROR_MISSING_USERNAME;
        }
        return new RegisterResponse(success, errorCode, id);
    }

    @RequestMapping("/username-available")
    public UsernameAvailableResponse usernameAvailable(String username) {
        boolean success = false;
        Integer errorCode = null;
        boolean available = false;
        if (username != null) {
            available = dbUtils.usernameAvailable(username);
            success = true;
        } else {
            errorCode = ERROR_MISSING_USERNAME;
        }
        return new UsernameAvailableResponse(success, errorCode, available);

    }


    @RequestMapping("get-user-search-result")
    public UserFoundSearchResponse getUserSearchResult(String search) {
        boolean success = false;
        Integer errorCode = null;
        List<User> found = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            try {
                found = dbUtils.getUserSearchResult(search);
                success = true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            errorCode = ERROR_MISSING_SEARCH;
        }
        return new UserFoundSearchResponse(success, errorCode, found);
    }

    @RequestMapping("follow")
    public BasicResponse follow(String username, String name) {
        boolean success = false;
        Integer errorCode = null;
        boolean added;
        if (name != null && !name.isEmpty()) {
            added = dbUtils.follow(username, name);
            System.out.println(added);
            success = true;
        } else {
            errorCode = ERROR_FOLLOW_DONT_SUCCESS;
        }
        return new BasicResponse(success, errorCode);

    }

    @RequestMapping("/all-following")
    public AllFollowingResponse allFollowing(String username) {
        boolean success = false;
        Integer errorCode = null;
        List<User> allFollowing = new ArrayList<>();
        if (username != null) {
            allFollowing = dbUtils.getAllFollowing(username);
            success = true;
        } else {
            errorCode = ERROR_MISSING_USERNAME;
        }
        return new AllFollowingResponse(success, errorCode, allFollowing);

    }

    @RequestMapping("post")
    public BasicResponse addPost(String username, String post) {
        System.out.println("income post");
        boolean success = false;
        Integer errorCode = null;
        if (post != null) {
            success = dbUtils.addPost(username, post);
        } else {
            errorCode = FAIL_PUBLISH_POST;
        }
        return new BasicResponse(success, errorCode);
    }
//
    @RequestMapping("get-all-posts")
    public GetAllPostsResponse getAllPosts(String username) {
        boolean success = false;
        Integer errorCode = null;
        List<Post> allPosts = new ArrayList<>();
        if (username != null) {
            allPosts = dbUtils.getAllPosts(username);
            success = true;
        } else {
            errorCode = ERROR_GET_ALL_POSTS;
        }
        return new GetAllPostsResponse(success, errorCode, allPosts);

    }

    @RequestMapping("show-feed")
    public ShowFeedResponse showFeed(String username) {
        boolean success = false;
        Integer errorCode = null;
        List<Post> feed = new ArrayList<>();
        if (username != null) {
            feed = dbUtils.showFeed(username);
            success = true;
        } else {
            errorCode = ERROR_GET_FEED;
        }
        return new ShowFeedResponse(success, errorCode, feed);
    }


}
