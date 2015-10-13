package dat255.busster.Objects;

/**
 * Created by elias on 2015-10-13.
 */
public class Preference {
    private int _id;
    private String _key;
    private String _value;

    public Preference(String _key, String _value) {
        this._key = _key;
        this._value = _value;
    }
    public Preference(int _id, String _key, String _value) {
        this._id = _id;
        this._key = _key;
        this._value = _value;
    }



    public int get_id() {
        return _id;
    }

    public String get_key() {
        return _key;
    }

    public String get_value() {
        return _value;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public void set_value(String _value) {
        this._value = _value;
    }
}
