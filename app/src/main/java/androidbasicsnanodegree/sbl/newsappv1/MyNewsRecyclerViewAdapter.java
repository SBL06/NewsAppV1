package androidbasicsnanodegree.sbl.newsappv1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidbasicsnanodegree.sbl.newsappv1.NewsFragment.OnListFragmentInteractionListener;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MyNewsRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsRecyclerViewAdapter.ViewHolder> {

    public List<News> mValues;
    private OnListFragmentInteractionListener mListener;

    // When the adapter is created, it initialize an empty list to be used while the real list is
    // updated from the network

    public MyNewsRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mValues.add(new News(" ", " ", " ", Context_getter.getContext().getString(R.string.loading_news), " ", " ", " T"));
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_news, parent, false);
        return new ViewHolder(view);
    }

    // The following method updates the UI. It is also downloading images in order to display them.
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (this.getItemCount() != 0) {
            News currentNews = mValues.get(position);
            holder.mItem = mValues.get(position);
            holder.title.setText(currentNews.getTitle());
            holder.author.setText(Context_getter.getContext().getString(R.string.by) + currentNews.getAuthor());
            holder.section.setText(Context_getter.getContext().getString(R.string.category_display) + currentNews.getSection());
            holder.pubdate.setText(Context_getter.getContext().getString(R.string.published_display) + currentNews.getDate());
            holder.description.setText(currentNews.getDescription());
            holder.image.setImageResource(R.drawable.ic_cloud_download_black_24dp);

            new DownloadImageTask(holder.image).execute(currentNews.getImgUrl());

        } else {
            Log.d(Context_getter.getContext().getString(R.string.ui_update_error), Context_getter.getContext().getString(R.string.empty_list));
        }

        // Following method is made to pass data to the main activity when an item is clicked.
        holder.View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        } else {
            return 0;
        }
    }

    // Views are selected in the UI
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View View;
        public TextView title;
        public TextView author;
        public TextView pubdate;
        public TextView section;
        public TextView description;
        public News mItem;
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            View = view;
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            pubdate = view.findViewById(R.id.pubDate);
            section = view.findViewById(R.id.section);
            description = view.findViewById(R.id.description);
            image = view.findViewById(R.id.imgview);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + author.getText() + "'";
        }
    }


    // Following method is used to download images and display them on the UI.
    // Inspired from GitHub : https://github.com/tschellenbach/android/blob/master/src/com/example/test/DownloadImageTask.java
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            URL urldisplay ;

            Bitmap mIcon11 = null;

            try {
               urldisplay = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                urldisplay=null ;
            }
            
            try {
                InputStream in = urldisplay.openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(Context_getter.getContext().getString(R.string.error), e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
