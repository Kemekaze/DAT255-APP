package dat255.app.buzzter.Events;

import java.util.List;

import dat255.app.buzzter.Objects.Post;

/**
 * Created by elias on 2015-09-28.
 */
public class PostsEvent {

    public final List<Post> posts;
    public PostsEvent(List<Post> posts){
        this.posts = posts;
    }

}
