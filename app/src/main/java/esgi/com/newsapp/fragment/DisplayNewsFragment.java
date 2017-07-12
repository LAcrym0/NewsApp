package esgi.com.newsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import esgi.com.newsapp.R;
import esgi.com.newsapp.model.Comment;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;

/**
 * Created by Grunt on 12/07/2017.
 */

public class DisplayNewsFragment extends RootFragment {
    public static String DISPLAY_NEWS_TAG = "DISPLAYNEWS";

    private String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        title = bundle.getString(getString(R.string.bundle_news_title));
        String id = bundle.getString(getString(R.string.bundle_news_id));

        getComs(id);

        return view;
    }

    private void getComs(String id) {
        RetrofitSession.getInstance().getCommentService().getCommentsForNews(id, new ApiResult<List<Comment>>() {
            @Override
            public void success(List<Comment> res) {
                for (Comment comment : res)
                    System.out.println(comment.getContent());
                System.out.println(res.size());
            }

            @Override
            public void error(int code, String message) {

            }
        });
    }

    @Override
    public String getTitle() {
        return title;
    }
}
