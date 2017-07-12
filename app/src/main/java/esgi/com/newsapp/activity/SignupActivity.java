package esgi.com.newsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import esgi.com.newsapp.R;
import esgi.com.newsapp.model.User;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.et_subscribe_email)
    EditText etEmail;

    @BindView(R.id.et_subscribe_password)
    EditText etPassword;

    @BindView(R.id.et_subscribe_lastname)
    EditText etLastname;

    @BindView(R.id.et_subscribe_firstname)
    EditText etFirstname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    private boolean verifyFields() {
        if (etLastname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill the lastname", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etFirstname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill the firstname", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill the password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill the email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_subscribe)
    public void subscribe() {
        if (!verifyFields())
            return;
        User userToCreate = getUserFromForm();
        RetrofitSession.getInstance().getUserService().createAccount(userToCreate, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                Toast.makeText(SignupActivity.this, "Compte créé", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void error(int code, String message) {
                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private User getUserFromForm() {
        User newUser = new User();
        newUser.setEmail(etEmail.getText().toString());
        newUser.setFirstname(etFirstname.getText().toString());
        newUser.setLastname(etLastname.getText().toString());
        newUser.setPassword(etPassword.getText().toString());
        return newUser;
    }
}
