package dat255.app.buzzter.Objects;

/**
 * Created by elias on 2015-10-10.
 */
public class Vote {
    private int _id;
    private String _postId;
    private Boolean _like;

    public Vote(int _id,String _postId,Boolean _like) {
        this._id = _id;
        this._like = _like;
        this._postId = _postId;
    }

    public Vote(String _postId,Boolean _like) {
        this._like = _like;
        this._postId = _postId;
    }

    public Vote() {
    }

    public int get_id() {
        return _id;
    }

    public Boolean get_like() {
        return _like;
    }

    public void set_like(Boolean _like) {
        this._like = _like;
    }

    public String get_postId() {
        return _postId;
    }

    public void set_postId(String _postId) {
        this._postId = _postId;
    }
}
