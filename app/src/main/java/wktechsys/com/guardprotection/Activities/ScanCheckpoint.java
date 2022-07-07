package wktechsys.com.guardprotection.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
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
import wktechsys.com.guardprotection.Models.CheckPointModel;
import wktechsys.com.guardprotection.Utilities.Constant;
import wktechsys.com.guardprotection.R;
import wktechsys.com.guardprotection.Adapters.RoundAdapter;
import wktechsys.com.guardprotection.Utilities.SessionManager;

public class ScanCheckpoint extends AppCompatActivity {

    RecyclerView recyclerView;
    RoundAdapter rAdapter;
    RelativeLayout strt;
    SessionManager session;
    RelativeLayout rr, back;

    private Camera camera;
    boolean showingFirst = true;
    ImageView flash;

    StringRequest stringRequest, stringRequest1;
    RequestQueue mRequestQueue, mRequestQueue1;
    private List<CheckPointModel> list = new ArrayList<>();
    public static final String TAG = "STag";
    String id, code = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_scan_checkpoint);

        session = new SessionManager(getApplicationContext());

        Intent i = getIntent();
        code = i.getStringExtra("code");

        rr = (RelativeLayout) findViewById(R.id.r1);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        rAdapter = new RoundAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(rAdapter);

        HashMap<String, String> roles = session.getUserDetails();
        id = roles.get(SessionManager.KEY_ID);

        strt = (RelativeLayout) findViewById(R.id.strtscanning);
        strt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScanCheckpoint.this, ScanActivity.class);
                finish();
                startActivity(i);
            }
        });

        back = (RelativeLayout) findViewById(R.id.arrowback2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        flash = findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (showingFirst == true) {

                    showingFirst = false;
                    camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();

                } else {

                    showingFirst = true;
                    camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                    camera.release();
                }
            }
        });


        CList();
    }

    public void CList() {

        final ProgressDialog showMe = new ProgressDialog(ScanCheckpoint.this, AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();

        mRequestQueue = Volley.newRequestQueue(ScanCheckpoint.this);

        stringRequest = new StringRequest(Request.Method.POST, Constant.SCAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showMe.dismiss();
                        list.clear();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            String status = j.getString("status");
                            String msg = j.getString("msg");

                             if(status.equals("500")) {
                                session.logoutUser();
                                Intent i = new Intent(ScanCheckpoint.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                                Toast.makeText(ScanCheckpoint.this, msg, Toast.LENGTH_SHORT).show();
                            }
                           else if (status.equals("200")) {
                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {
                                    for (int i = 0; i < applist.length(); i++) {

                                        CheckPointModel model = new CheckPointModel();
                                        JSONObject getOne = applist.getJSONObject(i);

                                        model.setId(getOne.getString("id"));
                                        model.setDate(getOne.getString("date"));
                                        model.setTime(getOne.getString("time"));
                                        model.setCheckno(getOne.getString("name"));
                                        model.setLocation(getOne.getString("location"));

                                        list.add(model);
                                        rAdapter = new RoundAdapter(getApplicationContext(), list);
                                        recyclerView.setAdapter(rAdapter);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        rr.setVisibility(View.GONE);
                                        if (!msg.equals("Success")) {

                                            Toast.makeText(ScanCheckpoint.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    showMe.dismiss();
                                    recyclerView.setVisibility(View.GONE);
                                    rr.setVisibility(View.VISIBLE);
                                    Toast.makeText(ScanCheckpoint.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                showMe.dismiss();
                                recyclerView.setVisibility(View.GONE);
                                rr.setVisibility(View.VISIBLE);
                                Toast.makeText(ScanCheckpoint.this, msg, Toast.LENGTH_SHORT).show();
                                //  nodata.setVisibility(View.VISIBLE);
                                //  recyclerView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            showMe.dismiss();
                            recyclerView.setVisibility(View.GONE);
                            rr.setVisibility(View.VISIBLE);
                            Log.e("TAG", "Something Went Wrong");
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMe.dismiss();
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

                if (code == null) {

                    headers.put("qrcode", "0");

                } else {

                    headers.put("qrcode", code);
                }
                headers.put("guard_id", id);
                return headers;
            }

        };
        stringRequest.setTag(TAG);
        mRequestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(ScanCheckpoint.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                CList();

            }
        });
        dialogs.show();
    }
}