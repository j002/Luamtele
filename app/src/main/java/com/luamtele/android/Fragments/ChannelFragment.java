package com.luamtele.android.Fragments;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Menu;
import android.view.MenuInflater;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.luamtele.android.Activities.ChannelViewActivity;
import com.luamtele.android.DB.LuamteleDb;
import com.luamtele.android.R;
import com.luamtele.android.adapter.ChannelListAdapter;
import com.luamtele.android.model.Channel;

import java.util.ArrayList;
import java.util.List;
import io.realm.RealmObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChannelFragment extends Fragment {

    public ChannelFragment() {
    }
    ChannelListAdapter customAdapter;
    Dialog loginDialog;
    Context context;
    List<RealmObject> text = new ArrayList<>();
    boolean first = false;
    List<String> list = new ArrayList<>();
    int posi;
    LuamteleDb db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_channel, container, false);
        context = this.getActivity();
        final ListView listView = (ListView) rootView.findViewById(R.id.channellistview);
        db = new LuamteleDb(this.getActivity());
        customAdapter = new ChannelListAdapter(this.getActivity(), new ArrayList<RealmObject>(),this.getActivity());
        listView.setAdapter(customAdapter);
        db.getInitializeApi(new Channel());
        getAllItemObject();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChannelViewActivity.class);
                intent.putExtra("cha", (((Channel) text.get(position)).getId()));
                startActivity(intent);


            }
        });
        setHasOptionsMenu(true);

        return rootView;
    }

    private void getAllItemObject() {
        final List<RealmObject> items = new ArrayList<>();
        db.getLuamteleApi().getChannels(new Callback<List<Channel>>() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void success(List<Channel> channels, Response response) {
                items.addAll(channels);
                db.addObjects(items);
                if (first == false) text.addAll(db.getChanel());
                first = true;
                customAdapter.addAll(text);
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void failure(RetrofitError retrofitError) {
              Toast.makeText(context, "Connexion Echoué",
                        Toast.LENGTH_SHORT).show();
                if (first == false) text.addAll(db.getChanel());
                first = true;
                customAdapter.addAll(text);

            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       // MenuItem itemBlog = menu.add(Menu.NONE, // Group ID
               // R.id.blog_item, // Item ID
              //  1, // Order
               // R.string.blog_item); // Title
       // itemBlog.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); // ShowAsAction
      //  itemBlog.setIcon(R.drawable.ic_action_blog); // Icon
        // add your item before calling the super method
        super.onCreateOptionsMenu(menu,inflater);
    }


}
