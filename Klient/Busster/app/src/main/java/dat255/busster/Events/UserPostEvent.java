package dat255.busster.Events;

import java.util.List;

import dat255.busster.Objects.UserPost;

/**
 * handling the events of type userpost.
 */
public class UserPostEvent {
    public List<UserPost> userPosts;

    public UserPostEvent(List<UserPost> userPosts){
        this.userPosts = userPosts;

    }
}
