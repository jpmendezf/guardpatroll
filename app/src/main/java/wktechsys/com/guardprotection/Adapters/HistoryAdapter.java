package wktechsys.com.guardprotection.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wktechsys.com.guardprotection.Models.HistoryModel;
import wktechsys.com.guardprotection.Models.RoundModel;
import wktechsys.com.guardprotection.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    int lastPosition = 0;
    private Context context;
    List<HistoryModel> list = new ArrayList<>();
    private int currentPosition = 0;

    public HistoryAdapter(Context context, List<HistoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.historycard, parent, false);

        HistoryAdapter.ViewHolder vh = new HistoryAdapter.ViewHolder(v);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, rname, time, checkpoint,location;
        private Context context;
        RelativeLayout info,touch;

        public ViewHolder(View itemView) {
            super(itemView);

            touch = itemView.findViewById(R.id.touch);
            date = itemView.findViewById(R.id.date);
            rname = itemView.findViewById(R.id.round);
            }
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, final int position) {

        HistoryModel model = list.get(position);
        holder.date.setText(model.getDate());
        holder.rname.setText(model.getRoundname());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

