package wktechsys.com.guardprotection.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wktechsys.com.guardprotection.Models.AttendenceModel;
import wktechsys.com.guardprotection.Models.RoundModel;
import wktechsys.com.guardprotection.R;

public class TotalRoundAdapter extends RecyclerView.Adapter<TotalRoundAdapter.ViewHolder> {

    int lastPosition = 0;
    private Context context;
    List<RoundModel> list = new ArrayList<>();

    public TotalRoundAdapter(Context context, List<RoundModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TotalRoundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rounddetails, parent, false);

        TotalRoundAdapter.ViewHolder vh = new TotalRoundAdapter.ViewHolder(v);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, time;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.roundname);
            time = itemView.findViewById(R.id.timing);

        }
    }

    @Override
    public void onBindViewHolder(TotalRoundAdapter.ViewHolder holder, int position) {

        RoundModel model = list.get(position);
        holder.name.setText(model.getRname());
        holder.time.setText("( " + model.getTime() + " )");

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}

