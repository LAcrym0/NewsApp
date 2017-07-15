package esgi.com.newsapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import esgi.com.newsapp.R;
import esgi.com.newsapp.model.News;
import esgi.com.newsapp.utils.DateConverter;

/**
 * Created by Grunt on 12/07/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<News> newsList;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
        newsViewHolder.tvTitle.setText(newsList.get(position).getTitle());
        newsViewHolder.tvContent.setText(newsList.get(position).getContent());
        if (newsList.get(position).getDate() != null) {
            newsViewHolder.tvDate.setText(DateConverter.toHumanReadableDate(newsList.get(position).getDate()));
            newsViewHolder.tvDate.setVisibility(View.VISIBLE);
        } else {
            newsViewHolder.tvDate.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static private class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent, tvDate;

        NewsViewHolder(View itemView) {
            super(itemView);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            this.tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            this.tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void remove(int position){
        newsList.remove(position);
        notifyItemRemoved(position);
    }
}
