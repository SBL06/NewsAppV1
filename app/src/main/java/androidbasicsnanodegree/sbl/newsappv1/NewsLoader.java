package androidbasicsnanodegree.sbl.newsappv1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    // The url used to retrieve datas
    private static final String REQUEST_URL = "https://content.guardianapis.com/search?from-date=2017-01-01&use-date=published&show-fields=headline%2Cbyline%2CfirstPublicationDate%2CtrailText%2Cthumbnail&q=opera&api-key=dd26bab4-1e9a-4017-a632-4a5d2fc690d4";


    public NewsLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    // Following method is used to retrieve data in the background thread
    public List<News> loadInBackground() {
        URL url = NetworkMethods.createUrl(REQUEST_URL);
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
