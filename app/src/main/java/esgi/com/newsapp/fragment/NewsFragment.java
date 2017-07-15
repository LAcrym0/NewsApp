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
            if(position != -1 && newsList.get(position).getAuthor().compareTo(PreferencesHelper.getInstance().getUserId()) == 0){
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
        //todo implement news edition
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


    /*private void setUpGestureListener() {
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

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();
                if(position != -1){
                    if (newsList.get(position).getAuthor().compareTo(PreferencesHelper.getInstance().getUserId()) == 0) {
                        Log.d("SWIPED", String.valueOf(position));
                        if (direction == ItemTouchHelper.LEFT) {
                            RetrofitSession.getInstance().getNewsService().deleteNews(newsList.get(position).getId(), new ApiResult<Void>() {
                                @Override
                                public void success(Void res) {
                                    adapter.remove(position);
                                }

                                @Override
                                public void error(int code, String message) {

                                }
                            });
                        }
                    }
                }



            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_mode_edit_black_24dp );
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_delete_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };




        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvNews);
    }*/

    @Override
    public String getTitle() {
        return getString(R.string.nav_news);
    }
}
