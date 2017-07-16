package esgi.com.newsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import esgi.com.newsapp.R;
import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;
import esgi.com.newsapp.utils.DateConverter;

/**
 * Created by junior on 13/07/2017.
 */

public class CreatePostForTopicFragment extends RootFragment {
    private final int OFFLINE = -22;

    @BindView(R.id.et_content_post)
    public EditText etContent;

    @BindView(R.id.et_title_post)
    public EditText etTitle;

    public String topic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_create_post, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        topic = bundle.getString(getString(R.string.bundle_topic_id));

        return view;
    }

    @Override
    public String getTitle() {
        return getString(R.string.post_creation);
    }

    private boolean verifyFields() {
        if (etTitle.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.fill_title, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etContent.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.fill_content, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private Post getPostFromForm(){
        Post post = new Post();
        post.setTitle(etTitle.getText().toString());
        post.setContent(etContent.getText().toString());
        post.setTopic(topic);
        post.setDate(DateConverter.getCurrentFormattedDate());
        Log.d("DATE", DateFormat.getDateTimeInstance().format(new Date()));

        return post;
    }

    @OnClick(R.id.btn_create_post)
    public void createPost() {
        if (!verifyFields())
            return;
        Post postCreate = getPostFromForm();
        RetrofitSession.getInstance().getPostService().createPost(postCreate, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                Toast.makeText(getContext(), R.string.post_created, Toast.LENGTH_SHORT).show();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStackImmediate();
            }

            @Override
            public void error(int code, String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                if (code == OFFLINE) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStackImmediate();
                }
                Log.d("FAIL", message);
            }
        });
    }

}
