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
import com.luamtele.android.DB.LuamteleDb;
import com.luamtele.android.Activities.Login;
import com.luamtele.android.utils.Status;
import com.luamtele.android.R;
import com.luamtele.android.api.LuamteleApi;
import com.luamtele.android.api.RealmSerializer;
import com.luamtele.android.model.Channel;
import com.luamtele.android.model.Follow;
import com.luamtele.android.utils.PreferencesManager;

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
public class ChannelListAdapter extends ArrayAdapter<RealmObject> {

    private Context context, contextActivity;
    private LayoutInflater layoutinflater;
    private List<RealmObject> listStorage;
    private LuamteleDb db;
PreferencesManager preferencesManager=new PreferencesManager();
    //  int idUser;
    public ChannelListAdapter(Context context, List<RealmObject> customizedListView, Context acontext) {
        super(context, 0, customizedListView);
        this.context = context;
        contextActivity = acontext;
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
        db = new LuamteleDb(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder listViewHolder;
        final View v;
        v = convertView;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.channelinflater, parent, false);
            listViewHolder.logo = (ImageView) convertView.findViewById(R.id.logoList);
            listViewHolder.follow = (ImageView) convertView.findViewById(R.id.follow);
            listViewHolder.name = (TextView) convertView.findViewById(R.id.channelnameList);
            convertView.setTag(listViewHolder);
            convertView.findViewById(R.id.follow).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Status status = new Status();
                     int idUser =preferencesManager.getValueInt(contextActivity, "idUser");

                    String statUsers=preferencesManager.getValue(contextActivity, "stat");
                    final Dialog loginDialog;
                    if (!statUsers.equals("yes")) {
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


                                Log.d("USERR", "logine " + idUser);
                                fol = db.getFollowWithChannelAndUserId(((Channel) listStorage.get(position)).getId(), idUser);
                                Follow follow = new Follow();
                                if (fol == null) {
                                    ((ImageView) v.findViewById(R.id.follow)).setImageResource(R.drawable.suivi);

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

                                    follow.setIdChannel(((Channel) db.getChanel().get(position)).getId());
                                    follow.setIdUser(idUser);
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
                                    ((ImageView) v.findViewById(R.id.follow)).setImageResource(R.drawable.suivre);
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
                            unfollow.setIdUser(idUser);
                            unfollow.setIdChannel(((Channel) db.getChanel().get(position)).getId());

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
        } else {

            listViewHolder = (ViewHolder) convertView.getTag();
        }

        listViewHolder.logo.setImageResource(R.drawable.sentv);
        if (db.getFollowWithChannelAndUserId(((Channel) listStorage.get(position)).getId(), Status.getUserId()) == null)
        listViewHolder.follow.setImageResource(R.drawable.suivre);
        if (db.getFollowWithChannelAndUserId(((Channel) listStorage.get(position)).getId(), Status.getUserId()) != null)
            listViewHolder.follow.setImageResource(R.drawable.suivi);
        listViewHolder.name.setText(((Channel) (listStorage.get(position))).getName());
        return convertView;
    }
    public String getToken() {
        String token = PreferencesManager.getValue(contextActivity, "token");
        return token;
    }

    static class ViewHolder {
        ImageView follow;
        ImageView logo;
        TextView name;
    }
}
