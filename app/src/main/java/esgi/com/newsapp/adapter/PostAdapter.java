package esgi.com.newsapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import esgi.com.newsapp.R;
import esgi.com.newsapp.model.Comment;
import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.utils.DateConverter;

/**
 * Created by Grunt on 12/07/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Post> postsList;

    public PostAdapter(List<Post> postsList) {
        this.postsList = postsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PostViewHolder postViewHolder = (PostViewHolder) holder;
        postViewHolder.tvTitle.setText(postsList.get(position).getTitle());
        postViewHolder.tvContent.setText(postsList.get(position).getContent());
        if (postsList.get(position).getDate() != null) {
            postViewHolder.tvDate.setText(DateConverter.toHumanReadableDate(postsList.get(position).getDate()));
            postViewHolder.tvDate.setVisibility(View.VISIBLE);
        } else {
            postViewHolder.tvDate.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    static private class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent, tvDate;

        PostViewHolder(View itemView) {
            super(itemView);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tv_post_title);
            this.tvContent = (TextView) itemView.findViewById(R.id.tv_post_content);
            this.tvDate = (TextView) itemView.findViewById(R.id.tv_post_date);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
