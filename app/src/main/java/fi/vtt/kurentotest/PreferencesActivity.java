package fi.vtt.kurentotest;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.text.InputType;

import fi.vtt.kurentotest.util.Constants;

public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        EditTextPreference preference = (EditTextPreference) findPreference(Constants.SERVER_NAME);
        preference.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
    }
}
