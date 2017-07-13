package esgi.com.newsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import esgi.com.newsapp.R;
import esgi.com.newsapp.model.Auth;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_edit_email)
    EditText email;

    @BindView(R.id.login_edit_password)
    EditText password;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //todo remove that, only for tests
        email.setText("testeur@gmail.com");
        password.setText("password");


        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();





    }

    @OnClick(R.id.login_button_login)
    public void login() {
        Auth auth = new Auth(email.getText().toString(), password.getText().toString());
        RetrofitSession.getInstance().getUserService().login(auth, new ApiResult<String>() {
            @Override
            public void success(String res) {
                //login(auth);
                Log.d("LOGINACTIVITY", "logged");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void error(int code, String message) {
                System.out.println(message);
            }
        });

    }

    @OnClick(R.id.login_button_signup)
    public void signup() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }
}