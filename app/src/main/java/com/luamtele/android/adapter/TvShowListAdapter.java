package com.luamtele.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.luamtele.android.R;
import com.luamtele.android.model.TvShow;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by masta on 10/1/15.
 */
public class TvShowListAdapter extends ArrayAdapter<RealmObject> {

    private Context context;
    private LayoutInflater layoutinflater;
    private List<RealmObject> listStorage;

    public TvShowListAdapter(Context context, List<RealmObject> customizedListView) {
        super(context,0,customizedListView);
        this.context = context;
        layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        if (convertView == null) {

            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.tv_show_list_item, parent, false);
            listViewHolder.imgChaineTv = (ImageView) convertView.findViewById(R.id.imgChaineTv);
            listViewHolder.imgTvShow = (ImageView) convertView.findViewById(R.id.imgTvShow);
            listViewHolder.title = (TextView)convertView.findViewById(R.id.title);
            listViewHolder.heure = (TextView)convertView.findViewById(R.id.datetime);
            listViewHolder.ratingBarOnList = (RatingBar)convertView.findViewById(R.id.ratingBarOnList);
            listViewHolder.type = (TextView)convertView.findViewById(R.id.type);
            convertView.setTag(listViewHolder);
        }
        else {

            listViewHolder = (ViewHolder) convertView.getTag();
        }

        listViewHolder.title.setText(((TvShow)(listStorage.get(position))).getName());
        listViewHolder.heure.setText(((TvShow)(listStorage.get(position))).getTime());
        listViewHolder.type.setText(((TvShow)(listStorage.get(position))).getGenre());
        listViewHolder.imgChaineTv.setImageResource(R.drawable.sentv);
        return convertView;
    }
    static class ViewHolder{
        ImageView imgChaineTv;
        ImageView imgTvShow;
        TextView title;
        TextView heure;
        RatingBar ratingBarOnList;
        TextView type;
    }
}
