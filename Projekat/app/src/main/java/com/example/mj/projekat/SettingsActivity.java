package com.example.mj.projekat;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        ListPreference listPreference = (ListPreference)findPreference("sortPreferences");
        String sort = listPreference.getValue();
        SharedPreferences sharedPref2 = getSharedPreferences("sort",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref2.edit();
        editor.putString("sort",sort);
        editor.apply();

        ListPreference listPreference2 = (ListPreference)findPreference("sortCommentPreferences");
        String sort2 = listPreference2.getValue();
        SharedPreferences sharedPref3 = getSharedPreferences("sortComm",MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPref3.edit();
        editor2.putString("sortComm",sort2);
        editor2.apply();

        final DatePreferences datePreferences = (DatePreferences)findPreference("datePicker");
        SharedPreferences sharedPref4 = getSharedPreferences("datum",MODE_PRIVATE);
        SharedPreferences.Editor editor4 = sharedPref4.edit();
        String datum = datePreferences.toString();
        editor4.putString("datum",datum);
        editor4.apply();

        datePreferences.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference,Object newValue) {
                SharedPreferences sharedPref4 = getSharedPreferences("datum",MODE_PRIVATE);
                SharedPreferences.Editor editor4 = sharedPref4.edit();
                String datum = datePreferences.toString();
                editor4.putString("datum",datum);
                editor4.apply();
                return true;
            }
        });



    }

    public void convertDate(String datum)
    {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListPreference listPreference = (ListPreference)findPreference("sortPreferences");
        String sort = listPreference.getValue();
        SharedPreferences sharedPref2 = getSharedPreferences("sort",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref2.edit();
        editor.putString("sort",sort);

        ListPreference listPreference2 = (ListPreference)findPreference("sortCommentPreferences");
        String sort2 = listPreference2.getValue();
        SharedPreferences sharedPref3 = getSharedPreferences("sortComm",MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPref3.edit();
        editor2.putString("sortComm",sort2);
        editor2.apply();

        DatePreferences datePreferences = (DatePreferences)findPreference("datePicker");
        SharedPreferences sharedPref4 = getSharedPreferences("datum",MODE_PRIVATE);
        SharedPreferences.Editor editor4 = sharedPref4.edit();
        String datum = datePreferences.toString();
        editor4.putString("datum",datum);
        editor4.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ListPreference listPreference = (ListPreference)findPreference("sortPreferences");
        String sort = listPreference.getValue();
        SharedPreferences sharedPref2 = getSharedPreferences("sort",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref2.edit();
        editor.putString("sort",sort);


        ListPreference listPreference2 = (ListPreference)findPreference("sortCommentPreferences");
        String sort2 = listPreference2.getValue();
        SharedPreferences sharedPref3 = getSharedPreferences("sortComm",MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPref3.edit();
        editor2.putString("sortComm",sort2);
        editor2.apply();

        DatePreferences datePreferences2 = (DatePreferences)findPreference("datePicker");
        SharedPreferences sharedPref4 = getSharedPreferences("datum",MODE_PRIVATE);
        SharedPreferences.Editor editor4 = sharedPref4.edit();
        String datum = datePreferences2.toString();
        editor4.putString("datum",datum);
        editor4.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
