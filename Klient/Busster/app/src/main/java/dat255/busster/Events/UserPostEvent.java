package dat255.busster.Events;

import java.util.List;

import dat255.busster.Objects.UserPost;

/**
 * Created by elias on 2015-10-19.
 */
public class UserPostEvent {
    public List<UserPost> userPosts;

    public UserPostEvent(List<UserPost> userPosts){
        this.userPosts = userPosts;

    }
}
