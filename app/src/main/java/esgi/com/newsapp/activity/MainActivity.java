package esgi.com.newsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import esgi.com.newsapp.R;
import esgi.com.newsapp.model.User;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final User user = new User("test127@digipolitan.com", "testcréationtest", "UnMarabout", "BarbuBreton");
        System.out.println("Launching");

        RetrofitSession.getInstance().getUserService().createAccount(user, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                login(user);
            }

            @Override
            public void error(int code, String message) {
                System.out.println(message);
            }
        });


    }

    private void login(User user) {
        RetrofitSession.getInstance().getUserService().login(user, new ApiResult<String>() {
            @Override
            public void success(String res) {
                System.out.println(res);
            }

            @Override
            public void error(int code, String message) {
                System.out.println(message);
            }
        });
    }
}
