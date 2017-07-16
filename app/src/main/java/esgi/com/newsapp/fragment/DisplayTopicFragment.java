package esgi.com.newsapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import esgi.com.newsapp.R;
import esgi.com.newsapp.adapter.PostAdapter;
import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;
import esgi.com.newsapp.utils.PreferencesHelper;

/**
 * Created by Grunt on 12/07/2017.
 */

public class DisplayTopicFragment extends RootFragment {
    public static String DISPLAY_TOPIC_TAG = "DISPLAYTOPIC";

    private String title;
    private List<Post> postsList;
    private PostAdapter adapter;

    @BindView(R.id.tv_content_topic)
    public TextView tvContent;

    @BindView(R.id.rv_posts)
    public RecyclerView rvPosts;

    @BindView(R.id.fab)
    public FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_display_topic, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        title = bundle.getString(getString(R.string.bundle_topic_title));
        final String id = bundle.getString(getString(R.string.bundle_topic_id));
        String content = bundle.getString(getString(R.string.bundle_topic_content));
        tvContent.setText(content);

        rvPosts.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(llm);

        setUpGestureListener();

        getPosts(id);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FAB",id);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment createPostFragment = new CreatePostForTopicFragment();
                FragmentTransaction transaction = fm.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.bundle_topic_id), id);

                createPostFragment.setArguments(bundle);
                transaction.addToBackStack(null).replace(R.id.main_act_frame_content, createPostFragment, DisplayTopicFragment.DISPLAY_TOPIC_TAG);
                transaction.commit();
            }
        });

        return view;
    }

    private void setUpGestureListener() {
        final GestureDetectorCompat detector = new GestureDetectorCompat(getActivity(), new DisplayTopicFragment.RecyclerViewOnGestureListener());
        rvPosts.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

    private void getPosts(String id) {
        RetrofitSession.getInstance().getPostService().getPostsForTopic(id, new ApiResult<List<Post>>() {
            @Override
            public void success(List<Post> res) {
                postsList = res;
                for (Post post : res)
                    System.out.println(post.getContent());
                System.out.println(res.size());
                adapter = new PostAdapter(postsList);
                rvPosts.setAdapter(adapter);
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

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            View view = rvPosts.findChildViewUnder(e.getX(), e.getY());
            final int position = rvPosts.getChildLayoutPosition(view);
            Log.d("LONGTOUCHPOSITION", String.valueOf(position));
            if(position != -1 && postsList.get(position).getAuthor() != null && postsList.get(position).getAuthor().compareTo(PreferencesHelper.getInstance().getUserId()) == 0){
                Log.d("LONGTOUCH", "AUTHOR");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.actions));
                builder.setItems(getResources().getStringArray(R.array.menu_admin), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                            deletePostForPosition(position);
                        else if (which == 1)
                            editPostWithPosition(position);
                    }
                });
                builder.show();
            } else
                Log.d("LONGTOUCH", "NOT AUTHOR");
            super.onLongPress(e);
        }
    }

    private void editPostWithPosition(final int position) {
        //todo implement news edition
    }

    private void deletePostForPosition(final int position) {
        RetrofitSession.getInstance().getCommentService().deleteComment(postsList.get(position).getId(), new ApiResult<Void>() {
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
}
