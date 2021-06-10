package com.example.leancloud_demo;


import cn.leancloud.LCObject;
import cn.leancloud.LCUser;

public class SubUser extends LCUser {
  public LCObject getArmor() {
    return getLCObject("armor");
  }

  public void setArmor(LCObject armor) {
    this.put("armor", armor);
  }

  public void setNickName(String name) {
    this.put("nickName", name);
  }

  public String getNickName() {
    return this.getString("nickName");
  }
}
