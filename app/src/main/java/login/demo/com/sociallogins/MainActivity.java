package login.demo.com.sociallogins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import login.demo.com.sociallogins.Util.DownloadImage;

public class MainActivity extends AppCompatActivity {

    private ShareDialog shareDialog;
    private TextView nameView;
    private final static String name ="login.demo.com.sociallogins.name",
                                surname="login.demo.com.sociallogins.surname",
                                imageUrl="login.demo.com.sociallogins.imageUrl";


    private String nombre, apellido, imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder().build();
                shareDialog.show(content);
            }
        });

        nombre = getIntent().getStringExtra(name);
        apellido = getIntent().getStringExtra(surname);
        imagen = getIntent().getStringExtra(imageUrl);

        nameView = (TextView)findViewById(R.id.nameAndSurname);
        nameView.setText("" + nombre + " " + apellido);

        new DownloadImage((ImageView) findViewById(R.id.profileImage)).execute(imagen);

        shareDialog = new ShareDialog(this);

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent login = new Intent(MainActivity.this, SocialLoginActivity.class);
                startActivity(login);
                finish();
            }
        });


    }



    public static Intent newIntent(Context context, String nombre, String apellido, String imagen){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(name, nombre);
        intent.putExtra(surname, apellido);
        intent.putExtra(imageUrl, imagen);
        return intent;
    }



}
