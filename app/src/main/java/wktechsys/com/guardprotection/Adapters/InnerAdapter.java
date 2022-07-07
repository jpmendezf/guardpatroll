package wktechsys.com.guardprotection.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wktechsys.com.guardprotection.Models.HistoryModel;
import wktechsys.com.guardprotection.Models.InnerModel;
import wktechsys.com.guardprotection.Models.MissedModel;
import wktechsys.com.guardprotection.R;

public class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.ViewHolder>{

    int lastPosition = 0;
    private Context context;
    List<InnerModel> list = new ArrayList<>();


    public InnerAdapter(Context context, List<InnerModel> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView above,below, circle;
        TextView time, chkpt, chkloc;

        public ViewHolder(View itemView) {
            super(itemView);

            above = itemView.findViewById(R.id.aboveLine);
            below =  itemView.findViewById(R.id.belowLine);
            circle =  itemView.findViewById(R.id.pt);
            time = itemView.findViewById(R.id.time);
            chkpt = itemView.findViewById(R.id.checkpoint);
            chkloc = itemView.findViewById(R.id.location);
        }
    }

    @Override
    public InnerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.innercard, parent, false);

        InnerAdapter.ViewHolder vh = new InnerAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(InnerAdapter.ViewHolder holder, int position) {

        InnerModel model = list.get(position);
        holder.time.setText(model.getTime());
        holder.chkpt.setText(model.getCheckname());
        holder.chkloc.setText(model.getCheckloc());

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

        if (model.getTime().equals("00:00")){


            holder.circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.missed));

        }else {

            holder.circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.progress));
        }

    }


    @Override
    public int getItemCount() {
        return  list.size();
    }

}
