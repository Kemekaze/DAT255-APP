package dat255.busster.Events;

import java.util.List;

import dat255.busster.Objects.Post;

/**
 * Created by elias on 2015-10-20.
 */
public class CommentsEvent {

    public List<Post.Comment> comments;
    public int eventType = 0;
    /* 0 = (add) add to end of list and keep existing
     * 1 = (refresh) add new ones and delete existing
     * 2 = (load new) add to beginning of list and keep existing
     *
     */
    public CommentsEvent(List<Post.Comment> comments, int eventType){
        this.comments = comments;
        this.eventType = eventType;
    }
    public CommentsEvent(List<Post.Comment> comments){
        this.comments = comments;
    }


}
