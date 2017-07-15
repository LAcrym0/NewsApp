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
import esgi.com.newsapp.adapter.TopicAdapter;
import esgi.com.newsapp.model.Topic;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;
import esgi.com.newsapp.utils.PreferencesHelper;

public class TopicFragment extends RootFragment {

    @BindView(R.id.progress)
    public ProgressBar progressBar;

    @BindView(R.id.rv_topics)
    public RecyclerView rvTopics;

    private FloatingActionButton fab;

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

        fab = getMainActivity().getFab();
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FAB", "CLICK");
                createTopic();
            }
        });

        return view;
    }

    public static TopicFragment newInstance() {
        Bundle args = new Bundle();
        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void createTopic() {
        fab.setVisibility(View.GONE);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment createTopicFragment = new CreateTopicFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.addToBackStack(null).replace(R.id.main_act_frame_content, createTopicFragment, DisplayNewsFragment.DISPLAY_NEWS_TAG);
        transaction.commit();
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

    private void displayTopic(int position) {
        fab.setVisibility(View.GONE);
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
                displayTopic(position);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View view = rvTopics.findChildViewUnder(e.getX(), e.getY());
            final int position = rvTopics.getChildLayoutPosition(view);
            Log.d("LONGTOUCHPOSITION", String.valueOf(position));
            if(position != -1 && topicsList.get(position).getAuthor().compareTo(PreferencesHelper.getInstance().getUserId()) == 0){
                Log.d("LONGTOUCH", "AUTHOR");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.actions);
                builder.setItems(getResources().getStringArray(R.array.menu_admin), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                            deleteTopicForPosition(position);
                        else if (which == 1)
                            editTopicWithPosition(position);

                    }
                });
                builder.show();
            } else
                Log.d("LONGTOUCH", "NOT AUTHOR");
            super.onLongPress(e);

        }
    }

    private void editTopicWithPosition(final int position) {
        //todo implement news edition
    }

    private void deleteTopicForPosition(final int position) {
        RetrofitSession.getInstance().getTopicService().deleteTopic(topicsList.get(position).getId(), new ApiResult<Void>() {
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
        return getString(R.string.nav_topics);
    }
}
