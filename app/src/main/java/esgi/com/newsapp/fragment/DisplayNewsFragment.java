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
import butterknife.OnClick;
import esgi.com.newsapp.R;
import esgi.com.newsapp.adapter.CommentAdapter;
import esgi.com.newsapp.database.RealmManager;
import esgi.com.newsapp.model.Comment;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;
import esgi.com.newsapp.utils.Network;
import esgi.com.newsapp.utils.PreferencesHelper;

/**
 * Created by Grunt on 12/07/2017.
 */

public class DisplayNewsFragment extends RootFragment {
    public static String DISPLAY_NEWS_TAG = "DISPLAYNEWS";

    private String title;
    private List<Comment> commentsList;
    private CommentAdapter adapter;

    @BindView(R.id.tv_content_news)
    public TextView tvContent;

    @BindView(R.id.rv_coms)
    public RecyclerView rvComs;

    @BindView(R.id.fab_news)
    public FloatingActionButton floatingActionButton;

    String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_display_news, container, false);
        ButterKnife.bind(this, view);
        sendCom();
        Bundle bundle = getArguments();
        title = bundle.getString(getString(R.string.bundle_news_title));
         id = bundle.getString(getString(R.string.bundle_news_id));
        String content = bundle.getString(getString(R.string.bundle_news_content));
        tvContent.setText(content);

        rvComs.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvComs.setLayoutManager(llm);

        getComs(id);

        setUpGestureListener();

        return view;
    }

    private void getComs(String id) {
        RetrofitSession.getInstance().getCommentService().getCommentsForNews(id, new ApiResult<List<Comment>>() {
            @Override
            public void success(List<Comment> res) {
                commentsList = res;
                for (Comment comment : res)
                    System.out.println(comment.getContent());
                System.out.println(res.size());
                adapter = new CommentAdapter(commentsList);
                rvComs.setAdapter(adapter);
            }

            @Override
            public void error(int code, String message) {

            }
        });
    }

    private void setUpGestureListener() {
        final GestureDetectorCompat detector = new GestureDetectorCompat(getActivity(), new DisplayNewsFragment.RecyclerViewOnGestureListener());
        rvComs.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            View view = rvComs.findChildViewUnder(e.getX(), e.getY());
            final int position = rvComs.getChildLayoutPosition(view);
            Log.d("LONGTOUCHPOSITION", String.valueOf(position));
            if(position != -1 && commentsList.get(position).getAuthor().compareTo(PreferencesHelper.getInstance().getUserId()) == 0){
                Log.d("LONGTOUCH", "AUTHOR");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.actions));
                builder.setItems(getResources().getStringArray(R.array.menu_admin), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                            deleteComForPosition(position);
                        else if (which == 1)
                            editComWithPosition(position);

                    }
                });
                builder.show();
            } else
                Log.d("LONGTOUCH", "NOT AUTHOR");
            super.onLongPress(e);

        }
    }

    private void editComWithPosition(final int position) {
        //todo implement news edition
    }

    private void deleteComForPosition(final int position) {
        RetrofitSession.getInstance().getCommentService().deleteComment(commentsList.get(position).getId(), new ApiResult<Void>() {
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

    private void sendCom(){
        if(Network.isConnectionAvailable()){
            final List<Comment> commentList = RealmManager.getCommentDAO().getCommentOff();
            for (int i = 0; i < commentList.size(); i ++){
                RetrofitSession.getInstance().getCommentService().createComment(commentList.get(i), new ApiResult<Void>() {
                    @Override
                    public void success(Void res) {
                        Toast.makeText(getContext(), "Com en mémoire envoyé", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void error(int code, String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }


    @OnClick(R.id.fab_news)
    public void goToCreateComment(){
        Log.d("FAB",id);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment createCommentFragment = new CreateCommentForNewsFragment();
        FragmentTransaction transaction = fm.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_news_id), id);

        createCommentFragment.setArguments(bundle);
        transaction.addToBackStack(null).replace(R.id.main_act_frame_content, createCommentFragment, DisplayTopicFragment.DISPLAY_TOPIC_TAG);
        transaction.commit();
    }
}
