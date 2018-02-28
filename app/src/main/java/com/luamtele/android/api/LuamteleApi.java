package com.luamtele.android.api;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.luamtele.android.model.Channel;
import com.luamtele.android.model.Comment;
import com.luamtele.android.model.Follow;
import com.luamtele.android.model.Like;
import com.luamtele.android.model.Rate;
import com.luamtele.android.model.TvShow;
import com.luamtele.android.model.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.*;
import retrofit.http.*;



/**
 * Created by HP on 20/10/2015.
 */
public interface LuamteleApi {


public static final String TvApiUrl="http://192.168.1.20/luamtele-api";
//public static final String TvApiWebUrl="http://192.168.1.14/Luamteleweb/upload";
    // public static final String TvApiUrl="http://volkeno-tank.com/api/luamtele";
    //Luamteleweb/upload/

    /*<<------------------------------Posts-------------------------------->>*/
     @POST("/api/users")
     void adduser(@Body User User, Callback<User> response);
    @POST("/api/doLoginUsers")
    void login(@Body HashMap<String, String> user, Callback<JsonElement> calback);
    @POST("/api/comments")
    void addcomment(@Body Comment comment, Callback<Comment> response);
    @POST("/api/likes")
    void addlike(@Body Like like, Callback<Like> response);
    @POST("/api/rates")
    void addrate(@Body Rate rate, Callback<Rate> response);
     @POST("/api/follows")
     void addfollow(@Body Follow follow, Callback<Follow> response);

    /*<<------------------------------Deletes-------------------------------->>*/

    @DELETE("/api/follows/{idUser}/{idChannel}")
    void deletefollow(@Path("idUser") int id1  ,@Path("idChannel") int  id2, Callback<JsonElement> callback);
    @DELETE("/api/likes/{idUser}/{idTvShow}")
    void deletelike(@Path("idUser") int id1  ,@Path("idTvShow") int  id2, Callback<JsonElement> callback);

  /*<<------------------------------Gets-------------------------------->>*/

    @GET("/api/users")
    public void getUsers(Callback<List<User>> response);
    @GET("/api/tvShows")
    public void getTvShows(Callback<List<TvShow>> response);
    @GET("/api/channels")
    public void getChannels(Callback<List<Channel>> response);
    @GET("/api/comments")
    public void getComments(Callback<List<Comment>> response);
    @GET("/api/likes")
    public void getLikes(Callback<List<Like>> response);
    @GET("/api/rates")
    public void getRates(Callback<List<Rate>> response);
    @GET("/api/follows")
    public void getFollows(Callback<List<Follow>> response);
    @GET("/upload")
    public void getImage(Callback<String> response);
}
