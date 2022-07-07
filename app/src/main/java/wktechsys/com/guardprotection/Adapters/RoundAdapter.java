package wktechsys.com.guardprotection.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wktechsys.com.guardprotection.Models.CheckPointModel;
import wktechsys.com.guardprotection.R;

public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.ViewHolder> {

    int lastPosition = 0;
    private Context context;
    List<CheckPointModel> list = new ArrayList<>();


    public RoundAdapter(Context context, List<CheckPointModel> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView above, below;
        TextView date, time, chkno, loc;

        public ViewHolder(View itemView) {


            super(itemView);

            above = (ImageView) itemView.findViewById(R.id.aboveLine);
            below = (ImageView) itemView.findViewById(R.id.belowLine);

            date = itemView.findViewById(R.id.textdate);
            time = itemView.findViewById(R.id.texttime);
            chkno = itemView.findViewById(R.id.checkpoint);
            loc = itemView.findViewById(R.id.loc);
        }
    }

    @Override
    public RoundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkpointcard, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == 0 && position == list.size() - 1) {

            holder.above.setVisibility(View.INVISIBLE);
            holder.below.setVisibility(View.INVISIBLE);

        }else if (position == 0){

            holder.below.setVisibility(View.INVISIBLE);

        }else if (position == list.size() - 1) {

            holder.above.setVisibility(View.INVISIBLE);

        } else {
            holder.above.setVisibility(View.VISIBLE);
            holder.below.setVisibility(View.VISIBLE);
        }

        CheckPointModel model = list.get(position);
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.chkno.setText(model.getCheckno());
        holder.loc.setText(model.getLocation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
