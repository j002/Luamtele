package com.luamtele.android.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.luamtele.android.utils.Status;
import com.luamtele.android.R;
import com.luamtele.android.api.LuamteleApi;
import com.luamtele.android.api.RealmSerializer;
import com.luamtele.android.model.User;
import com.luamtele.android.utils.PreferencesManager;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmObject;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


public class Login extends ActionBarActivity {
    Status status = new Status();
    PreferencesManager preferencesManager = new PreferencesManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText email = (EditText) findViewById(R.id.usertexte);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0080c0")));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    public String md5(String s) {
        try {

            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();


            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int djiby;
    public static String statu;
    public static String stat;

    public boolean isUserLoggedIn() {
        if (preferencesManager.getValue(Login.this, "stat").equals("yes"))
            return true;
        return false;
    }

    public String getToken() {
        return preferencesManager.getValue(Login.this, "token");
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void OnButtonClick(View v) throws JSONException {
        if (v.getId() == R.id.btnlogin) {
            User user = new User();
            EditText email = (EditText) findViewById(R.id.usertexte);
            EditText password = (EditText) findViewById(R.id.password);
            String recupemail = email.getText().toString();
            String recuppassword = md5(password.getText().toString());
            if (recupemail.isEmpty() || recuppassword.isEmpty()) {
                Toast.makeText(Login.this, "Remplissez tous les elements",
                        Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new GsonBuilder()
                        .setExclusionStrategies(new ExclusionStrategy() {
                            @Override
                            public boolean shouldSkipField(FieldAttributes f) {
                                return f.getDeclaringClass().equals(RealmObject.class);
                            }

                            @Override
                            public boolean shouldSkipClass(Class<?> clazz) {
                                return false;
                            }
                        })
                        .registerTypeAdapter(user.getClass(), new RealmSerializer())
                        .create();

                GsonConverter converter = new GsonConverter(gson);

                LuamteleApi luamteleApi = new RestAdapter.Builder()
                        .setEndpoint(LuamteleApi.TvApiUrl)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setConverter(converter)
                        .build()
                        .create(LuamteleApi.class);
                HashMap<String, String> jsonObject = new HashMap<>();
                jsonObject.put("email", recupemail);
                jsonObject.put("password", recuppassword);
                luamteleApi.login(jsonObject, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.d("login", "json: " + jsonElement.toString());
                        JsonObject object = jsonElement.getAsJsonObject();
                        if (object.get("status").getAsString().contains("success")) {
                            //  djiby = (Integer.parseInt(jsonElement.toString().substring(jsonElement.toString().length() - 4, jsonElement.toString().length() - 2)));
                            String token = object.get("token").getAsString();
                            djiby = object.get("idUser").getAsInt();
                            Log.d("login", "jsonnnnnnnnn: " + djiby);
                            Log.d("login", "token: " + token);
                            preferencesManager.setValue(Login.this, "stat", "yes");
                            preferencesManager.setValue(Login.this, "token", token);
                            preferencesManager.setValueInt(Login.this, "idUser", djiby);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Password ou Email n'est pas correct",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.d("log10", "failure: " + retrofitError);
                    }
                });
            }
        }
        if (v.getId() == R.id.btnsignup) {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
