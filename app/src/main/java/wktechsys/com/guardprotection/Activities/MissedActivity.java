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
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wktechsys.com.guardprotection.Adapters.MissedptAdapter;
import wktechsys.com.guardprotection.Models.MissedModel;
import wktechsys.com.guardprotection.R;
import wktechsys.com.guardprotection.Utilities.Constant;
import wktechsys.com.guardprotection.Utilities.SessionManager;

public class MissedActivity extends AppCompatActivity {

    RelativeLayout back;
    RecyclerView recyclerView;
    MissedptAdapter mpAdapter;
    StringRequest stringRequest,stringRequest1;
    RequestQueue mRequestQueue, mRequestQueue1;
    private List<MissedModel> list  = new ArrayList<>();
    public static final String TAG = "STag";
    SessionManager session;
    String id;
    ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed);

        back = (RelativeLayout) findViewById(R.id.arrowback2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);

        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MissedActivity.this, LinearLayoutManager.VERTICAL, true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mpAdapter = new MissedptAdapter(MissedActivity.this,list);
        recyclerView.setAdapter(mpAdapter);

        session = new SessionManager(MissedActivity.this);
        HashMap<String, String> attend = session.getUserDetails();
        id = attend.get(SessionManager.KEY_ID);

        mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        Missedpoint();
    }

    public void Missedpoint(){

        mRequestQueue = Volley.newRequestQueue(MissedActivity.this);

        stringRequest = new StringRequest(Request.Method.POST, Constant.MISSEDPT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //showMe.dismiss();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        list.clear();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            String status = j.getString("status");

                            if(status.equals("500")) {
                                String msg = j.getString("msg");
                                session.logoutUser();
                                Intent i = new Intent(MissedActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                                Toast.makeText(MissedActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                           else if (status.equals("200")) {
                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {
                                    for (int i = 0; i <applist.length(); i++) {

                                        MissedModel model = new MissedModel();
                                        JSONObject getOne = applist.getJSONObject(i);

                                        model.setId(getOne.getString("id"));
                                        model.setRound(getOne.getString("round"));
                                        model.setName(getOne.getString("name"));
                                        model.setLocation(getOne.getString("location"));

                                        list.add(model);
                                        mpAdapter = new MissedptAdapter(MissedActivity.this,list);
                                        recyclerView.setAdapter(mpAdapter);

                                    }
                                }
                            } else {
                                //   showMe.dismiss();
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);
                                Toast toast=Toast.makeText(MissedActivity.this, ""+j.getString("msg"),Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                                //  nodata.setVisibility(View.VISIBLE);
                                //  recyclerView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            // showMe.dismiss();
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            Log.e("TAG", "Something Went Wrong");
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //showMe.dismiss();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
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
        final Dialog dialogs = new Dialog(MissedActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                Missedpoint();

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
