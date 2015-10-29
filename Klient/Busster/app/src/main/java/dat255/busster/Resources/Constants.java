package dat255.busster.Resources;


import java.util.Arrays;
import java.util.List;

/**
 * Created by elias on 2015-09-25.
 * Class that all constants are declared
 */
public class Constants {


    public static final String SERVER_IP = "81.232.129.196";
    public static final String SERVER_PORT = "3000";


    public class SocketEvents {

        public static final String AUTHENTICATE = "authenticate";
        public static final String AUTHORIZED = "authorized";
        public static final String UNAUTHORIZED = "unauthorized";

        public static final String GET_POSTS = "getPosts";
        public static final String GET_POST = "getPost";
        public static final String SAVE_POST = "savePost";
        public static final String INC_VOTES_UP = "incVotesUp";
        public static final String DEC_VOTES_UP = "decVotesUp";
        public static final String INC_VOTES_DOWN = "incVotesDown";
        public static final String DEC_VOTES_DOWN = "decVotesDown";


        public static final String GET_COMMENTS = "getComments";
        public static final String SAVE_COMMENT = "saveComment";


        public static final String UPDATE_SURVEY = "updateSurvey";


        public static final String GET_BUSES = "getBuses";
        public static final String GET_BUS = "getBus";
        public static final String GET_BUS_NEXT_STOP = "getBusNextStop";
        public static final String GET_BUSES_GPS = "getBusesGPS";
        public static final String GET_BUS_GPS = "getBusGPS";

        public static final String GET_STOPS = "getStops";
        public static final String SET_USER_STOP = "setUserStop";

    }
    public class DB{
        public static final int VERSION = 6;
        public static final String DB_NAME = "busster.db";
        public class PREFERENCES{
            public static final String DISPLAY_NAME = "displayName";
            public static final String DESTINATION = "destination";

            public static final String TABLE = "preferences";
            public static final String COLUMN_ID = "_id";
            public static final String COLUMN_KEY = "key";
            public static final String COLUMN_VALUE = "value";
        }
        public class VOTES{
            public static final String TABLE = "votes";
            public static final String COLUMN_ID = "_id";
            public static final String COLUMN_POST_ID = "postId";
            public static final String COLUMN_LIKE = "like";
        }
        public class SURVEY{
            public static final String TABLE = "survey";
            public static final String COLUMN_ID = "_id";
            public static final String COLUMN_POST_ID = "postId";
            public static final String COLUMN_OPTION = "like";
        }
    }
    public static List<String> COLORS = Arrays.asList(
            "red_600",
            "pink_600",
            "purple_600",
            "deep_purple_600",
            "indigo_600",
            "blue_600", "light_blue_600",
            "cyan_600",
            "teal_600",
            "green_600",
            "light_green_600",
            "lime_600",
            "yellow_600",
            "amber_600",
            "orange_600",
            "deep_orange_600",
            "brown_600",
            "grey_600",
            "blue_gray_600"
    );

}
