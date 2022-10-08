package com.example.jsonplaceholder.model.data.entity;

import com.example.jsonplaceholder.model.PostDetailItem;

public class PostDetailPostItem extends PostDetailItem {
    private Post post;

    public PostDetailPostItem(Post post) {
        this.post = post;
    }

    @Override
    public Type getType() {
        return Type.POST;
    }

    public String getTitle(){
        return post.getTitle();
    }

    public String getBody(){
        return post.getBody();
    }
}
