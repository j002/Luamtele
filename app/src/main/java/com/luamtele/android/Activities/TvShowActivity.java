package com.luamtele.android.Activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luamtele.android.DB.LuamteleDb;
import com.luamtele.android.utils.Status;
import com.luamtele.android.R;
import com.luamtele.android.adapter.TvShowViewAdapter;
import com.luamtele.android.api.LuamteleApi;
import com.luamtele.android.api.RealmSerializer;
import com.luamtele.android.model.Comment;
import com.luamtele.android.utils.PreferencesManager;
import com.poliveira.apps.parallaxlistview.ParallaxListView;

import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmObject;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


public class TvShowActivity extends ActionBarActivity {

    TvShowViewAdapter customAdapter;
    List<String> list = new ArrayList<>();
    Dialog rankDialog;
    Dialog loginDialog;
    LuamteleDb db;
    String statUser;
    PreferencesManager preferencesManager=new PreferencesManager();
    int idshow;
    Status status=new Status();
    int      idUser;
    Menu menu;
    RelativeLayout mContainer;
    static Context context;
    EditText editText;
   public int comDone;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show);
    //    final ListView listView = (ListView) findViewById(R.id.tvShowList);
        db = new LuamteleDb(this);
         final int idUser =preferencesManager.getValueInt(TvShowActivity.this, "idUser");

        statUser=preferencesManager.getValue(TvShowActivity.this, "stat");
        Bundle extras = this.getIntent().getExtras();
        if (savedInstanceState == null) {
            if (extras == null) {
                idshow = extras.getInt("show");
            } else {
                idshow = extras.getInt("show");
            }
        } else {
            idshow = extras.getInt("show");
        }

        customAdapter = new TvShowViewAdapter(this, new ArrayList<RealmObject>(),idshow,this);
     //   listView.setAdapter(customAdapter);
        context = this;

        editText = ((EditText) findViewById(R.id.commentediter));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0080c0")));
        getSupportActionBar().setTitle(db.getTvShowWithId(idshow).getName());

        final ImageView button = (ImageView) findViewById(R.id.addbutton);
        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Status status = new Status();
                if (!statUser.equals("yes")) {
                    loginDialog = new Dialog(TvShowActivity.this, R.style.FullHeightDialog);
                    loginDialog.setContentView(R.layout.connexion_dialog);
                    loginDialog.setCancelable(true);
                    ImageView loginDialogButton = (ImageView) loginDialog.findViewById(R.id.loginDialog);
                    loginDialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, Login.class);
                            startActivity(intent);
                        }
                    });
                    ImageView returnb = (ImageView) loginDialog.findViewById(R.id.backDialog);
                    returnb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loginDialog.dismiss();
                        }
                    });
                    loginDialog.show();
                } else {

                    String mytexte = editText.getText().toString();
                    if (!editText.getText().toString().equals("")) {
                        Log.d("drake", "in !");
                        // customAdapter.add(editText.getText().toString());
                        editText.setText("");
                        editText.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        Comment comment = new Comment();

                        Log.d("USERR", "logine " + idUser);
                        Gson gson1 = new GsonBuilder()
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
                                .registerTypeAdapter(Comment.class, new RealmSerializer())

                                .create();

                        GsonConverter converter = new GsonConverter(gson1);

                        LuamteleApi luamteleApi = new RestAdapter.Builder()
                                .setEndpoint(LuamteleApi.TvApiUrl)
                                .setLogLevel(RestAdapter.LogLevel.FULL)
                                .setRequestInterceptor(new RequestInterceptor() {
                                    @Override
                                    public void intercept(RequestInterceptor.RequestFacade request) {
                                        request.addHeader("Accept", "application/json;versions=1");
                                        request.addHeader("Authorization", getToken());
                                    }
                                })
                                .setConverter(converter)
                                .build()
                                .create(LuamteleApi.class);

                        comment.setIdTvShow(idshow);
                        comment.setIdUser(idUser);
                        comment.setContent(mytexte);
                        Log.d("log10", "success : " + comment.getContent());
                        Calendar d = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strDate = sdf.format(d.getTime());
                        comment.setDateTime(strDate);
                        luamteleApi.addcomment(comment, new Callback<Comment>() {
                            @Override
                            public void success(Comment comment, Response response) {
                                Log.d("log10", "success : ");
                                comDone = 1;
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Log.d("log10", "erreur " + retrofitError);
                            }
                        });
                        Log.d("comment", "" + comDone);

                        if (comDone == 1) {
                            customAdapter.add((RealmObject) comment);
                            customAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
        mContainer = (RelativeLayout) findViewById(R.id.container);
        mContainer.post(new Runnable() {
            @Override
            public void run() {
                db.getInitializeApi(new Comment());
                getComments();
                parrallaxCustomed();
            }
        });


            }


    public void parrallaxCustomed (){
            mContainer.removeAllViews();
        Log.d("iff",""+getLayoutInflater());
            final View v = getLayoutInflater().inflate(R.layout.include_listview, mContainer, true);
            final ParallaxListView mListView = (ParallaxListView) v.findViewById(R.id.view);
            mListView.setAdapter(customAdapter);
            mListView.setParallaxView(getLayoutInflater().inflate(R.layout.view_header_parallax, mListView, false));
            final int size = Math.round(48 * getResources().getDisplayMetrics().density);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.setMargins(0, 0, Math.round(16 * getResources().getDisplayMetrics().density), 0);
            mListView.setDivider(null);
            mListView.setOnTouchListener(new View.OnTouchListener() {
                                             float height;

                                             @Override
                                             public boolean onTouch(View v, MotionEvent event) {
                                                 int action = event.getAction();
                                                 float height = event.getY();


                                                 if (action == MotionEvent.ACTION_DOWN) {
                                                     this.height = height;
                                                 } else if (action == MotionEvent.ACTION_UP) {
                                                     if (this.height < height) {
                                                         findViewById(R.id.commentediter).setVisibility(View.INVISIBLE);
                                                         findViewById(R.id.addbutton).setVisibility(View.INVISIBLE);
                                                         ((EditText) findViewById(R.id.commentediter)).setText("");
                                                         InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                         imm.hideSoftInputFromWindow(findViewById(R.id.commentediter).getWindowToken(), 0);
                                                     } else if (this.height > height) {
                                                         InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                         imm.hideSoftInputFromWindow(findViewById(R.id.commentediter).getWindowToken(), 0);
                                                         findViewById(R.id.commentediter).setVisibility(View.VISIBLE);
                                                         findViewById(R.id.addbutton).setVisibility(View.VISIBLE);


                                                     }
                                                 }
                                                 return false;
                                             }
                                         }

            );
       // mListView.getChildAt(1).setPadding(0,0,0,10);
           }

    private void getComments() {
        final List<RealmObject> items = new ArrayList<>();
        db.getLuamteleApi().getComments(new Callback<List<Comment>>() {
            @Override
            public void success(List<Comment> comments, Response response) {
                items.addAll(comments);
                if (db.getComment().size() <= items.size()) db.addObjects(items);
                Log.d("infff",""+db.getComment());
                customAdapter.add(db.getTvShowWithId(idshow));
                customAdapter.addAll(db.getCommentWithTvShowId(idshow));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(context, "Connexion Echoué",
                        Toast.LENGTH_SHORT).show();
                customAdapter.add(db.getTvShowWithId(idshow));
                customAdapter.addAll(db.getCommentWithTvShowId(idshow));
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tv_show, menu);
       this.menu=menu;
        if(!statUser.equals("yes")){
            menu.findItem(R.id.menu_login).setTitle("se connecter");
        }
        if(statUser.equals("yes")){
            menu.findItem(R.id.menu_login).setTitle("se deconnecter");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            //do something
            Toast.makeText(this, "Settings menu was clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.menu_login) {



                if(item.getTitle().equals("se connecter")) {
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }else{
                    preferencesManager.setValue(TvShowActivity.this, "stat","no");
                    preferencesManager.setValueInt(TvShowActivity.this, "idUser",0);
                }


            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    public String getToken() {
        String token = PreferencesManager.getValue(context, "token");
        return token;
    }

}
