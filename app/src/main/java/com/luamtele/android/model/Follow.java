package com.luamtele.android.model;

import io.realm.RealmObject;

/**
 * Created by number on 19/10/15.
 */
public class Follow extends RealmObject {


    private int idUser;
    private int idChannel;
    private String dateTime;


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

    public int getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }


}
