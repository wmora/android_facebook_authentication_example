package com.wmora.demos.facebookauthentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class MainActivity extends Activity {

    private final String TAG = getClass().getSimpleName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Start Facebook login
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            /*
                The call method of the callback will likely fire multiple times (when the session is OPENING and then
                OPENED, for example). We want to use that final successful state to fetch the user's name and update
                the welcome message.
             */
            @Override
            public void call(Session session, SessionState state, Exception exception) {

                if (session.isOpened()) {
                    //make request to the /me API
                    Request.newMeRequest(session, new Request.GraphUserCallback() {

                        //Callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            Log.i(TAG, "Me request completed");
                            if(user != null) {
                                TextView mTextView = (TextView) findViewById(R.id.welcome);
                                mTextView.setText(String.format(getString(R.string.welcome_message),
                                        user.getFirstName()));
                            }
                        }

                    }).executeAsync();
                    Log.i(TAG, "Me request started. Waiting response");
                }

            }
        });
    }

    /*
        Since the login flow for your app will require the users to transition out of, and back into, this Activity,
        we need a small amount of wiring to update the active session
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

}
