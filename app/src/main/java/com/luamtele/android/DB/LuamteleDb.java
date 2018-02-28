package com.luamtele.android.DB;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.luamtele.android.api.LuamteleApi;
import com.luamtele.android.api.RealmSerializer;
import com.luamtele.android.model.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luamtele.android.utils.PreferencesManager;

import io.realm.Realm;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by HP on 19/10/2015.
 */
public class LuamteleDb {
    private Realm realm=null;
    private LuamteleApi luamteleApi;
    private Context context = null;
    public LuamteleDb(Context ctx) {
        context = ctx;
        this.realm = Realm.getInstance(ctx);
    }
    public ArrayList<RealmObject> getChanel() {
        RealmResults<Channel> result = realm.where(Channel.class)
                .findAll();
        ArrayList<RealmObject> chanels = new ArrayList<>();
        for(Channel ch:result) chanels.add(ch);

        return chanels;
    }
    public ArrayList<RealmObject> getComment() {
        RealmResults<Comment> result = realm.where(Comment.class)
                .findAll();
        ArrayList<RealmObject> comments = new ArrayList<>();
        for(Comment cmt:result) comments.add(cmt);

        return comments;
    }
    public ArrayList<RealmObject> getCommentWithTvShowId(int id) {
        RealmResults<Comment> result = realm.where(Comment.class)
                .equalTo("idTvShow", id)
                .findAll();
        ArrayList<RealmObject> comments = new ArrayList<>();
        for(Comment cmt:result) comments.add(cmt);
        return comments;
    }
    public ArrayList<RealmObject> getFollow() {
        RealmResults<Follow> result = realm.where(Follow.class)
                .findAll();
        ArrayList<RealmObject> follows= new ArrayList<>();
        for(Follow fl:result) follows.add(fl);

        return follows;
    }
    public ArrayList<RealmObject> getLike() {
        RealmResults<Like> result = realm.where(Like.class)
                .findAll();
        ArrayList<RealmObject> likes= new ArrayList<>();
        for(Like lk:result) likes.add(lk);

        return likes;
    }
    public ArrayList<RealmObject> getRate() {
        RealmResults<Rate> result = realm.where(Rate.class)
                .findAll();
        ArrayList<RealmObject> rates= new ArrayList<>();
        for(Rate rate:result) rates.add(rate);

        return rates;
    }
    public ArrayList<RealmObject> getTvShow() {
        RealmResults<TvShow> result = realm.where(TvShow.class)
                .findAll();
        ArrayList<RealmObject> tvshows= new ArrayList<>();
        for(TvShow tv:result) tvshows.add(tv);

        return tvshows;
    }
    public ArrayList<RealmObject> getUser() {
        RealmResults<User> result = realm.where(User.class)
                .findAll();
        ArrayList<RealmObject> users= new ArrayList<>();
        for(User us:result) users.add(us);

        return users;
    }
    public TvShow getTvShowWithId(int id) {
        RealmResults<TvShow> result = realm.where(TvShow.class)
                .equalTo("id", id)
                .findAll();
        if(result.size() > 0) return result.get(0);
        return null;
    }
    public TvShow getTvShowWithName(String name) {
        RealmResults<TvShow> result = realm.where(TvShow.class)
                .equalTo("name", name)
                .findAll();
        if(result.size() > 0) return result.get(0);
        return null;
    }
    public Channel getChannelWithName(String name) {
        RealmResults<Channel> result = realm.where(Channel.class)
                .equalTo("name", name)
                .findAll();
        if(result.size() > 0) return result.get(0);
        return null;
    }
    public Channel getChannelWithId(int id) {
        RealmResults<Channel> result = realm.where(Channel.class)
                .equalTo("id", id)
                .findAll();
        if(result.size() > 0) return result.get(0);
        return null;
    }
    public User getUserWithId(int id) {
        RealmResults<User> result = realm.where(User.class)
                .equalTo("id", id)
                .findAll();
        if(result.size() > 0) return result.get(0);
        return null;
    }
    public RealmResults<TvShow> getTvShowWithDay(String date) {
        RealmResults<TvShow> result = realm.where(TvShow.class)
                .contains("date", date)
                .findAll();

         return result;
    }
    public List<RealmObject> getTvShowWithChannelId(int id) {
        RealmResults<TvShow> result = realm.where(TvShow.class)
                .equalTo("idChannel", id)
                .findAll();
        ArrayList<RealmObject> tvshows= new ArrayList<>();
        for(TvShow tv:result) tvshows.add(tv);
        return tvshows;
    }
    public RealmResults getFollowWithChannelAndUserId(int idChannel,int idUser) {
        Log.d("dougoubi ",""+idChannel+" "+idUser);
        RealmResults<Follow> result = realm.where(Follow.class)
                .equalTo("idChannel", idChannel)
                .equalTo("idUser", idUser)
                .findAll();
        Log.d("dougou ",""+result);
        if(result.size() > 0) return result;
        return null;
    }
    public RealmResults getRateWithtvShowAndUserId(int idTvShow,int idUser) {
        RealmResults<Rate> result = realm.where(Rate.class)
                .equalTo("idTvShow", idTvShow)
                .equalTo("idUser", idUser)
                .findAll();
        if(result.size() > 0) return result;
        return null;
    }
    public RealmResults getLikeWithtvShowAndUserId(int idTvShow,int idUser) {
        RealmResults<Like> result = realm.where(Like.class)
                .equalTo("idTvShow", idTvShow)
                .equalTo("idUser", idUser)
                .findAll();
        if(result.size() > 0) return result;
        return null;
    }
    public List<RealmObject> getTvShowWithTime(String date,int time){
        RealmResults<TvShow> result = getTvShowWithDay(date);
        ArrayList<RealmObject> tvShows = new ArrayList<>();
         for(TvShow tv:result)
            if(Integer.parseInt(tv.getTime().substring(0, 2)) <= time+2 && Integer.parseInt(tv.getTime().substring(0, 2)) >=time -1 ) tvShows.add(tv);
           return tvShows;
    }
    public void addObjects(List<RealmObject> objects) {
        realm.beginTransaction();
        List<RealmObject> temp = realm.copyToRealmOrUpdate(objects);
        if(temp != null) realm.commitTransaction();
    }
    public void addLike(Like like) {
        realm.beginTransaction();
        Like like1 = realm.createObject(Like.class);
        like1.setIdTvShow(like.getIdTvShow());
        like1.setIdUser(like.getIdUser());
        like1.setDateTime(like.getDateTime());
        realm.commitTransaction();
    }
    public void addRate(Rate rate) {
        realm.beginTransaction();
        Rate rate1 = realm.createObject(Rate.class);
        rate1.setIdTvShow(rate.getIdTvShow());
        rate1.setIdUser(rate.getIdUser());
        rate1.setDateTime(rate.getDateTime());
        rate1.setValue(rate.getValue());
        realm.commitTransaction();
    }
    public void addFollow(Follow follow) {
        realm.beginTransaction();
        Follow follow1 = realm.createObject(Follow.class);
        follow1.setIdChannel(follow.getIdChannel());
        follow1.setIdUser(follow.getIdUser());
        follow1.setDateTime(follow.getDateTime());
        realm.commitTransaction();
    }
    public void deleteObjects(RealmResults result) {
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

    }
    public void getInitializeApi(RealmObject object) {
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
                .registerTypeAdapter(object.getClass(), new RealmSerializer())
                .create();

        GsonConverter converter = new GsonConverter(gson);

        setLuamteleApi(new RestAdapter.Builder()
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
                .create(LuamteleApi.class));

    }

    public String getToken() {
        String token = PreferencesManager.getValue(context, "token");
        return token;
    }


    public String getDay(int day){
        if(day == 1) return "lun";
        else if(day == 2) return "mar";
        else if(day == 3) return "mer";
        else if(day == 4) return "jeu";
        else if(day == 5) return "ven";
        else if(day == 6) return "sam";
        else return "dim";
     }
    public LuamteleApi getLuamteleApi() {
        return luamteleApi;
    }
    public void setLuamteleApi(LuamteleApi luamteleApi) {
        this.luamteleApi = luamteleApi;
    }

}
