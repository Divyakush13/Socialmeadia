package com.example.socialmeadia.Modal;

public class UserStory {
    private String image;
    private long StoryAt;

    public UserStory(String image, long storyAt) {
        this.image=image;
        StoryAt=storyAt;
    }

    public UserStory() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image=image;
    }

    public long getStoryAt() {
        return StoryAt;
    }

    public void setStoryAt(long storyAt) {
        StoryAt=storyAt;
    }
}
