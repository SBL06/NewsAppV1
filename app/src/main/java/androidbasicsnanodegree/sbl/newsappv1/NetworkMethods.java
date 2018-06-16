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

    static final String LOG_TAG = "Network Method";


    // Following method is creating an URL from a String
    public static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
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
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
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
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
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

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject results = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = results.getJSONArray("results");

            // Retrieving data for each news or displaying a specific text if there are no news

            if (newsArray.length()==0) {
                News news = new News("", "", "", "No news to retrieve ! Please come back later!", "", "", "");
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

            Log.e("Network Methods", "Problem parsing the JSON results", e);
            News news = new News("", "", "", "No news to retrieve ! Please come back later!", "", "", "");
            newsList.add(news);
        }

        // Return the list of news
        return newsList;
    }


}
