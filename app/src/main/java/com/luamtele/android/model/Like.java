package com.luamtele.android.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by number on 19/10/15.
 */
public class Like extends RealmObject {

    private int idUser;
    private int idTvShow;
    private String dateTime;


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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
