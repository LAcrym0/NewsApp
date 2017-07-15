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
import esgi.com.newsapp.database.RealmManager;
import esgi.com.newsapp.model.Comment;
import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;
import esgi.com.newsapp.utils.DateConverter;

/**
 * Created by junior on 13/07/2017.
 */

public class CreateCommentForNewsFragment extends RootFragment {

    @BindView(R.id.et_content_comment)
    public EditText etContent;

    @BindView(R.id.et_title_comment)
    public EditText etTitle;

    public String news;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_create_comment, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        news = bundle.getString(getString(R.string.bundle_news_id));

        return view;
    }

    @Override
    public String getTitle() {
        return getString(R.string.comment_creation);
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


    private Comment getPostFromForm(){
        Comment comment = new Comment();
        comment.setTitle(etTitle.getText().toString());
        comment.setContent(etContent.getText().toString());
        comment.setNews(news);
        comment.setDate(DateConverter.getCurrentFormattedDate());
        Log.d("DATE", DateFormat.getDateTimeInstance().format(new Date()));

        return comment;
    }

    @OnClick(R.id.btn_create_comment)
    public void createComment() {
        if (!verifyFields())
            return;
        final Comment comment = getPostFromForm();
        RetrofitSession.getInstance().getCommentService().createComment(comment, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                Toast.makeText(getContext(), R.string.comment_created, Toast.LENGTH_SHORT).show();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStackImmediate();
            }

            @Override
            public void error(int code, String message) {
                Log.d("FAIL", message);
            }
        });
    }

}
