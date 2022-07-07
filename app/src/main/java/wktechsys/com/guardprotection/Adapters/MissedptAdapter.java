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

import wktechsys.com.guardprotection.Models.MissedModel;
import wktechsys.com.guardprotection.R;

public class MissedptAdapter  extends RecyclerView.Adapter<MissedptAdapter.ViewHolder>{

    int lastPosition = 0;
    private Context context;
    List<MissedModel> list = new ArrayList<>();


    public MissedptAdapter(Context context, List<MissedModel> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView above,below;
        TextView round, chkpt, chkname;

        public ViewHolder(View itemView) {
            super(itemView);

            above = (ImageView) itemView.findViewById(R.id.aboveLine1);
            below = (ImageView) itemView.findViewById(R.id.belowLine1);

            round = itemView.findViewById(R.id.round);
            chkpt = itemView.findViewById(R.id.checkpoint);
            chkname = itemView.findViewById(R.id.location);
        }
    }

    @Override
    public MissedptAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.missedptcard, parent, false);

        MissedptAdapter.ViewHolder vh = new MissedptAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MissedptAdapter.ViewHolder holder, int position) {

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

        MissedModel model = list.get(position);
        holder.round.setText(model.getRound());
        holder.chkpt.setText(model.getName());
        holder.chkname.setText(model.getLocation());

    }


    @Override
    public int getItemCount() {
        return  list.size();
    }

}
