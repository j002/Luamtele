package com.luamtele.android.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.luamtele.android.Activities.TvShowActivity;
import com.luamtele.android.DB.LuamteleDb;
import com.luamtele.android.R;
import com.luamtele.android.adapter.TvShowListAdapter;
import com.luamtele.android.model.TvShow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeuxiemePartieFragment extends Fragment {


    public DeuxiemePartieFragment() {

    }

    TvShowListAdapter customAdapter;
    ListView listview;
    Calendar c;
    LuamteleDb db;
    List<RealmObject> text = new ArrayList<>();
    boolean first = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deuxieme_partie, container, false);
        customAdapter=new TvShowListAdapter(this.getActivity(),new ArrayList<RealmObject>());
        listview = (ListView)rootView.findViewById(R.id.listView_tv_secondPart);
        db = new LuamteleDb(getActivity());
        Calendar c = Calendar.getInstance();
        listview.setAdapter(customAdapter);
        if(first ==false && !db.getTvShow().isEmpty()) text.addAll((db.getTvShowWithTime(db.getDay(c.getTime().getDay()), 23)));
        first = true;
        customAdapter.addAll(text);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), TvShowActivity.class);
                intent.putExtra("show", (((TvShow)text.get(position)).getId()));
                startActivity(intent);
            }
        });
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
