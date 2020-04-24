package com.example.preferencedemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyPreferenceFragment myPreferenceFragment = new MyPreferenceFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, myPreferenceFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {

        private Preference changePinPreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.simple_preference);
            changePinPreference = findPreference("change_pin");
            Intent jumpIntent = new Intent(getContext(), SecondActivity.class);
            changePinPreference.setIntent(jumpIntent);
        }
    }
}
