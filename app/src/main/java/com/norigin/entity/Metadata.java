package com.norigin.entity;

import android.support.annotation.Nullable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ibohdan on 4/29/2016.
 */
@JsonObject
public class Metadata implements Serializable {

    @JsonField
    private String releaseYear;

    @JsonField
    private List<Person> directors = new ArrayList<>();

    @JsonField
    private List<Person> actors = new ArrayList<>();

    public void setReleaseYear(@Nullable String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setDirectors(List<Person> directors) {
        this.directors = directors;
    }

    public List<Person> getDirectors() {
        return directors;
    }

    public void setActors(List<Person> actors) {
        this.actors = actors;
    }

    public List<Person> getActors() {
        return actors;
    }
}
