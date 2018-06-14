package androidbasicsnanodegree.sbl.newsappv1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
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

public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    public OnListFragmentInteractionListener mListener;
    public MyNewsRecyclerViewAdapter adapter;
    private final static int NEWS_LOADER_ID = 1;


    public NewsFragment() {
    }

    public static NewsFragment newInstance(int columnCount) {

        NewsFragment fragment = new NewsFragment();
        Bundle args;
        args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
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
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            adapter.mValues.clear();
            adapter.mValues.add(new News("", "", "", "No internet connection !", "", "", " T"));
            adapter.notifyDataSetChanged();
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(Objects.requireNonNull(getContext()));
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
                    + " must implement OnListFragmentInteractionListener");
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
