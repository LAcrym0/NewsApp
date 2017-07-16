package esgi.com.newsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import esgi.com.newsapp.R;
import esgi.com.newsapp.model.User;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;

/**
 * Created by Grunt on 12/07/2017.
 */

public class EditUserFragment extends RootFragment {
    @BindView(R.id.et_subscribe_email)
    EditText etEmail;

    @BindView(R.id.et_subscribe_password)
    EditText etPassword;

    @BindView(R.id.et_subscribe_lastname)
    EditText etLastname;

    @BindView(R.id.et_subscribe_firstname)
    EditText etFirstname;

    @Override
    public String getTitle() {
        return getString(R.string.nav_my_account);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        ButterKnife.bind(this, view);

        getMyInformation();

        FloatingActionButton fab = getMainActivity().getFab();
        fab.setVisibility(View.GONE);

        return view;
    }

    private void getMyInformation() {
        RetrofitSession.getInstance().getUserService().getMyInformation(new ApiResult<User>() {
            @Override
            public void success(User user) {
                etEmail.setText(user.getEmail());
                etFirstname.setText(user.getFirstname());
                etLastname.setText(user.getLastname());
            }

            @Override
            public void error(int code, String message) {
                Toast.makeText(getContext(), getString(R.string.error_getting_information), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_edit)
    public void editUser() {
        User userToEdit = getUserFromForm();
        RetrofitSession.getInstance().getUserService().editUser(userToEdit, new ApiResult<User>() {
            @Override
            public void success(User res) {
                Toast.makeText(getContext(), "Compte modifié", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int code, String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private User getUserFromForm() {
        User newUser = new User();
        newUser.setFirstname(etFirstname.getText().toString());
        newUser.setLastname(etLastname.getText().toString());
        newUser.setPassword(etPassword.getText().toString());
        newUser.setEmail(etEmail.getText().toString());
        return newUser;
    }

    public static EditUserFragment newInstance() {
        Bundle args = new Bundle();
        EditUserFragment fragment = new EditUserFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
