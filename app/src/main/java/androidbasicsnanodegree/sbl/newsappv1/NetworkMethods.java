package androidbasicsnanodegree.sbl.newsappv1;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


// All the following methods are inspired from the Udacity course : Android Basics : Networking
public class NetworkMethods {

    static final String LOG_TAG = Context_getter.getContext().getString(R.string.log_tag_network_methods);
    static final int READ_TIMEOUT = 10000 ;
    static final int CONNECTION_TIMEOUT = 15000 ;
    static final int RESPONSE_CODE = 200 ;


    // Following method is creating an URL from a String
    public static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, Context_getter.getContext().getString(R.string.problem_building_url), e);
        }
        return url;
    }

    //Following method is making the internet request and retrieving the JSON response.
    public static String makeRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod(Context_getter.getContext().getString(R.string.get));
            urlConnection.connect();

            if (urlConnection.getResponseCode() == RESPONSE_CODE ) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, Context_getter.getContext().getString(R.string.error_response_code) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, Context_getter.getContext().getString(R.string.problem_JSON), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(Context_getter.getContext().getString(R.string.utf_8)));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //Following method is creating a list of news from the JSON object retrieved
    public static List<News> extractFeature(String newsJSON) {

        List<News> newsList = new ArrayList<>();

        if (TextUtils.isEmpty(newsJSON)) {
            News news = new News("", "", "", Context_getter.getContext().getString(R.string.problem_retrieving_data), "", "", "");
            newsList.add(news);
            return newsList ;
        }

        if (newsJSON == null) {
            News news = new News("", "", "", Context_getter.getContext().getString(R.string.no_news), "", "", "");
            newsList.add(news);
            return newsList ;
        }

        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject results = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = results.getJSONArray("results");

            // Retrieving data for each news or displaying a specific text if there are no news

            if (newsArray.length()==0) {
                News news = new News("", "", "", Context_getter.getContext().getString(R.string.no_news), "", "", "");
                newsList.add(news);

            } else {

            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentNews = newsArray.getJSONObject(i);

                String title = currentNews.getString("webTitle");

                String section = currentNews.getString("sectionName");

                String url = currentNews.getString("webUrl");

                String date = currentNews.getString("webPublicationDate");

                JSONObject fields = currentNews.getJSONObject("fields");

                String description = fields.getString("trailText");

                String imgurl = fields.getString("thumbnail");

                String author = fields.getString("byline");

                // Creating a list of news
                News news = new News(title, section, url, description, imgurl, author, date);
                newsList.add(news); }
            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, Context_getter.getContext().getString(R.string.problem_parsing_json), e);
        }

        // Return the list of news
        return newsList;
    }


}
