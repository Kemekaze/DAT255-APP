package dat255.busster.Menu;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import dat255.busster.MainActivity;
import dat255.busster.R;


public class Menu {
    public static Intent getNavigationItem(Context context,MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent =null;

        if (id == R.id.feed) {
            intent = new Intent(context, MainActivity.class);
        }

        return intent;

    }
}
