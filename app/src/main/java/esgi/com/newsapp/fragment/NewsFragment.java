package esgi.com.newsapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import esgi.com.newsapp.R;
import esgi.com.newsapp.adapter.NewsAdapter;
import esgi.com.newsapp.model.News;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;
import esgi.com.newsapp.utils.PreferencesHelper;

public class NewsFragment extends RootFragment {
    private static final int TAG_NEWS = 3;

    @BindView(R.id.progress)
    public ProgressBar progressBar;

    @BindView(R.id.rv_news)
    public RecyclerView rvNews;

    private FloatingActionButton fab;

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

        fab = getMainActivity().getFab();
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FAB", "CLICK");
                createNews();
            }
        });

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
                adapter = new NewsAdapter(newsList);
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

    private void createNews() {
        fab.setVisibility(View.GONE);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment createNewsFragment = new CreateNewsFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.addToBackStack(null).replace(R.id.main_act_frame_content, createNewsFragment, DisplayNewsFragment.DISPLAY_NEWS_TAG);
        transaction.commit();
    }

    private void displayNews(int position) {
        fab.setVisibility(View.GONE);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment displayNewsFragment = new DisplayNewsFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_news_id), newsList.get(position).getId());
        bundle.putString(getString(R.string.bundle_news_title), newsList.get(position).getTitle());
        bundle.putString(getString(R.string.bundle_news_content), newsList.get(position).getContent());
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

        @Override
        public void onLongPress(MotionEvent e) {
            View view = rvNews.findChildViewUnder(e.getX(), e.getY());
            final int position = rvNews.getChildLayoutPosition(view);
            Log.d("LONGTOUCHPOSITION", String.valueOf(position));
            if(position != -1 && newsList.get(position).getAuthor() != null && newsList.get(position).getAuthor().compareTo(PreferencesHelper.getInstance().getUserId()) == 0){
                Log.d("LONGTOUCH", "AUTHOR");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.actions));
                builder.setItems(getResources().getStringArray(R.array.menu_admin), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                            deleteNewsForPosition(position);
                        else if (which == 1)
                            editNewsWithPosition(position);

                    }
                });
                builder.show();
            } else
                Log.d("LONGTOUCH", "NOT AUTHOR");
            super.onLongPress(e);

        }
    }

    private void editNewsWithPosition(final int position) {
        fab.setVisibility(View.GONE);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment editFragment = new EditFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_id), newsList.get(position).getId());
        bundle.putString(getString(R.string.bundle_content), newsList.get(position).getContent());
        bundle.putString(getString(R.string.bundle_title), newsList.get(position).getTitle());
        bundle.putInt(getString(R.string.bundle_type), TAG_NEWS);
        editFragment.setArguments(bundle);
        transaction.addToBackStack(null).replace(R.id.main_act_frame_content, editFragment, DisplayTopicFragment.DISPLAY_TOPIC_TAG);
        transaction.commit();
    }

    private void deleteNewsForPosition(final int position) {
        RetrofitSession.getInstance().getNewsService().deleteNews(newsList.get(position).getId(), new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                adapter.remove(position);
                Toast.makeText(getContext(), getString(R.string.deleted), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int code, String message) {
                Toast.makeText(getContext(), getString(R.string.error_deleting), Toast.LENGTH_SHORT).show();
            }
        });
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
