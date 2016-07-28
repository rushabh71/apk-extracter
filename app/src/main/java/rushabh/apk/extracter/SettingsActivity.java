package rushabh.apk.extracter;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class SettingsActivity extends AppCompatPreferenceActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String cp = sharedPref.getString("pref_path", "/sdcard/Apk-Extract/");
        EditTextPreference path = (EditTextPreference)findPreference("pref_path");
        path.setSummary(cp);
        path.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());

                return true;
            }
        });

        Preference email = (Preference) findPreference("pref_email");
        email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:rushabhp99@gmail.com")));
                return false;
            }
        });


    }
}