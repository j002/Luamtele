package com.luamtele.android.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by number on 19/10/15.
 */
public class Rate extends RealmObject {

    private int idUser;
    private String dateTime;
    private int value;
    private int idTvShow;


    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIdTvShow() {
        return idTvShow;
    }

    public void setIdTvShow(int idTvShow) {
        this.idTvShow = idTvShow;
    }
}
