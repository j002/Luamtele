package com.luamtele.android.model;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by masta on 10/12/15.
 */
public class Comment extends RealmObject{

    @PrimaryKey
    private int id;
    private String content;
    private String dateTime;
    private int idUser;
    private int idTvShow;

    public Comment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdTvShow() {
        return idTvShow;
    }

    public void setIdTvShow(int idTvShow) {
        this.idTvShow = idTvShow;
    }
}
