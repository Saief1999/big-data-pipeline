package com.example.importmicroservice.pojos;

public class Message {

    public Message( String url) {
        this.url = url;
    }

    public Message() {}

    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
