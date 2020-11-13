package com.devatrans.mysmet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public  class PrefConfig {

    private static SharedPreferences sharedPreferences;
    private static Context context;

    public PrefConfig(Context context) {

        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);

    }

    public void writeId(String id) {
        Log.d("WriteId" , String.valueOf(id));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putInt(context.getString(R.string.pref_user_id), id);
        //editor.apply();
    }

    public Integer readId(){
        return sharedPreferences.getInt(context.getString(R.string.pref_user_id), 0);
    }

    public void writeFCMToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_fcm_token), token);
        editor.apply();
    }

    public String readFCMToken() {
        return sharedPreferences.getString(context.getString(R.string.pref_fcm_token), "com.devatrans.mydriver.User");
    }

    public void displayToast(String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public void displayNetworkStatusDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("Моля свържете се").setTitle("Няма интернет връзка");
        builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}
