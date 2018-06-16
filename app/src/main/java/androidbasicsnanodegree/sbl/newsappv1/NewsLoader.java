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
    private static final String REQUEST_URL = "https://content.guardianapis.com/search?from-date=2018-04-01&use-date=published&show-fields=headline%2Cbyline%2CfirstPublicationDate%2CtrailText%2Cthumbnail&q=opera&api-key=dd26bab4-1e9a-4017-a632-4a5d2fc690d4";
    private String url_request ;

    public NewsLoader(Context context, String url) {
        super(context);
        url_request = url ;
        Log.d("URL CREATED", url_request.toString()) ;
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

        Log.d("REPONSE", response.toString());

        List<News> updatedList = NetworkMethods.extractFeature(response);

        Log.d("LISTE", updatedList.toString());

        return updatedList;
    }
}
