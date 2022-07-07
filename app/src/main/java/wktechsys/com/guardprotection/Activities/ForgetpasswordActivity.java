package wktechsys.com.guardprotection.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import wktechsys.com.guardprotection.R;
import wktechsys.com.guardprotection.Utilities.Constant;

public class ForgetpasswordActivity extends AppCompatActivity {

    EditText email;
    Button forget;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_forgetpassword);

        email = (EditText)findViewById(R.id.email);
        forget = (Button)findViewById(R.id.submit);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPassword();
            }
        });

    }

    public void ForgetPassword()
    {
        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(ForgetpasswordActivity.this);

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FORGETPASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Matching server responce message to our text.
                        JSONObject j = null;
                        try {
                            j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            if (status.equals("200")) {
                                String suc = j.getString("msg");
                                // If response matched then show the toast.
                                Toast.makeText(ForgetpasswordActivity.this, suc, Toast.LENGTH_SHORT).show();

                                // Finish the current Login activity.
                                finish();
                             //   session.createLoginSession(mobile.getText().toString(), password.getText().toString(), id);

                                // Opening the user profile activity using intent.
                                Intent intent = new Intent(ForgetpasswordActivity.this, LoginActivity.class);

                                startActivity(intent);

                            } else {
                                String msg = j.getString("msg");
                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(ForgetpasswordActivity.this, msg, Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

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
                params.put("mail", email.getText().toString());
                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(ForgetpasswordActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);


    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(ForgetpasswordActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                ForgetPassword();

            }
        });
        dialogs.show();
    }
}
