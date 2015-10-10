package dat255.app.buzzter.Resources;



/**
 * Created by elias on 2015-09-25.
 */
public class Constants {


    public static final String SERVER_IP = "192.168.0.202";

    public static final String SERVER_PORT = "3000";


    public class SocketEvents {
        //
        public static final String AUTHENTICATE = "authenticate";
        public static final String AUTHORIZED = "authorized";
        public static final String UNAUTHORIZED = "unauthorized";

        public static final String GET_POSTS = "getPosts";
        public static final String GET_POST = "getPost";
        public static final String SAVE_POST = "savePost";
        public static final String INC_VOTES_UP = "incVotesUp";
        public static final String DEC_VOTES_UP = "decVoteUp";
        public static final String INC_VOTES_DOWN = "incVotesDown";
        public static final String DEC_VOTES_DOWN = "decVoteDown";


        public static final String GET_COMMENTS = "getComments";
        public static final String SAVE_COMMENT = "saveComment";


        public static final String GET_BUSES = "getBuses";
        public static final String GET_BUS = "getBus";
        public static final String GET_BUS_NEXT_STOP = "getBusNextStop";
        public static final String GET_BUSES_GPS = "getBusesGPS";
        public static final String GET_BUS_GPS = "getBusGPS";
    }


}
