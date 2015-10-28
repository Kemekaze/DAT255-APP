package dat255.busster;

import android.support.design.widget.FloatingActionButton;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import dat255.busster.Adapters.FeedAdapter;


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

    public void testShowUserPostAndAddComment() throws Exception{
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


}
