package androidbasicsnanodegree.sbl.newsappv1;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    // The url used to retrieve datas
    private String url_request ;

    public NewsLoader(Context context, String url) {
        super(context);
        url_request = url ;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    // Following method is used to retrieve data in the background thread
    public List<News> loadInBackground() {
        URL url = NetworkMethods.createUrl(url_request);
        String response;

        try {
            response = NetworkMethods.makeRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
            response = null;
        }


        List<News> updatedList = NetworkMethods.extractFeature(response);


        return updatedList;
    }
}
