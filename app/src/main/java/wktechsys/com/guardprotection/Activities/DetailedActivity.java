package wktechsys.com.guardprotection.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import wktechsys.com.guardprotection.Adapters.InnerAdapter;
import wktechsys.com.guardprotection.Adapters.RoundAdapter;
import wktechsys.com.guardprotection.Fragments.HistoryFragment;
import wktechsys.com.guardprotection.Models.CheckPointModel;
import wktechsys.com.guardprotection.Models.InnerModel;
import wktechsys.com.guardprotection.R;
import wktechsys.com.guardprotection.Utilities.Constant;
import wktechsys.com.guardprotection.Utilities.SessionManager;

public class DetailedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InnerAdapter iAdapter;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    private List<InnerModel> list = new ArrayList<>();
    public static final String TAG = "STag";
    String id,date = "",rid = "",rname = "";
    SessionManager session;
    TextView name;
    RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_detailed);

        name = findViewById(R.id.rname);
        back = findViewById(R.id.arrowback5);
        recyclerView = (RecyclerView) findViewById(R.id.inner);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        iAdapter = new InnerAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(iAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              onBackPressed();
            }
        });

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> roles = session.getUserDetails();
        id = roles.get(SessionManager.KEY_ID);

        Intent i = getIntent();
        date = i.getStringExtra("date");
        rid = i.getStringExtra("rid");
        rname = i.getStringExtra("rname");

        name.setText(rname);

        Detailed(date, rid);
    }

    public void Detailed(final String date, final String rid) {

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        stringRequest = new StringRequest(Request.Method.POST, Constant.DETAILED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //showMe.dismiss();
                        list.clear();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            String status = j.getString("status");
                            String msg = j.getString("msg");
                            if(status.equals("500")) {
                                session.logoutUser();
                                Intent i = new Intent(DetailedActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                                Toast.makeText(DetailedActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            else if (status.equals("200")) {
                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {
                                    for (int i = 0; i < applist.length(); i++) {

                                        InnerModel model = new InnerModel();
                                        JSONObject getOne = applist.getJSONObject(i);

                                        model.setCheckname(getOne.getString("name"));
                                        model.setCheckloc(getOne.getString("location"));
                                        model.setTime(getOne.getString("time"));

                                        list.add(model);
                                        iAdapter = new InnerAdapter(getApplicationContext(), list);
                                        recyclerView.setAdapter(iAdapter);

                                    }
                                } else {
                                    // showMe.dismiss();

                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            // showMe.dismiss();

                            Log.e("TAG", "Something Went Wrong");
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkDialog(date,rid);
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

                headers.put("guard_id", id);
                headers.put("round_id", rid);
                headers.put("date", date);
                return headers;
            }

        };
        stringRequest.setTag(TAG);
        mRequestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void NetworkDialog(final String v1, final  String v2) {
        final Dialog dialogs = new Dialog(DetailedActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
               Detailed(v1,v2);
            }
        });
        dialogs.show();
    }
}
