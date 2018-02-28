package com.luamtele.android.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.luamtele.android.DB.LuamteleDb;
import com.luamtele.android.R;
import com.luamtele.android.adapter.TabsPagerAdapter;
import com.luamtele.android.model.Comment;
import com.luamtele.android.model.Follow;
import com.luamtele.android.model.Like;
import com.luamtele.android.model.Rate;
import com.luamtele.android.model.TvShow;
import com.luamtele.android.model.User;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    LuamteleDb db;
    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setAdapter(new TabsPagerAdapter(getChildFragmentManager()));
        db = new LuamteleDb(this.getActivity());
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                db.getInitializeApi(new Comment());
                getUsers();
            }
        });
        return rootView;
    }
    private void getUsers() {
        final List<RealmObject> items = new ArrayList<>();
        db.getLuamteleApi().getUsers(new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                items.addAll(users);
                if (db.getUser().size() <= items.size()) db.addObjects(items);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.now_main_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            Toast.makeText(getActivity().getApplicationContext(), "refresh", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
