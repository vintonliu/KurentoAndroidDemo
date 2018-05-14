package fi.vtt.kurentotest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fi.vtt.kurentotest.util.Constants;

public class LoginActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 101;
    private static final int MY_PERMISSIONS_REQUEST = 102;
    private static final String TAG = "LoginActivity";

    private EditText mEdtUserName;
    private EditText mEdtRoomName;

    private SharedPreferences mSharedPreferences;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mContext = this;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        mEdtUserName = (EditText) findViewById(R.id.username);
        mEdtRoomName = (EditText) findViewById(R.id.roomname);

        askForPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(mContext, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void askForPermissions() {
        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST);
        } else if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    private boolean arePermissionGranted() {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_DENIED);
    }

    /**
     * Takes the username from the EditText, check its validity and saves it if valid.
     *   Then, redirects to the MainActivity.
     * @param view Button clicked to trigger call to joinChat
     */
    public void joinRoom(View view) {
        String username = mEdtUserName.getText().toString();
        String roomname = mEdtRoomName.getText().toString();
        if (!validUsername(username) || ! validRoomname(roomname)) {
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.USER_NAME, username);
        editor.putString(Constants.ROOM_NAME, roomname);
        editor.commit();

        if (arePermissionGranted()) {
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
        } else {
            showToast("Permission denied.");
        }

    }

    /**
     * Optional function to specify what a username in your chat app can look like.
     * @param username The name entered by a user.
     * @return is username valid
     */
    private boolean validUsername(String username) {
        if (username.length() == 0) {
            mEdtUserName.setError("Username cannot be empty.");
            return false;
        }
        if (username.length() > 16) {
            mEdtUserName.setError("Username too long.");
            return false;
        }
        return true;
    }

    /**
     * Optional function to specify what a username in your chat app can look like.
     * @param roomname The name entered by a user.
     * @return is username valid
     */
    private boolean validRoomname(String roomname) {
        if (roomname.length() == 0) {
            mEdtRoomName.setError("Rooomname can not too long");
            return false;
        }
        if (roomname.length() > 16) {
            mEdtRoomName.setError("Roomname too long.");
            return false;
        }
        return true;
    }

    public void showToast(String string) {
        try {
            CharSequence text = string;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }
        catch (Exception e){e.printStackTrace();}
    }
}
