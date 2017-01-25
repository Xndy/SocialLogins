package login.demo.com.sociallogins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import login.demo.com.sociallogins.Util.CustomVolleyRequest;

public class GmailActivity extends AppCompatActivity {

    private final static String name ="login.demo.com.sociallogins.name",
            surname="login.demo.com.sociallogins.surname",
            imageUrl="login.demo.com.sociallogins.imageUrl";

    private String nombre, apellido, imagen;

    private TextView textViewName;
    private TextView textViewEmail;
    private NetworkImageView profilePhoto;

    //Image Loader
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apellido = getIntent().getStringExtra(surname);
        nombre = getIntent().getStringExtra(name);
        imagen = getIntent().getStringExtra(imageUrl);



        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        profilePhoto = (NetworkImageView) findViewById(R.id.profileImage);

        textViewName.setText(nombre);
        textViewEmail.setText(apellido);

        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                .getImageLoader();

        imageLoader.get(imagen,
                ImageLoader.getImageListener(profilePhoto,
                        R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher));

        profilePhoto.setImageUrl(imagen, imageLoader);

    }


    public static Intent newIntent(Context context, String nombre, String apellido, String imagen){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(name, nombre);
        intent.putExtra(surname, apellido);
        intent.putExtra(imageUrl, imagen);
        return intent;
    }



}
