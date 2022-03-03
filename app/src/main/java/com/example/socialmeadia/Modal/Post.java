package com.example.socialmeadia.Modal;

public class Post {
private String postId;
private String postImage;
private String postedBy;
private String postDecription;
private long postedAt;
private int postLike;
private int commentCount;


    public Post(String postId, String postImage, String postedBy, String postDecription, long postedAt) {
        this.postId=postId;
        this.postImage=postImage;
        this.postedBy=postedBy;
        this.postDecription=postDecription;
        this.postedAt=postedAt;
    }

    public Post() {
    }

    public Post(int postLike) {
        this.postLike=postLike;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId=postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage=postImage;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy=postedBy;
    }

    public String getPostDecription() {
        return postDecription;
    }

    public void setPostDecription(String postDecription) {
        this.postDecription=postDecription;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt=postedAt;
    }

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike=postLike;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount=commentCount;
    }
}
