package com.intern.nikhil_mandlik.Activity.Util;

public class Chats {
    private Boolean isSender = null;
    private Boolean isImage = null;
    private String message = null;

    public Chats(Boolean isSender, Boolean isImage, String message) {
        this.isSender = isSender;
        this.isImage = isImage;
        this.message = message;
    }

    public Boolean getSender() {
        return isSender;
    }

    public void setSender(Boolean sender) {
        isSender = sender;
    }

    public Boolean getImage() {
        return isImage;
    }

    public void setImage(Boolean image) {
        isImage = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
