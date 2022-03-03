package com.example.socialmeadia.Modal;

public class Notification {

private String notificationBY;
private long notificationAt;
private String type;
private String postId;
private String notificationId;
private String postedBy;
private boolean checjOpen;

    public String getNotificationBY() {
        return notificationBY;
    }

    public void setNotificationBY(String notificationBY) {
        this.notificationBY=notificationBY;
    }

    public long getNotificationAt() {
        return notificationAt;
    }

    public void setNotificationAt(long notificationAt) {
        this.notificationAt=notificationAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type=type;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId=postId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy=postedBy;
    }

    public boolean isChecjOpen() {
        return checjOpen;
    }

    public void setChecjOpen(boolean checjOpen) {
        this.checjOpen=checjOpen;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId=notificationId;
    }
}
