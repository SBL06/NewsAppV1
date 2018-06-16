package androidbasicsnanodegree.sbl.newsappv1;

import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference category = findPreference(getString(R.string.category_key)) ;



        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringvalue = newValue.toString() ;
            preference.setSummary(stringvalue);
            return false;
        }
    }
}
