package com.luamtele.android.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by masta on 10/1/15.
 */
public class TvShow extends RealmObject {

    @PrimaryKey
    private int id;
    private int idChannel;
    private int ageInterval;
    private String duration;
    private String name;
    private String date;
    private String time;
    private String description;
    private String presentor;
    private String genre;
    private String banner;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgeInterval() {
        return ageInterval;
    }

    public void setAgeInterval(int ageInterval) {
        this.ageInterval = ageInterval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }

    public String getPresentor() {
        return presentor;
    }

    public void setPresentor(String presentor) {
        this.presentor = presentor;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
