package dat255.app.buzzter.Events;

import java.util.List;

import dat255.app.buzzter.Objects.Post;

/**
 * Created by elias on 2015-09-28.
 */
public class PostsEvent {

    public List<Post> posts;
    public int eventType = 0;
    /* 0 = (add) add to end of list and keep existing
     * 1 = (refresh) add new ones and delete existing
     * 2 = (load new) add to beginning of list and keep existing
     *
     */
    public PostsEvent(List<Post> posts, int eventType){
        this.posts = posts;
        this.eventType = eventType;
    }
    public PostsEvent(List<Post> posts){
        this.posts = posts;
    }

}
