package wktechsys.com.guardprotection.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import wktechsys.com.guardprotection.Adapters.AttendanceAdapter;
import wktechsys.com.guardprotection.Models.AttendenceModel;
import wktechsys.com.guardprotection.R;
import wktechsys.com.guardprotection.Utilities.Constant;
import wktechsys.com.guardprotection.Utilities.SessionManager;

public class AttendenceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AttendanceAdapter aAdapter;
    RelativeLayout back;
    StringRequest stringRequest,stringRequest1;
    RequestQueue mRequestQueue, mRequestQueue1;
    private List<AttendenceModel> list  = new ArrayList<>();
    public static final String TAG = "STag";
    SessionManager session;
    String id;
    ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_attendence);

        session = new SessionManager(getApplicationContext());

        back = findViewById(R.id.arrowback3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        recyclerView = findViewById(R.id.attendancerecycler);

        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AttendenceActivity.this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        aAdapter = new AttendanceAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(aAdapter);

        HashMap<String, String> attend = session.getUserDetails();
        id = attend.get(SessionManager.KEY_ID);

        mShimmerViewContainer = (ShimmerFrameLayout)findViewById(R.id.shimmer_view_container);

        Attendance();

    }

    public void  Attendance(){

        mRequestQueue = Volley.newRequestQueue(AttendenceActivity.this);

        stringRequest = new StringRequest(Request.Method.POST, Constant.ATTENDANCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //showMe.dismiss();
                        list.clear();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            String status = j.getString("status");

                            if(status.equals("500")) {
                                String msg = j.getString("msg");
                                session.logoutUser();
                                Intent i = new Intent(AttendenceActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                                Toast.makeText(AttendenceActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                          else if (status.equals("200")) {
                                JSONArray attendlist = j.getJSONArray("data");
                                if (attendlist != null && attendlist.length() > 0) {
                                    for (int i = 0; i <attendlist.length(); i++) {

                                        AttendenceModel attendmodel = new AttendenceModel();
                                        JSONObject getOne = attendlist.getJSONObject(i);

                                        attendmodel.setId(getOne.getString("id"));
                                        attendmodel.setIntime(getOne.getString("intime"));
                                        attendmodel.setOuttime(getOne.getString("outtime"));
                                        attendmodel.setDuration(getOne.getString("duration"));
                                        attendmodel.setDate(getOne.getString("date"));

                                        list.add(attendmodel);
                                        aAdapter = new AttendanceAdapter(getApplicationContext(),list);
                                        recyclerView.setAdapter(aAdapter);


                                        mShimmerViewContainer.stopShimmerAnimation();
                                        mShimmerViewContainer.setVisibility(View.GONE);

                                    }
                                }
                            } else {
                                Toast toast = Toast.makeText(AttendenceActivity.this, ""+j.getString("msg"),Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                        } catch (JSONException e) {
                            Log.e("TAG", "Something Went Wrong");
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkDialog();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", "d29985af97d29a80e40cd81016d939af");
                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("guard_id",id);
                return headers;
            }

        };
        stringRequest.setTag(TAG);
        mRequestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(AttendenceActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                Attendance();
            }
        });
        dialogs.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}
