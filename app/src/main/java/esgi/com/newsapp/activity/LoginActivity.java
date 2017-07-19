package esgi.com.newsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import esgi.com.newsapp.R;
import esgi.com.newsapp.model.Auth;
import esgi.com.newsapp.model.User;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;
import esgi.com.newsapp.utils.PreferencesHelper;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_edit_email)
    EditText email;

    @BindView(R.id.login_edit_password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.login_button_login)
    public void login() {
        Auth auth = new Auth(email.getText().toString(), password.getText().toString());
        RetrofitSession.getInstance().getUserService().login(auth, new ApiResult<String>() {
            @Override
            public void success(String res) {
                //necessary to get the user id...
                getMe();
            }

            @Override
            public void error(int code, String message) {
                System.out.println(message);
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getMe() {
        RetrofitSession.getInstance().getUserService().getMyInformation(new ApiResult<User>() {
            @Override
            public void success(User user) {
                PreferencesHelper.getInstance().setUserId(user.getId());
                Log.d("LOGINACTIVITY", "logged");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void error(int code, String message) {
                Toast.makeText(LoginActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.login_button_signup)
    public void signup() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }
}