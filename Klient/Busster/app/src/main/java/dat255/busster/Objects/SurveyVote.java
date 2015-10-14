package dat255.busster.Objects;

/**
 * Created by Rasmus on 2015-10-14.
 */
public class SurveyVote {

    private int _id;
    private String _postId;
    private int _option;

    public SurveyVote(int _id,String _postId,int _option) {
        this._id = _id;
        this._option = _option;
        this._postId = _postId;
    }

    public SurveyVote(String _postId,Boolean _like) {
        this._option = _option;
        this._postId = _postId;
    }

    public SurveyVote() {
    }

    public int get_id() {
        return _id;
    }

    public int get_vote() {
        return _option;
    }

    public void set_vote(int _option) {
        this._option = _option;
    }

    public String get_postId() {
        return _postId;
    }

    public void set_postId(String _postId) {
        this._postId = _postId;
    }
}
