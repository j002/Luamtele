package com.luamtele.android.Fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.luamtele.android.DB.LuamteleDb;
import com.luamtele.android.R;
import com.luamtele.android.Activities.TvShowActivity;
import com.luamtele.android.adapter.TvShowListAdapter;
import com.luamtele.android.model.TvShow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NowFragment extends Fragment implements AdapterView.OnItemClickListener {


    public NowFragment() {
        // Required empty public constructor
    }
    TvShowListAdapter customAdapter;
    ListView listview;
    Calendar c;
    LuamteleDb db;
    Context context;
    List<RealmObject> text = new ArrayList<>();
    boolean first = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now, container, false);
        context = this.getActivity();
        customAdapter=new TvShowListAdapter(this.getActivity(),new ArrayList<RealmObject>());
        listview = (ListView)rootView.findViewById(R.id.listView_tv_showNow);
        db = new LuamteleDb(getActivity());
        c = Calendar.getInstance();
        listview.setAdapter(customAdapter);
        db.getInitializeApi(new TvShow());
        getTvShows();
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
    private void getTvShows() {
        final List<RealmObject> items = new ArrayList<>();
        db.getLuamteleApi().getTvShows(new Callback<List<TvShow>>() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void success(List<TvShow> TvShows, Response response) {
                items.addAll(TvShows);
                if (db.getTvShow().size() <= items.size()) db.addObjects(items);
                if(first ==false && !db.getTvShow().isEmpty()) text.addAll((db.getTvShowWithTime(db.getDay(c.getTime().getDay()), c.getTime().getHours())));
                first = true;
                customAdapter.addAll(text);
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(context, "Connexion Echoué",
                        Toast.LENGTH_SHORT).show();
                if(first ==false && !db.getTvShow().isEmpty() ) text.addAll((db.getTvShowWithTime(db.getDay(c.getTime().getDay()), c.getTime().getHours())));
                first = true;
                customAdapter.addAll(text);
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    /*
    * com.google.gson.JsonSyntaxException: com
    * .google.gson.stream.MalformedJsonException: Unterminated object at line 1 column 27*/
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  realm.close();
    }
}
