package esgi.com.newsapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import esgi.com.newsapp.R;
import esgi.com.newsapp.model.Topic;
import esgi.com.newsapp.utils.DateConverter;

/**
 * Created by Grunt on 12/07/2017.
 */

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Topic> topicsList;

    public TopicAdapter(List<Topic> topicsList) {
        this.topicsList = topicsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_topic, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TopicViewHolder topicViewHolder = (TopicViewHolder) holder;
        topicViewHolder.tvTitle.setText(topicsList.get(position).getTitle());
        topicViewHolder.tvContent.setText(topicsList.get(position).getContent());
        if (topicsList.get(position).getDate() != null) {
            topicViewHolder.tvDate.setText(DateConverter.toHumanReadableDate(topicsList.get(position).getDate()));
            topicViewHolder.tvDate.setVisibility(View.VISIBLE);
        } else {
            topicViewHolder.tvDate.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return topicsList.size();
    }

    static private class TopicViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent, tvDate;

        TopicViewHolder(View itemView) {
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
        topicsList.remove(position);
        notifyItemRemoved(position);
    }
}
