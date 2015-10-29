package dat255.busster;

import android.content.Context;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;
import android.view.WindowManager;

import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

import dat255.busster.Adapters.FeedAdapter;
import dat255.busster.DB.SurveyDBHandler;
import dat255.busster.Objects.Post;
import dat255.busster.Objects.UserPost;


public class AppTests extends ActivityInstrumentationTestCase2<MainActivity>{



    private final String TAG = "dat255.test.Main";
    private Solo solo;
    public AppTests() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(),getActivity());
    }


    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
    public void testAddPost() throws Exception{
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
        MainActivity main = (MainActivity) solo.getCurrentActivity();
        solo.sleep(1000);
        FeedAdapter adapter =((FeedAdapter)main.getmAdapter());
        int count = adapter.getItemCount();
        //Click on add post Button
        FloatingActionButton fab = (FloatingActionButton) main.findViewById(R.id.addPostActivty);
        fab.callOnClick();
        //Assert that AddPostActivity is opened
        solo.assertCurrentActivity("Expected AddPostActivity", "AddPostActivity");
        //In text field, enter Post test
        solo.sleep(1000);
        solo.enterText(0, "Post test");
        //Send post
        AddPostActivity addPostActivity = (AddPostActivity) solo.getCurrentActivity();
        addPostActivity.findViewById(R.id.send_post_action).callOnClick();
        solo.sleep(1000);
        //solo.clickOnActionBarItem(R.id.send_post_action);
        //Assert that MainActivity is opened after event
        assertEquals(true, solo.waitForActivity("MainActivity"));
        //Assert that the created post is added to the Resyclerlist
        assertNotNull(solo.getText("Post test"));
    }

    public void testVoteUserPost() throws Exception{
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
        MainActivity main = (MainActivity) solo.getCurrentActivity();
        FeedAdapter adapter =((FeedAdapter)main.getmAdapter());

        //Click on menu item so that we can vote
        solo.clickOnMenuItem("Posts", true);
        solo.sleep(1000);
        WindowManager wm = (WindowManager) main.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        UserPost p = (UserPost)adapter.getItem(0);
        int likes[]= p.getVotes();
        //like
        solo.drag(size.x / 8, size.x / 2, size.y / 4, size.y / 4, 1);
        assertEquals(likes[0] - likes[0] + 1, p.getVotes()[0] - p.getVotes()[1]);
        solo.sleep(1500);
        //dislike
        solo.drag(size.x-size.x/8,size.x/2,size.y/4,size.y/4,1);
        assertEquals(likes[0] - likes[0] - 1, p.getVotes()[0] - p.getVotes()[1]);
    }
    public void testGetMorePosts() throws Exception{
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
        MainActivity main = (MainActivity) solo.getCurrentActivity();
        final FeedAdapter adapter =((FeedAdapter)main.getmAdapter());
        RecyclerView recyclerView = main.getmRecyclerView();
        //get current item count
        final int count = adapter.getItemCount();
        //scroll to bottom and check if size has  increased

        assertEquals(true, solo.waitForCondition(new Condition() {
            @Override
            public boolean isSatisfied() {

                while (adapter.getItemCount() == count) {
                    solo.scrollRecyclerViewToBottom(0);
                }
                return true;
            }
        }, 5000));
    }
    public void testRefreshPosts() throws Exception{
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
        final MainActivity main = (MainActivity) solo.getCurrentActivity();
        FeedAdapter adapter =((FeedAdapter)main.getmAdapter());
        main.getRefreshListener().onRefresh();

    }

    public void testSelectFilter() throws Exception{
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");

        //Click on menu item
        solo.clickOnMenuItem("Posts", true);
        //Assert that clicked menu item is clicked
        MainActivity main = (MainActivity) solo.getCurrentActivity();
        FeedAdapter adapter =((FeedAdapter)main.getmAdapter());
        solo.sleep(1000);
        for(Post p: adapter.getPosts()){
            assertEquals("userpost",p.getType());
        }
    }

    public void testShowComments() throws Exception {
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");

        MainActivity main = (MainActivity) solo.getCurrentActivity();
        FeedAdapter adapter = ((FeedAdapter) main.getmAdapter());
        //get first userpost
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).getType().equals("userpost")) {
                solo.clickLongInRecycleView(i);

                break;
            }
        }
        //Assert that ViewCommentsActivity is opened
        solo.assertCurrentActivity("Expected ViewCommentsActivity", "ViewCommentsActivity");
        solo.goBack();
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
    }
    public void testAddComment() throws Exception{
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");

        MainActivity main = (MainActivity) solo.getCurrentActivity();
        FeedAdapter adapter =((FeedAdapter)main.getmAdapter());
        //get first userpost
        for(int i=0;i<adapter.getItemCount();i++){
            if(adapter.getItem(i).getType().equals("userpost")){
                solo.clickLongInRecycleView(i);

                break;
            }
        }

        //Assert that ViewCommentsActivity is opened
        solo.assertCurrentActivity("Expected ViewCommentsActivity", "ViewCommentsActivity");
        solo.sleep(1000);
        ViewCommentsActivity comments = (ViewCommentsActivity) solo.getCurrentActivity();
        //Click on add Comment Button
        FloatingActionButton fab = (FloatingActionButton) comments.findViewById(R.id.addCommentActivity);
        fab.callOnClick();
        //Assert that AddCommentActivity is opened
        solo.assertCurrentActivity("Expected AddCommentActivity", "AddCommentActivity");
        solo.sleep(1000);
        //In text field, enter Post test
        solo.enterText(0, "Comment test");
        //Send Comment
        AddCommentActivity addCommentActivity = (AddCommentActivity) solo.getCurrentActivity();
        addCommentActivity.findViewById(R.id.send_comment_action).callOnClick();
        //Assert that ViewCommentsActivity is opened after event
        assertEquals(true, solo.waitForActivity("ViewCommentsActivity"));
        solo.sleep(1000);
        //check that the new comment is in the list
        assertNotNull(solo.getText("Comment test"));
        //go back to main menu
        solo.goBack();
        solo.sleep(1000);
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");

    }

    public void testVoteSurvey() throws Exception{
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
        MainActivity main = (MainActivity) solo.getCurrentActivity();
        FeedAdapter adapter =((FeedAdapter)main.getmAdapter());

        //Click on menu item so that we can vote
        solo.clickOnMenuItem("Surveys", true);
        solo.sleep(1000);

        solo.clickInRecyclerView(0);
        //Assert that SurveyActivity is opened
        solo.assertCurrentActivity("Expected SurveyActivity", "SurveyActivity");
        solo.sleep(1000);
        SurveyDBHandler surveyDBHandler = new SurveyDBHandler(
                solo.getCurrentActivity().getApplicationContext(),null
        );
        int sizeBefore = surveyDBHandler.getVotes().size();
        solo.clickOnRadioButton(0);
        solo.sleep(1000);
        assertEquals(sizeBefore + 1, surveyDBHandler.getVotes().size());
        assertEquals(true,solo.waitForDialogToOpen());
    }
    public void testShowSurveyReulst() throws Exception{
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
        MainActivity main = (MainActivity) solo.getCurrentActivity();
        FeedAdapter adapter =((FeedAdapter)main.getmAdapter());

        //Click on menu item so that we can vote
        solo.clickOnMenuItem("Surveys", true);
        solo.sleep(1000);

        solo.clickInRecyclerView(0);
        //Assert that a dialog is opened
        assertEquals(true,solo.waitForDialogToOpen());
        solo.sleep(1000);
    }



}
