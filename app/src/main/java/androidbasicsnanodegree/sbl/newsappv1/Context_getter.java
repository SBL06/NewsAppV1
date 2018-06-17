package androidbasicsnanodegree.sbl.newsappv1;

import android.app.Application;
import android.content.Context;

public class Context_getter extends Application {

    private static Context Context;


    @Override
    public void onCreate() {
        super.onCreate();
        Context = this;
    }

    public static Context getContext(){
        return Context;
    }
}
