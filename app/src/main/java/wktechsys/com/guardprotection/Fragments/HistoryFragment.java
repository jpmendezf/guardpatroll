package wktechsys.com.guardprotection.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import wktechsys.com.guardprotection.Activities.DashboardActivity;
import wktechsys.com.guardprotection.Activities.DetailedActivity;
import wktechsys.com.guardprotection.Activities.LoginActivity;
import wktechsys.com.guardprotection.Adapters.HistoryAdapter;
import wktechsys.com.guardprotection.Models.HistoryModel;
import wktechsys.com.guardprotection.Models.InnerModel;
import wktechsys.com.guardprotection.R;
import wktechsys.com.guardprotection.Utilities.Constant;
import wktechsys.com.guardprotection.Utilities.RecyclerTouchListener;
import wktechsys.com.guardprotection.Utilities.SessionManager;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;
  HistoryAdapter hAdapter;
    SessionManager session;
    String id;
    ImageView select;
    RelativeLayout back;

    StringRequest stringRequest, stringRequest1;
    RequestQueue mRequestQueue, mRequestQueue1;
    private List<HistoryModel> list = new ArrayList<>();
    private List<InnerModel> list1 = new ArrayList<>();
    public static final String TAG = "STag";
    String dates, roundid,roundname;
    Dialog dialog;
    Button submit;
    EditText todate,fromdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Fabric.with(getActivity(), new Crashlytics());
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_history, container, false);

       back = v.findViewById(R.id.arrowback3);
       select = v.findViewById(R.id.dateselect);

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent i = new Intent(getActivity(),DashboardActivity.class);
               startActivity(i);
               getActivity().finish();

           }
       });
        recyclerView = v. findViewById(R.id.historyrecycle);

        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);


        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        hAdapter = new HistoryAdapter(getActivity(), list);
        recyclerView.setAdapter(hAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                HistoryModel model=list.get(position);
                dates = model.getDate();
                roundid = model.getId();
                roundname = model.getRoundname();
                Intent i = new Intent(getActivity(), DetailedActivity.class);
                i.putExtra("date", dates);
                i.putExtra("rid", roundid);
                i.putExtra("rname",roundname);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        session = new SessionManager(getActivity());

        HashMap<String, String> roles = session.getUserDetails();
        id = roles.get(SessionManager.KEY_ID);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
                dialog.setContentView(R.layout.dialogdate);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                submit = dialog.findViewById(R.id.submit);

                fromdate = dialog.findViewById(R.id.fromdate);
                todate = dialog.findViewById(R.id.todate);

                fromdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentDate = Calendar.getInstance();
                        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};

                        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                // TODO Auto-generated method stub
                                mYear[0] = selectedyear;
                                mMonth[0] = selectedmonth;
                                mDay[0] = selectedday;
                                fromdate.setText(new StringBuilder().append(mDay[0]).append("/").append(mMonth[0] + 1).append("/").append(mYear[0]).append("")); }
                        }, mYear[0], mMonth[0], mDay[0]);
                        mDatePicker.setTitle("Select Date");
                        mDatePicker.show();
//                        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());

                    }
                });

                todate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentDate = Calendar.getInstance();
                        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};

                        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                // TODO Auto-generated method stub
                                mYear[0] = selectedyear;
                                mMonth[0] = selectedmonth;
                                mDay[0] = selectedday;
                                todate.setText(new StringBuilder().append(mDay[0]).append("/").append(mMonth[0] + 1).append("/").append(mYear[0]).append("")); }

                        }, mYear[0], mMonth[0], mDay[0]);
                        mDatePicker.setTitle("Select Date");
                        mDatePicker.show();
//                        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());

                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    HistorySort(fromdate.getText().toString(), todate.getText().toString());
                    dialog.dismiss();

                    }
                });
            }

        });

        History();

        return  v;
    }

    public void History() {

        final ProgressDialog showMe = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();

        mRequestQueue = Volley.newRequestQueue(getActivity());

        stringRequest = new StringRequest(Request.Method.POST, Constant.HISTORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showMe.dismiss();
                        list.clear();
                       // list1.clear();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            String status = j.getString("status");
                            String msg = j.getString("msg");
                            if(status.equals("500")) {
                                session.logoutUser();
                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                startActivity(i);
                                getActivity().finish();
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                           else if (status.equals("200")) {
                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {
                                    for (int i = 0; i < applist.length(); i++) {

                                        HistoryModel model = new HistoryModel();
//                                        InnerModel model1 = new InnerModel();
                                        JSONObject getOne = applist.getJSONObject(i);

                                        model.setDate(getOne.getString("date"));
                                        model.setId(getOne.getString("round_id"));
                                        model.setRoundname(getOne.getString("round_name"));

//                                        JSONObject jsonObject = applist .getJSONObject(i);
//                                        JSONArray lists = jsonObject.getJSONArray("checkpoints");

//                                        if (lists != null && lists.length() > 0) {
//                                            for (int a= 0; a < lists.length(); a++) {
//                                                JSONObject getOnes = lists.getJSONObject(a);
//
//                                                model1.setCheckname(getOnes.getString("name"));
//                                                model1.setCheckloc(getOnes.getString("location"));
//                                                model1.setTime(getOnes.getString("time"));
//                                            }
//                                        }

                                        list.add(model);
                                      //  list1.add(model1);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        hAdapter = new HistoryAdapter(getActivity(), list);
                                        recyclerView.setAdapter(hAdapter);
                                    }
                                } else {
                                    showMe.dismiss();

                                }
                            } else {
                                showMe.dismiss();
                                recyclerView.setVisibility(View.GONE);
                                //  nodata.setVisibility(View.VISIBLE);
                                //  recyclerView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            showMe.dismiss();

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

                headers.put("guard_id", id);
                return headers;
            }

        };
        stringRequest.setTag(TAG);
        mRequestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void HistorySort(final String from, final String to) {

        final ProgressDialog showMe = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();

        mRequestQueue = Volley.newRequestQueue(getActivity());

        stringRequest = new StringRequest(Request.Method.POST, Constant.HISTORYSORT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showMe.dismiss();
                        list.clear();
                        // list1.clear();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            String status = j.getString("status");
                            String msg = j.getString("msg");
                            if(status.equals("500")) {
                                session.logoutUser();
                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                startActivity(i);
                                getActivity().finish();
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                            else if (status.equals("200")) {
                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {
                                    for (int i = 0; i < applist.length(); i++) {

                                        HistoryModel model = new HistoryModel();
//                                        InnerModel model1 = new InnerModel();
                                        JSONObject getOne = applist.getJSONObject(i);

                                        model.setDate(getOne.getString("date"));
                                        model.setId(getOne.getString("round_id"));
                                        model.setRoundname(getOne.getString("round_name"));

                                        list.add(model);
                                        //  list1.add(model1);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        hAdapter = new HistoryAdapter(getActivity(), list);
                                        recyclerView.setAdapter(hAdapter);
                                    }
                                } else {
                                    showMe.dismiss();

                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                                showMe.dismiss();

                                //  nodata.setVisibility(View.VISIBLE);
                                //  recyclerView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            showMe.dismiss();

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

                headers.put("from", from);
                headers.put("to", to);
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
        final Dialog dialogs = new Dialog(getActivity());
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                History();

            }
        });
        dialogs.show();
    }
}
