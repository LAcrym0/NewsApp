package esgi.com.newsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import esgi.com.newsapp.R;
import esgi.com.newsapp.adapter.CommentAdapter;
import esgi.com.newsapp.adapter.PostAdapter;
import esgi.com.newsapp.model.Comment;
import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;

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
}
