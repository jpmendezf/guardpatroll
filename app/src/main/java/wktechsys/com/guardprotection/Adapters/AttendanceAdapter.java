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
import wktechsys.com.guardprotection.R;

public class AttendanceAdapter  extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder>{


    int lastPosition = 0;
    private Context context;
    List<AttendenceModel> list = new ArrayList<>();

    public AttendanceAdapter(Context context, List<AttendenceModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendancecard, parent, false);

        AttendanceAdapter.ViewHolder vh = new AttendanceAdapter.ViewHolder(v);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,intime,outtime,duration;

        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            intime = itemView.findViewById(R.id.inTime);
            outtime = itemView.findViewById(R.id.outTime);
            duration = itemView.findViewById(R.id.duration);
        }
    }

    @Override
    public void onBindViewHolder(AttendanceAdapter.ViewHolder holder, int position) {

        AttendenceModel model = list.get(position);
        holder.date.setText(model.getDate());
        holder.intime.setText(model.getIntime());
        holder.outtime.setText(model.getOuttime());
       holder.duration.setText(model.getDuration());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}

