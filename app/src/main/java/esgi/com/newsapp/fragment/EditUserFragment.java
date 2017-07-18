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
import esgi.com.newsapp.model.EditUser;
import esgi.com.newsapp.model.User;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;

/**
 * Created by Grunt on 12/07/2017.
 */

public class EditUserFragment extends RootFragment {
    @BindView(R.id.et_subscribe_email)
    EditText etEmail;

    @BindView(R.id.et_subscribe_lastname)
    EditText etLastname;

    @BindView(R.id.et_subscribe_firstname)
    EditText etFirstname;

    private User oldUser;

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
                oldUser = user;
            }

            @Override
            public void error(int code, String message) {
                Toast.makeText(getContext(), getString(R.string.error_getting_information), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_edit)
    public void editUser() {
        EditUser userToEdit = getEditUserFromForm();
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

    private EditUser getEditUserFromForm() {
        EditUser userToEdit = new EditUser();
        userToEdit.setFirstname(etFirstname.getText().toString().length() == 0 ? oldUser.getFirstname() : etFirstname.getText().toString());
        userToEdit.setLastname(etLastname.getText().toString().length() == 0 ? oldUser.getLastname() : etLastname.getText().toString());
        return userToEdit;
    }

    public static EditUserFragment newInstance() {
        Bundle args = new Bundle();
        EditUserFragment fragment = new EditUserFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
