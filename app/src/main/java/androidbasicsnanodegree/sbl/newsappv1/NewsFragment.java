package androidbasicsnanodegree.sbl.newsappv1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

// This fragment was created using the Android Studio template
//Some methods are inspired from the Udacity course: Android Basics Networking

public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String REQUEST_URL = "https://content.guardianapis.com/search?" ;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    public OnListFragmentInteractionListener mListener;
    public MyNewsRecyclerViewAdapter adapter;
    private final static int NEWS_LOADER_ID = 1;


    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    // This method is called whenever the fragment is created : that includes when the app is refreshed
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.destroyLoader(NEWS_LOADER_ID);

        adapter = new MyNewsRecyclerViewAdapter(mListener);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(adapter);
        }

        ConnectivityManager connMgr = (ConnectivityManager)
                Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.


            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            adapter.mValues.clear();
            adapter.mValues.add(new News("", "", "", getString(R.string.no_internet_connection), "", "", " T"));
            adapter.notifyDataSetChanged();
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String category = sharedPrefs.getString(getString(R.string.category_key), getString(R.string.settings_category_default) );
        String order = sharedPrefs.getString(getString(R.string.order_key), getString(R.string.order_default_value)) ;

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value
        uriBuilder.appendQueryParameter("from-date", "2018-04-01");
        uriBuilder.appendQueryParameter("use-date", "published");
        uriBuilder.appendQueryParameter("show-fields", "headline,byline,firstPublicationDate,trailText,thumbnail");
        uriBuilder.appendQueryParameter("q", category);
        uriBuilder.appendQueryParameter("order-by", order) ;
        uriBuilder.appendQueryParameter("api-key", "dd26bab4-1e9a-4017-a632-4a5d2fc690d4" ) ;

        return new NewsLoader(getContext(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {

        View loadingIndicator = getActivity().findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);

        adapter.mValues = data;

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getString(R.string.implement_onlistfragmentinteractionlistener));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // This interface is used to pass data to the main activity when a news is clicked
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(News item);
    }

}
