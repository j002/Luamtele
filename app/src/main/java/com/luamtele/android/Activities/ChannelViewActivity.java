package com.luamtele.android.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.luamtele.android.DB.LuamteleDb;
import com.luamtele.android.utils.Status;
import com.luamtele.android.R;
import com.luamtele.android.adapter.ChannelViewAdapter;
import com.poliveira.apps.parallaxlistview.ParallaxListView;
import com.luamtele.android.utils.PreferencesManager;
import java.util.ArrayList;

import io.realm.RealmObject;

public class ChannelViewActivity extends ActionBarActivity {
    ChannelViewAdapter customAdapter;
    int idcha;
    LuamteleDb db;
    RelativeLayout mContainer;
    Context context;
    Status status=new Status();
    Menu menu;
    String statUser;
    PreferencesManager preferencesManager=new PreferencesManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channelviewactivity);
        db = new LuamteleDb(this);
        context = this;
        Bundle extras = this.getIntent().getExtras();
        if (savedInstanceState == null) {
            if (extras == null) {
                idcha = extras.getInt("cha");
            } else {
                idcha = extras.getInt("cha");
            }
        } else {
            idcha = extras.getInt("cha");
        }
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        customAdapter = new ChannelViewAdapter(this, new ArrayList<RealmObject>(),idcha);
        customAdapter.add(db.getChannelWithId(idcha));
        customAdapter.addAll(db.getTvShowWithChannelId(idcha));
        statUser=preferencesManager.getValue(context, "stat");
        int idUser =preferencesManager.getValueInt(context, "idUser");
        mContainer = (RelativeLayout) findViewById(R.id.container);
        mContainer.post(new Runnable() {
            @Override
            public void run() {
                parrallaxCustomed();
            }
        });
    }
    public void parrallaxCustomed (){
        mContainer.removeAllViews();
        final View v = getLayoutInflater().inflate(R.layout.include_listview, mContainer, true);
        final ParallaxListView mListView = (ParallaxListView) v.findViewById(R.id.view);
        mListView.setAdapter(customAdapter);
        mListView.setParallaxView(getLayoutInflater().inflate(R.layout.view_header_parallax, mListView, false));
        final int size = Math.round(48 * getResources().getDisplayMetrics().density);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.setMargins(0, 0, Math.round(16 * getResources().getDisplayMetrics().density), 0);
        mListView.setDivider(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_channel_view, menu);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings menu was clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        else if (id == R.id.menu_logout) {

            if(item.getTitle().equals("se connecter")) {
                Intent intent = new Intent(context, Login.class);
                context.startActivity(intent);
            } else {
                preferencesManager.setValue(context, "stat","no");
               preferencesManager.setValueInt(context, "idUser",0);

            }



            return true;
        }

        return super.onOptionsItemSelected(item);

    }

}
