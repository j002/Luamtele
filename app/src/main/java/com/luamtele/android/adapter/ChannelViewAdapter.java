package com.luamtele.android.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.luamtele.android.Activities.TvShowActivity;
import com.luamtele.android.DB.LuamteleDb;
import com.luamtele.android.Activities.Login;
import com.luamtele.android.utils.Status;
import com.luamtele.android.R;
import com.luamtele.android.api.LuamteleApi;
import com.luamtele.android.api.RealmSerializer;
import com.luamtele.android.model.Channel;
import com.luamtele.android.model.Follow;
import com.luamtele.android.model.TvShow;

import com.luamtele.android.utils.PreferencesManager;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by number on 20/10/15.
 */
public class ChannelViewAdapter extends ArrayAdapter<RealmObject> {


    private Context context;
    private LayoutInflater layoutinflater;
    private List<RealmObject> listStorage;
    private LuamteleDb db;
    private int idChannel;

    private int posi = 1;

    String statUser;
    PreferencesManager preferencesManager=new PreferencesManager();

    public ChannelViewAdapter(Context context, List<RealmObject> customizedListView,int idChannel) {
        super(context, 0, customizedListView);
        this.context = context;
        this.idChannel=idChannel;
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
        db = new LuamteleDb(context);
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? 1 : 0;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
         final int idUser1 =preferencesManager.getValueInt(context, "idUser");
         statUser=preferencesManager.getValue(context, "stat");
        if (convertView == null) {
            listViewHolder = new ViewHolder();
           if (position == 0) {
                convertView = layoutinflater.inflate(R.layout.channel_view_inflater, parent, false);
                listViewHolder.logo = (ImageView) convertView.findViewById(R.id.logo);
                listViewHolder.like = (ImageView) convertView.findViewById(R.id.follow2);
                convertView.setTag(listViewHolder);
                convertView.findViewById(R.id.follow2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Status status = new Status();
                        final Dialog loginDialog;
                        if (!statUser.equals("yes")) {
                            loginDialog = new Dialog(context, R.style.FullHeightDialog);
                            loginDialog.setContentView(R.layout.connexion_dialog);
                            loginDialog.setCancelable(true);
                            ImageView loginDialogButton = (ImageView) loginDialog.findViewById(R.id.loginDialog);
                            loginDialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, Login.class);
                                    context.startActivity(intent);
                                    loginDialog.dismiss();
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
                            RealmResults fol = null;



                            fol = db.getFollowWithChannelAndUserId(idChannel,idUser1);
                            Follow follow = new Follow();
                            if (fol == null) {
                                ((ImageView) v.findViewById(R.id.follow2)).setImageResource(R.drawable.suivi);

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
                                        .registerTypeAdapter(Follow.class, new RealmSerializer())
                                        .create();

                                GsonConverter converter = new GsonConverter(gson);

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

                                follow.setIdChannel(idChannel);
                                follow.setIdUser(idUser1);
                                Calendar d = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String strDate = sdf.format(d.getTime());
                                follow.setDateTime(strDate);

                                luamteleApi.addfollow(follow, new Callback<Follow>() {

                                    @Override
                                    public void success(Follow follow, Response response) {
                                        Log.d("log10", "success : ");

                                    }

                                    @Override
                                    public void failure(RetrofitError retrofitError) {

                                        Log.d("log10", "erreur " + retrofitError);
                                    }
                                });
                                db.addFollow(follow);
                            } else {
                                ((ImageView) v.findViewById(R.id.follow2)).setImageResource(R.drawable.suivre);
                                db.deleteObjects(fol);
                                Follow unfollow = new Follow();
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
                                        .registerTypeAdapter(Follow.class, new RealmSerializer())
                                        .create();

                                GsonConverter converter = new GsonConverter(gson);

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
                                unfollow.setIdUser(idUser1);
                                unfollow.setIdChannel(idChannel);

                                luamteleApi.deletefollow(unfollow.getIdUser(), unfollow.getIdChannel(), new Callback<JsonElement>() {
                                    @Override
                                    public void success(JsonElement jsonElement, Response response) {
                                        Log.d("log100000", "unfollow matess ");
                                    }

                                    @Override
                                    public void failure(RetrofitError retrofitError) {
                                        Log.d("log100000", "erreur " + retrofitError);
                                    }
                                });


                            }
                        }
                    }
                });
                convertView.setOnClickListener(null);

            } else {

                convertView = layoutinflater.inflate(R.layout.channel_view_inflater_list, parent, false);
                listViewHolder.text = (TextView) convertView.findViewById(R.id.tv);
                listViewHolder.text2 = (TextView) convertView.findViewById(R.id.tv2);
                convertView.findViewById(R.id.tv).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.tv2).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.tvShowImage).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.tvShowImage2).setVisibility(View.INVISIBLE);
                convertView.setTag(listViewHolder);


           }

        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
          listViewHolder.logo.setImageResource(R.drawable.tfm);
            LuamteleDb ap = new LuamteleDb(context);
            Picasso.with(context).load(""+LuamteleApi.TvApiUrl+"PO.png").into(listViewHolder.logo);
            listViewHolder.like.setImageResource(R.drawable.suivre);
            if (db.getFollowWithChannelAndUserId(((Channel) listStorage.get(position)).getId(), Status.getUserId()) == null)
                listViewHolder.like.setImageResource(R.drawable.suivre);
            if (db.getFollowWithChannelAndUserId(((Channel) listStorage.get(position)).getId(), Status.getUserId()) != null)
                listViewHolder.like.setImageResource(R.drawable.suivi);
        }
        if (position != 0) {
posi = position;
    if ((position *2) -1  < listStorage.size()) {
        convertView.findViewById(R.id.tv).setVisibility(View.VISIBLE);
        convertView.findViewById(R.id.tvShowImage).setVisibility(View.VISIBLE);
        listViewHolder.text.setText(((TvShow) listStorage.get((position *2) -1)).getName());
        convertView.findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TvShowActivity.class);
                intent.putExtra("show", (db.getTvShowWithId(((TvShow)listStorage.get((position *2) -1)).getId())).getId());
                context.startActivity(intent);
            }
        });
        convertView.findViewById(R.id.tvShowImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TvShowActivity.class);
                intent.putExtra("show", (db.getTvShowWithId(((TvShow)listStorage.get((position *2) -1)).getId())).getId());
                context.startActivity(intent);
            }
        });
    }
    if (position * 2 < listStorage.size()) {
        convertView.findViewById(R.id.tv2).setVisibility(View.VISIBLE);
        convertView.findViewById(R.id.tvShowImage2).setVisibility(View.VISIBLE);
        listViewHolder.text2.setText(((TvShow) listStorage.get((position *2))).getName());
        convertView.findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TvShowActivity.class);
                intent.putExtra("show", (db.getTvShowWithId(((TvShow) listStorage.get((position *2) )).getId())).getId());
                context.startActivity(intent);
            }
        });
        convertView.findViewById(R.id.tvShowImage2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TvShowActivity.class);
                intent.putExtra("show", (db.getTvShowWithId(((TvShow)listStorage.get((position *2))).getId())).getId());
                context.startActivity(intent);
            }
        });


}

            convertView.setOnClickListener(null);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView banner;
        ImageView logo;
        ImageView like;
        TextView text;
        TextView text2;
    }

    public String getToken() {
        String token = PreferencesManager.getValue(context, "token");
        return token;
    }
}
