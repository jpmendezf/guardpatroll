package wktechsys.com.guardprotection.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import wktechsys.com.guardprotection.Utilities.Constant;
import wktechsys.com.guardprotection.R;
import wktechsys.com.guardprotection.Utilities.SessionManager;

public class LoginActivity extends AppCompatActivity {

    Button loginbtn;
    SessionManager session;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    EditText mobile, password;
    TextView forgetP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        session = new SessionManager(getApplicationContext());

        mobile = (EditText) findViewById(R.id.mobile);
        password = (EditText) findViewById(R.id.password);
        forgetP = (TextView)findViewById(R.id.forgetPass);

        forgetP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, ForgetpasswordActivity.class);
                startActivity(in);
            }
        });


        if (session.isLoggedIn() == true) {

            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();

        }

        loginbtn = (Button) findViewById(R.id.loginBtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {

                    Toast.makeText(LoginActivity.this, " Please fill all the fields!!", Toast.LENGTH_SHORT).show();

                } else {
                    Login();
                }
            }
        });

    }

    // Creating user login function.
    public void Login() {
        // Assigning Activity this to progress dialog.
        progressDialog = new ProgressDialog(LoginActivity.this);
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(LoginActivity.this);

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Matching server responce message to our text.
                        JSONObject j = null;
                        try {
                            j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            if (status.equals("200")) {
                                String suc = j.getString("msg");
                                String id = j.getString("id");
                                String name = j.getString("name");
                                String email = j.getString("email");
                                String agency = j.getString("agency");
                                String guardid = j.getString("guard_id");
                                // If response matched then show the toast.
                                Toast.makeText(LoginActivity.this, suc, Toast.LENGTH_SHORT).show();

                                // Finish the current Login activity.
                                finish();
                                session.createLoginSession(mobile.getText().toString(), password.getText().toString(), id, name, email, agency, guardid);

                                // Opening the user profile activity using intent.
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);

                                startActivity(intent);

                            } else {
                                String msg = j.getString("msg");
                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        NetworkDialog();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", "d29985af97d29a80e40cd81016d939af");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("mobile", mobile.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(LoginActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                Login();

            }
        });
        dialogs.show();
    }

}
