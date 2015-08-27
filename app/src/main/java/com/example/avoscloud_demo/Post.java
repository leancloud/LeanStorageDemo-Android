package com.example.avoscloud_demo;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by lzw on 15/8/27.
 */
@AVClassName("Post")
public class Post extends AVObject {
  public static final Creator CREATOR = AVObjectCreator.instance;

  public static final String CONTENT = "content";
  public static final String AUTHOR = "author";
  public static final String LIKES = "likes";

  public Post() {

  }

//  Student author;
//  String content;
//  List<Student> likes;

  public Student getAuthor() {
    return getAVObject(AUTHOR);
  }

  public void setAuthor(Student author) {
    put(AUTHOR, author);
  }

  public String getContent() {
    return getString(CONTENT);
  }

  public void setContent(String content) {
    put(CONTENT, content);
  }

  public List<Student> getLikes() {
    return getList(LIKES);
  }

  public void setLikes(List<Student> likes) {
    put(LIKES, likes);
  }
}
