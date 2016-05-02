package com.norigin.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ibohdan on 4/29/2016.
 */
@JsonObject
public class Movie implements Serializable {

    @JsonField
    private String id;

    @JsonField
    private String title;

    @JsonField
    private String description;

    @JsonField
    private Metadata meta;

    @JsonField
    private Image images;

    @JsonField
    private Stream streams;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setMeta(Metadata meta) {
        this.meta = meta;
    }

    public Metadata getMeta() {
        return meta;
    }

    public void setImages(Image image) {
        this.images = image;
    }

    public Image getImages() {
        return images;
    }

    public void setStreams(Stream streams) {
        this.streams = streams;
    }

    public Stream getStreams() {
        return streams;
    }
}
