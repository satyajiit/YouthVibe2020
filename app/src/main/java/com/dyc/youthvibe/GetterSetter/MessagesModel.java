package com.dyc.youthvibe.GetterSetter;

import android.util.Log;

public class MessagesModel {

    String msg, author,title;
Long time;

    public MessagesModel( String title, String msg, String author, Long time) {
        this.msg = msg;
        this.author = author;
        this.time = time;
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
