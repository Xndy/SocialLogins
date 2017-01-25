package login.demo.com.sociallogins;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;
import login.demo.com.sociallogins.Util.DownloadImage;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SocialLoginActivity extends AppCompatActivity {

    /*
    Variables para el TWITTER LOGIN
     */
    private TwitterLoginButton twitterloginButton;
    private static String TWITTER_KEY= "3s1BSWf7n1Rms2gpu8Sz4aLCk";
    private static String TWITTER_SECRET = "Tm3U6CXZFY3twfgxFgOvLErw8mB4FQsIc7Tt1JlqOAJTHWt5D7";
    /*
    VARIABLES PARA FACEBOOK LOGIN
    el callback hace la lamada al framwork de fb para usar los datos
     */
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKTwitter();
        SKFacebook();

        setContentView(R.layout.activity_social_login);

        //boton de facebook
        loginButton = (LoginButton)findViewById(R.id.login_button);
        callback = new FacebookCallback<LoginResult>() {
            //si l esion fue exitosa obtiene el token mas el perfil y redirige a la iguiente actividad
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();    }

            //Si el inicio de sesion es cancelado k es casi lo mismo con el de error
            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };

        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);


        //Boton de Twitter
        twitterloginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterloginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });





    }

    private void SDKTwitter(){
        //Inicializa el SK DE TWITWER
        if (!Fabric.isInitialized()) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY,TWITTER_SECRET);
            Fabric.with(SocialLoginActivity.this, new Twitter(authConfig));
        }
    }

    private void SKFacebook(){
        //INICIA EL SDK DE FACEBOOK DEBE SER ANTES DE INFLAR LA VISTA
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //CAMBIA DE ACTIVIDAD JUNTO AL PERFIL DEL USUARIO LOGEADO
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    private void nextActivity(Profile profile){
        if(profile != null){
            Intent main = MainActivity.newIntent
                    (SocialLoginActivity.this, profile.getFirstName(),profile.getLastName(), profile.getProfilePictureUri(200,200).toString());
            startActivity(main);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_social_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            nextActivity(profile);
        }
        @Override
        public void onCancel() {        }
        @Override
        public void onError(FacebookException e) {      }
    };


    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        if(callbackManager != null)
            callbackManager.onActivityResult(requestCode, responseCode, intent);
        twitterloginButton.onActivityResult(requestCode, responseCode, intent);
    }


    public void getTwitterData(final TwitterSession session) {
        MyTwitterApiClient tapiclient = new MyTwitterApiClient(session);
        tapiclient.getCustomService().show(session.getUserId(),
                new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {

                        TwitterAuthToken authToken = session.getAuthToken();
                        String token = authToken.token;
                        String secret = authToken.secret;
                        //name.setText(result.data.name);
                        //location.setText(result.data.location);
                       // new DownloadImage(profileImageView)
                         //      .execute(result.data.profileImageUrl);

                        Intent main = MainActivity.newIntent
                                (SocialLoginActivity.this, result.data.name, result.data.location,
                                        result.data.profileImageUrl);
                        startActivity(main);



//
                       // Log.d("Name", name);
                     //   Log.d("city", location);

                    }

                    public void failure(TwitterException exception) {
                        // Do something on failure
                        exception.printStackTrace();
                    }
                });


    }

    class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(TwitterSession session) {
            super(session);
        }

        public CustomService getCustomService() {
            return getService(CustomService.class);
        }


    }

    interface CustomService {
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") long id, Callback<User> cb);
    }

}
