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
import esgi.com.newsapp.model.News;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;
import esgi.com.newsapp.utils.DateConverter;

/**
 * Created by Grunt on 15/07/2017.
 */

public class CreateNewsFragment extends RootFragment {
    private final int OFFLINE = -22;

    @BindView(R.id.et_content_news)
    public EditText etContent;

    @BindView(R.id.et_title_news)
    public EditText etTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_create_news, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public String getTitle() {
        return getString(R.string.news_creation);
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


    private News getNewsFromForm(){
        News news = new News();
        news.setTitle(etTitle.getText().toString());
        news.setContent(etContent.getText().toString());
        news.setDate(DateConverter.getCurrentFormattedDate());
        Log.d("DATE", DateFormat.getDateTimeInstance().format(new Date()));

        return news;
    }

    @OnClick(R.id.btn_create_news)
    public void createNews() {
        if (!verifyFields())
            return;
        final News news = getNewsFromForm();
        RetrofitSession.getInstance().getNewsService().createNews(news, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                Toast.makeText(getContext(), R.string.news_created, Toast.LENGTH_SHORT).show();
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
