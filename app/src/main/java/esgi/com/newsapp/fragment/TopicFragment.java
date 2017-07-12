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
import esgi.com.newsapp.adapter.TopicAdapter;
import esgi.com.newsapp.model.Topic;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;

public class TopicFragment extends RootFragment {

    @BindView(R.id.progress)
    public ProgressBar progressBar;

    @BindView(R.id.rv_topics)
    public RecyclerView rvTopics;

    @BindView(R.id.fab)
    public FloatingActionButton fab;

    @BindView(R.id.srl_topic)
    public SwipeRefreshLayout swipeRefreshLayoutTopic;

    private List<Topic> topicsList;
    private TopicAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        ButterKnife.bind(this, view);

        rvTopics.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvTopics.setLayoutManager(llm);

        setUpGestureListener();

        swipeRefreshLayoutTopic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTopicsList();
            }
        });

        getTopicsList();

        return view;
    }

    public static TopicFragment newInstance() {
        Bundle args = new Bundle();
        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void getTopicsList() {
        RetrofitSession.getInstance().getTopicService().getTopicList(new ApiResult<List<Topic>>() {
            @Override
            public void success(List<Topic> res) {
                topicsList = res;
                rvTopics.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                adapter = new TopicAdapter(topicsList);
                rvTopics.setAdapter(adapter);
                if (swipeRefreshLayoutTopic.isRefreshing())
                    swipeRefreshLayoutTopic.setRefreshing(false);
            }

            @Override
            public void error(int code, String message) {
                progressBar.setVisibility(View.GONE);
                if (swipeRefreshLayoutTopic.isRefreshing())
                    swipeRefreshLayoutTopic.setRefreshing(false);
            }
        });
    }

    private void displayNews(int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment displayTopicFragment = new DisplayTopicFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_topic_id), topicsList.get(position).getId());
        bundle.putString(getString(R.string.bundle_topic_title), topicsList.get(position).getTitle());
        bundle.putString(getString(R.string.bundle_topic_content), topicsList.get(position).getContent());
        displayTopicFragment.setArguments(bundle);
        transaction.addToBackStack(null).replace(R.id.main_act_frame_content, displayTopicFragment, DisplayTopicFragment.DISPLAY_TOPIC_TAG);
        transaction.commit();
    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = rvTopics.findChildViewUnder(e.getX(), e.getY());
            int position = rvTopics.getChildLayoutPosition(view);
            if (position != -1)//security for click out of bounds
                displayNews(position);
            return super.onSingleTapConfirmed(e);
        }
    }

    private void setUpGestureListener() {
        final GestureDetectorCompat detector = new GestureDetectorCompat(getActivity(), new TopicFragment.RecyclerViewOnGestureListener());
        rvTopics.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
