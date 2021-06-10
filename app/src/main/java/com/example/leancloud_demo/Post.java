package com.example.leancloud_demo;


import android.os.Parcelable;

import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCParcelableObject;
import cn.leancloud.LCRelation;
import cn.leancloud.annotation.LCClassName;

/**
 * Created by lzw on 15/8/27.
 */
@LCClassName("Post")
public class Post extends LCObject {
  public static final Parcelable.Creator CREATOR = LCParcelableObject.LCObjectCreator.instance;

  public static final String CONTENT = "content";
  public static final String AUTHOR = "author";
  public static final String LIKES = "likes";
  public static final String REWARDS = "rewards"; // 打赏

  public Post() {

  }

//  Student author;
//  String content;
//  List<Student> likes;
//  LCRelation<Student> rewardStudents;

  public Student getAuthor() {
    return getLCObject(AUTHOR);
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

  public LCRelation<Student> getRewardStudents() {
    return getRelation(REWARDS);
  }

  public void setRewardStudents(LCRelation<Student> rewardStudents) {
    put(REWARDS, rewardStudents);
  }
}
