package androidbasicsnanodegree.sbl.newsappv1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NewsFragment.OnListFragmentInteractionListener {

    //For this app, the onCreate() methods will only launch the NewsFragment so we don't need to modify it
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //This method is used to create the refresh button on the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Following method is to refresh the page when the refresh button is clicked. We simply recreate the main activity
    // that will retrieve new data from the internet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            this.recreate();
        }
        return super.onOptionsItemSelected(item);
    }


    // The following method is made in order to open the internet browser when a news is clicked by the user
    @Override
    public void onListFragmentInteraction(News item) {
        News currentNews = item;

        if (currentNews.getNewsUrl()!=""){

        Uri newsUri = Uri.parse(currentNews.getNewsUrl());

        Intent i = new Intent(Intent.ACTION_VIEW, newsUri);

        startActivity(i); }
        else {
            Toast.makeText(this, "News cannot be opened", Toast.LENGTH_SHORT).show();
        }
    }
}
