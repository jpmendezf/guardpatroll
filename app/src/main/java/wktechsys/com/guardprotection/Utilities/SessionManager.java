package wktechsys.com.guardprotection.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import wktechsys.com.guardprotection.Activities.LoginActivity;


/**
 * Created by wktechsys on 4/17/2017.
 */
public class SessionManager {

    Context context;
    public static final String KEY_ID = "id";
    public static final String KEY_NUMBER="number";
    public static final String KEY_PASSWORD="pass";
    public static final String KEY_NAME="name";
    public static final String KEY_EMAIL="email";
    public static final String KEY_AGENCY="agency";
    public static final String KEY_GUARDID="guardid";
    private static final String Pref_Name= "Loginpref";
    private static final String IS_LOGIN = "IsLoggedIn";
   public static final String IS_Lock ="Islocked";

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    Context _context;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public SessionManager(Context context1) {
        this.context=context1;
        pref = context.getSharedPreferences(Pref_Name, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession( String number, String password, String id, String name, String email, String agency, String guardid){

        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_NUMBER, number);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_AGENCY, agency);
        editor.putString(KEY_GUARDID, guardid);

        editor.commit();
    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, pref.getString(KEY_ID, ""));
        user.put(KEY_NUMBER, pref.getString(KEY_NUMBER, ""));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, ""));
        user.put(KEY_NAME, pref.getString(KEY_NAME, ""));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        user.put(KEY_AGENCY, pref.getString(KEY_AGENCY, ""));
        user.put(KEY_GUARDID, pref.getString(KEY_GUARDID, ""));
        user.put(IS_Lock,pref.getString(IS_Lock,""));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(context,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
