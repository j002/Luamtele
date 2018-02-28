package com.luamtele.android.Activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luamtele.android.R;
import com.luamtele.android.api.LuamteleApi;
import com.luamtele.android.api.RealmSerializer;
import com.luamtele.android.model.User;
import com.luamtele.android.utils.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


public class Signup extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button btn = (Button) findViewById(R.id.idsingup);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0080c0")));
        btn.setOnClickListener(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View view) {
                                       User user1 = new User();


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
                                               .registerTypeAdapter(user1.getClass(), new RealmSerializer())
                                               .create();

                                       GsonConverter converter = new GsonConverter(gson);

                                       LuamteleApi luamteleApi = new RestAdapter.Builder()
                                               .setEndpoint(LuamteleApi.TvApiUrl)
                                               .setLogLevel(RestAdapter.LogLevel.FULL)
                                               .setConverter(converter)
                                               .build()
                                               .create(LuamteleApi.class);
                                       //
                                       EditText name = (EditText) findViewById(R.id.tfname);
                                       EditText email1 = (EditText) findViewById(R.id.tfemail);
                                       EditText pass1 = (EditText) findViewById(R.id.tfpassword);
                                       EditText pass2 = (EditText) findViewById(R.id.tfconfpass);
                                       EditText pseudo = (EditText) findViewById(R.id.tfpseudo);
                                       EditText surname = (EditText) findViewById(R.id.tfsurname);
                                       //Recuperation des données dans des variable
                                       String recupname = name.getText().toString();
                                       String recuppseudo = pseudo.getText().toString();
                                       String recupsurname = surname.getText().toString();
                                       String recupemail = email1.getText().toString();
                                       String recuppass1 = pass1.getText().toString();
                                       String recuppass2 = pass2.getText().toString();
                                       //ajouter les données dans un utilisateur
                                       user1.setName(recupname);
                                       user1.setPseudo(recuppseudo);
                                       user1.setEmail(recupemail);
                                       user1.setSurname(recupsurname);
                                       user1.setPassword(md5(recuppass1));
                                       if (recupname.isEmpty() || recupemail.isEmpty() || recuppass1.isEmpty() || recuppass2.isEmpty() || recupsurname.isEmpty() || recuppseudo.isEmpty()) {

                                       }
                                       if (verifEmail(recupemail)) {

                                                   if (!recuppass1.equals(recuppass2)) {
                                                       Toast.makeText(Signup.this, "Mot De Passe Ne sont pas Identiques",
                                                               Toast.LENGTH_SHORT).show();
                                                   } else if (pseudo.getText().toString().length() <= 2) {
                                                       pseudo.setError("La taille du pseudo doit etre superieur a 2");
                                                       pseudo.setHintTextColor(Color.parseColor("#110F10"));
                                                   } else {
                                                       Log.d("retrofit", Util.toGSON(user1));
                                                       luamteleApi.adduser(user1, new Callback<User>() {
                                                           @Override
                                                           public void success(User user1, Response response) {
                                                               Log.d("retrofite", "success : " + Util.toGSON(user1.getEmail()));
                                                               finish();
                                                           }
                                                           @Override
                                                           public void failure(RetrofitError retrofitError) {
                                                               Log.d("retrofite", "failure : " + Util.toGSON(retrofitError));
                                                           }
                                                       });
                                                   }
                                               }
                                           else{
                                                   Toast.makeText(Signup.this, "Adresse Email Valide", Toast.LENGTH_SHORT).show();
                                               }
                                   }
                               }
        );
    }

 public static boolean verifEmail(String email) {
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();
    }
    public String md5(String s) {
        try {

            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_singup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
