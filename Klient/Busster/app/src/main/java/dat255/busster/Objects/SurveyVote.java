package dat255.busster.Objects;

/**
 * Containing the information from a vote in a survey.
 * The information includes the id:s and what kind
 * the user voted for in the survey;
 */
public class SurveyVote {

    private int _id;
    private String _postId;
    private int _option;

    /**
     * Setting th values.
     * @param _id id of the post in form of a integer
     * @param _postId id of the post in form of a string
     * @param _option answer the user voted for.
     */
    public SurveyVote(int _id,String _postId,int _option) {
        this._id = _id;
        this._option = _option;
        this._postId = _postId;
    }

    /**
     *
     * @param _postId id of the post in form of a string
     * @param _option answer the user voted for.
     */
    public SurveyVote(String _postId,int _option) {
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
