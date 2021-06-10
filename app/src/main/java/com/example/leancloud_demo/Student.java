package com.example.leancloud_demo;


import android.os.Parcelable;

import java.util.List;

import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import cn.leancloud.LCParcelableObject;
import cn.leancloud.annotation.LCClassName;

/**
 * Created by lzw on 15/8/25.
 */
@LCClassName("Student")
public class Student extends LCObject {
  public static final Parcelable.Creator CREATOR = LCParcelableObject.LCObjectCreator.instance;

  public static final String NAME = "name";
  public static final String AGE = "age";
  public static final String AVATAR = "avatar";
  public static final String HOBBIES = "hobbies";
  public static final String ANY = "any";

  public Student() {

  }

  //  private String name;
//  private int age;
//  private LCFile avatar;
//  private Object any;
//  List<String> hobbies;

  public String getName() {
    return getString(NAME);
  }

  public void setName(String name) {
    put(NAME, name);
  }

  public int getAge() {
    return getInt(AGE);
  }

  public void setAge(int age) {
    put(AGE, age);
  }

  public LCFile getAvatar() {
    return getLCFile(AVATAR);
  }

  public void setAvatar(LCFile avatar) {
    put(AVATAR, avatar);
  }

  public Object getAny() {
    return get(ANY);
  }

  public void setAny(Object any) {
    put(ANY, any);
  }

  public List<String> getHobbies() {
    return getList(HOBBIES);
  }

  public void setHobbies(List<String> hobbies) {
    put(HOBBIES, hobbies);
  }
}
