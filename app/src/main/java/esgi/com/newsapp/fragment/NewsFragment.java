package esgi.com.newsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import esgi.com.newsapp.R;
import esgi.com.newsapp.adapter.NewsAdapter;
import esgi.com.newsapp.model.News;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;

public class NewsFragment extends RootFragment {

    @BindView(R.id.progress)
    public ProgressBar progressBar;

    @BindView(R.id.rv_news)
    public RecyclerView rvNews;

    @BindView(R.id.fab)
    public FloatingActionButton fab;

    @BindView(R.id.srl_news)
    public SwipeRefreshLayout swipeRefreshLayoutNews;

    private List<News> newsList;
    private NewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        rvNews.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvNews.setLayoutManager(llm);

        setUpGestureListener();

        swipeRefreshLayoutNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsList();
            }
        });

        getNewsList();

        return view;
    }

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void getNewsList() {
        RetrofitSession.getInstance().getNewsService().getNewsList(new ApiResult<List<News>>() {
            @Override
            public void success(List<News> res) {
                newsList = res;
                rvNews.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                adapter = new NewsAdapter(getContext(), newsList);
                rvNews.setAdapter(adapter);
                if (swipeRefreshLayoutNews.isRefreshing())
                    swipeRefreshLayoutNews.setRefreshing(false);
            }

            @Override
            public void error(int code, String message) {
                progressBar.setVisibility(View.GONE);
                if (swipeRefreshLayoutNews.isRefreshing())
                    swipeRefreshLayoutNews.setRefreshing(false);
            }
        });
    }

    private void displayNews(int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment displayNewsFragment = new DisplayNewsFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_news_id), newsList.get(position).getId());
        bundle.putString(getString(R.string.bundle_news_title), newsList.get(position).getTitle());
        displayNewsFragment.setArguments(bundle);
        transaction.addToBackStack(null).replace(R.id.main_act_frame_content, displayNewsFragment, DisplayNewsFragment.DISPLAY_NEWS_TAG);
        transaction.commit();
    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = rvNews.findChildViewUnder(e.getX(), e.getY());
            int position = rvNews.getChildLayoutPosition(view);
            if (position != -1)//security for click out of bounds
                displayNews(position);
            return super.onSingleTapConfirmed(e);
        }
    }

    private void setUpGestureListener() {
        final GestureDetectorCompat detector = new GestureDetectorCompat(getActivity(), new NewsFragment.RecyclerViewOnGestureListener());
        rvNews.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                detector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    @Override
    public String getTitle() {
        return getString(R.string.nav_news);
    }
}
