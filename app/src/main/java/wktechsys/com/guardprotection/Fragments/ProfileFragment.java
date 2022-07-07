package wktechsys.com.guardprotection.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import wktechsys.com.guardprotection.Activities.DashboardActivity;
import wktechsys.com.guardprotection.R;
import wktechsys.com.guardprotection.Utilities.SessionManager;

public class ProfileFragment extends Fragment {

    TextView gname, gphone, gemail, gagency, gid;
    SessionManager session;
    String id,name,number,email,agency,guardid;
    RelativeLayout back;
    LinearLayout ll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fabric.with(getActivity(), new Crashlytics());
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        session = new SessionManager(getActivity());
        HashMap<String, String> users = session.getUserDetails();
      //  id = users.get(session.KEY_ID);
        name = users.get(session.KEY_NAME);
        email = users.get(session.KEY_EMAIL);
        number = users.get(session.KEY_NUMBER);
        agency = users.get(session.KEY_AGENCY);
        guardid = users.get(session.KEY_GUARDID);

        gid = (TextView) v.findViewById(R.id.idnumber);
        gname = (TextView) v.findViewById(R.id.name);
        gphone = (TextView) v.findViewById(R.id.phone);
        gemail = (TextView) v.findViewById(R.id.email);
        gagency = (TextView) v.findViewById(R.id.agency);

        back = v.findViewById(R.id.arrowback1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DashboardActivity.class);
                startActivity(i);
                getActivity().finish();

            }
        });

        gname.setText(name);
        gphone.setText(number);
        gemail.setText(email);
        gid.setText(guardid);
        gagency.setText(agency);

        return v;

    }
}