package dat255.busster;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

import dat255.busster.Adapters.FeedAdapter;
import dat255.busster.Objects.Post;


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

        FeedAdapter adapter =((FeedAdapter)main.getmAdapter());
        int count = adapter.getItemCount();
        //Click on add post Button
        FloatingActionButton fab = (FloatingActionButton) main.findViewById(R.id.addPostActivty);
        fab.callOnClick();
        //Assert that AddPostActivity is opened
        solo.assertCurrentActivity("Expected AddPostActivity", "AddPostActivity");
        //In text field, enter Post test
        solo.enterText(0, "Post test");
        //Send post
        solo.clickOnImage(0);
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

        assertEquals(true,solo.waitForCondition(new Condition() {
            @Override
            public boolean isSatisfied() {

                while(adapter.getItemCount()== count){
                    solo.scrollRecyclerViewToBottom(0);
                }
                return true;
            }
        },5000));
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


}
