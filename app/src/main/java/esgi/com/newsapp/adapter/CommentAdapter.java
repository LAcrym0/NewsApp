package esgi.com.newsapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import esgi.com.newsapp.R;
import esgi.com.newsapp.model.Comment;
import esgi.com.newsapp.utils.DateConverter;

/**
 * Created by Grunt on 12/07/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Comment> commentsList;

    public CommentAdapter(List<Comment> commentsList) {
        this.commentsList = commentsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
        commentViewHolder.tvTitle.setText(commentsList.get(position).getTitle());
        commentViewHolder.tvContent.setText(commentsList.get(position).getContent());
        if (commentsList.get(position).getDate() != null) {
            commentViewHolder.tvDate.setText(DateConverter.toHumanReadableDate(commentsList.get(position).getDate()));
            commentViewHolder.tvDate.setVisibility(View.VISIBLE);
        } else {
            commentViewHolder.tvDate.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    static private class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent, tvDate;

        CommentViewHolder(View itemView) {
            super(itemView);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tv_comment_title);
            this.tvContent = (TextView) itemView.findViewById(R.id.tv_comment_content);
            this.tvDate = (TextView) itemView.findViewById(R.id.tv_comment_date);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void remove(int position){
        commentsList.remove(position);
        notifyItemRemoved(position);
    }
}
