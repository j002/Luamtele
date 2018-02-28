package com.luamtele.android.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.luamtele.android.model.Comment;
import com.luamtele.android.model.Like;
import com.luamtele.android.model.Rate;
import com.luamtele.android.model.TvShow;
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
 * Created by number on 21/10/15.
 */
public class TvShowViewAdapter extends ArrayAdapter<RealmObject> {

    private Context context, contextactivity;
    private LayoutInflater layoutinflater;
    private List<RealmObject> listStorage;
    LuamteleDb db;
    int idShow;
    PreferencesManager preferencesManager = new PreferencesManager();

    public TvShowViewAdapter(Context context, List<RealmObject> customizedListView, int id, Context acontext) {
        super(context, 0, customizedListView);
        this.context = context;
        contextactivity = acontext;
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
        db = new LuamteleDb(context);
        idShow = id;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        final int idUser2 =preferencesManager.getValueInt(contextactivity, "idUser");

        final String statUser = preferencesManager.getValue(contextactivity, "stat");

        if (convertView == null) {
            listViewHolder = new ViewHolder();
            if (position == 0) {
                final int posi = position;
                convertView = layoutinflater.inflate(R.layout.tvshowinflater, parent, false);
                listViewHolder.channelLogo = (ImageView) convertView.findViewById(R.id.channellogo);
               // listViewHolder.tvShowBanner = (ImageView) convertView.findViewById(R.id.tvShowBanner);
                listViewHolder.type = (TextView) convertView.findViewById(R.id.type);
                listViewHolder.tvShowName = (TextView) convertView.findViewById(R.id.tvShowName);
                listViewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
                listViewHolder.note = (TextView) convertView.findViewById(R.id.note);
                listViewHolder.tvShowDescription = (TextView) convertView.findViewById(R.id.tvShowDecription);
                listViewHolder.like = (ImageView) convertView.findViewById(R.id.idlike);
                listViewHolder.noter = (ImageView) convertView.findViewById(R.id.noter);
                convertView.setTag(listViewHolder);
                final View vi = convertView;
                convertView.findViewById(R.id.noter).setOnClickListener(new View.OnClickListener() {
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
                            final RealmResults rate1;
                            rate1 = db.getRateWithtvShowAndUserId(((TvShow) listStorage.get(posi)).getId(), Status.getUserId());
                            if (rate1 == null) {
                                final Rate rate = new Rate();
                                final Dialog rankDialog;
                                rankDialog = new Dialog(context, R.style.FullHeightDialog);
                                rankDialog.setContentView(R.layout.ratingdialog);
                                rankDialog.setCancelable(true);
                                final RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
                                ratingBar.setRating(2);
                                Button rangDialogButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                                ((TextView) rankDialog.findViewById(R.id.noteDialog)).setText("" + (int) ratingBar.getRating());
                                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                    @Override
                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                        ((TextView) rankDialog.findViewById(R.id.noteDialog)).setText("" + (int) ratingBar.getRating());
                                    }
                                });
                                rankDialog.show();

                                rangDialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        rankDialog.dismiss();
                                        Gson gson2 = new GsonBuilder()
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
                                                .registerTypeAdapter(rate.getClass(), new RealmSerializer())
                                                .create();

                                        GsonConverter converter = new GsonConverter(gson2);

                                        LuamteleApi luamteleApi1 = new RestAdapter.Builder()
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

                                        rate.setIdTvShow(idShow);
                                        rate.setValue((int) ratingBar.getRating());
                                        rate.setIdUser(idUser2);
                                        Calendar d = Calendar.getInstance();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String strDate = sdf.format(d.getTime());
                                        rate.setDateTime(strDate);
                                        Log.d("log10", "erreur " + rate.getDateTime());
                                        luamteleApi1.addrate(rate, new Callback<Rate>() {
                                            @Override
                                            public void success(Rate rate, Response response) {
                                                Log.d("log10", "succes " + rate.getDateTime());
                                                vi.findViewById(R.id.noter).setVisibility(View.INVISIBLE);

                                            }

                                            @Override
                                            public void failure(RetrofitError retrofitError) {
                                                Log.d("log10", "erreur " + retrofitError);
                                            }
                                        });
                                        db.addRate(rate);

                                    }

                                });
                            }
                        }
                    }
                });
                convertView.findViewById(R.id.idlike).setOnClickListener(new View.OnClickListener() {
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
                            RealmResults like1 = null;
                            like1 = db.getLikeWithtvShowAndUserId(((TvShow) listStorage.get(posi)).getId(), Status.getUserId());

                            if (like1 == null) {
                                ((ImageView) v.findViewById(R.id.idlike)).setImageResource(R.drawable.liked);
                                Like like = new Like();
                                Gson gson3 = new GsonBuilder()

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
                                        .registerTypeAdapter(Like.class, new RealmSerializer())
                                        .create();

                                GsonConverter converter = new GsonConverter(gson3);

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

                                like.setIdTvShow(idShow);
                                like.setIdUser(idUser2);
                                Calendar d = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String strDate = sdf.format(d.getTime());
                                like.setDateTime(strDate);

                                luamteleApi.addlike(like, new Callback<Like>() {
                                    @Override
                                    public void success(Like like, Response response) {
                                        Log.d("log10", "succes ");
                                    }

                                    @Override
                                    public void failure(RetrofitError retrofitError) {
                                        Log.d("log10", "erreur " + retrofitError);
                                    }
                                });
                                db.addLike(like);
                            } else {
                                ((ImageView) v.findViewById(R.id.idlike)).setImageResource(R.drawable.like);
                                db.deleteObjects(like1);

                                Like unlike = new Like();
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
                                        .registerTypeAdapter(Like.class, new RealmSerializer())
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
                                unlike.setIdUser(idUser2);
                                unlike.setIdTvShow(idShow);
                                luamteleApi.deletelike(unlike.getIdUser(), unlike.getIdTvShow(), new Callback<JsonElement>() {
                                    @Override
                                    public void success(JsonElement jsonElement, Response response) {
                                        Log.d("log10", "unlike matess");
                                    }

                                    @Override
                                    public void failure(RetrofitError retrofitError) {
                                        Log.d("log10", "erreor " + retrofitError);
                                    }
                                });

                            }
                        }
                    }
                });
                convertView.setOnClickListener(null);
            } else if (position != 0) {
                convertView = layoutinflater.inflate(R.layout.channel_tvshows_inflater, parent, false);
                listViewHolder.text = (TextView) convertView.findViewById(R.id.commentContent);
                listViewHolder.user = (TextView) convertView.findViewById(R.id.userSurnameCom);
                listViewHolder.date = (TextView) convertView.findViewById(R.id.commentDate);
                convertView.setTag(listViewHolder);
            }

        } else {

            listViewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            float rate = 0;

            if (db.getRate() == null)
                listViewHolder.note.setText("" + (int) rate);
            else {
                for (RealmObject r : db.getRate()) {
                    rate += ((float) ((Rate) r).getValue());
                }
                rate = rate / db.getRate().size();
                listViewHolder.note.setText("" + (int) rate);

            }

            if (db.getLikeWithtvShowAndUserId(((TvShow) (listStorage.get(position))).getId(), Status.getUserId()) == null)
                listViewHolder.like.setImageResource(R.drawable.like);
            if (db.getLikeWithtvShowAndUserId(((TvShow) (listStorage.get(position))).getId(), Status.getUserId()) != null) {
                listViewHolder.like.setImageResource(R.drawable.liked);
            }
            if (db.getRateWithtvShowAndUserId(((TvShow) (listStorage.get(position))).getId(), Status.getUserId()) != null) {
                listViewHolder.noter.setVisibility(View.INVISIBLE);
            }
            listViewHolder.channelLogo.setImageResource(R.drawable.tfm);
            listViewHolder.tvShowDescription.setText(((TvShow) (listStorage.get(position))).getDescription());
            listViewHolder.tvShowName.setText(((TvShow) (listStorage.get(position))).getName());
            listViewHolder.type.setText(((TvShow) (listStorage.get(position))).getGenre());
            listViewHolder.duration.setText(((TvShow) (listStorage.get(position))).getDuration() + " min");
        }
        if (position != 0) {
            String surnameUser;
            listViewHolder.text.setText(((Comment) (listStorage.get(position))).getContent());
            listViewHolder.date.setText((((Comment) (listStorage.get(position))).getDateTime() + " : "));
            surnameUser = db.getUserWithId(((Comment) (listStorage.get(position))).getIdUser()).getSurname();
            listViewHolder.user.setText(surnameUser);
            if(position == listStorage.size()-1){
                convertView.setPadding(0,0,0,40);
            }
            }
        return convertView;
    }

    static class ViewHolder {
        ImageView channelLogo;
        TextView tvShowName;
        TextView type;
        TextView duration;
        TextView note;
        ImageView noter;
        ImageView like;
        TextView tvShowDescription;
        TextView text;
        TextView user;
        TextView date;
    }
    public String getToken() {
        String token = PreferencesManager.getValue(context, "token");
        return token;
    }
}
