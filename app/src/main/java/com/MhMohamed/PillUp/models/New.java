package com.MhMohamed.PillUp.models;

import java.io.Serializable;

/**
 * Created by Mohamed on 3/13/2018.
 */

public class New implements Serializable{

    String title;
    String content;
    String imageUrl;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
