package com.example.avoscloud_demo.demo;


import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.Student;

import java.util.HashMap;

import cn.leancloud.LCCloud;
import cn.leancloud.LCException;
import cn.leancloud.LCObject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 云引擎与 SDK 的交互，需要结合云引擎项目学习 https://github.com/leancloud/sdk-demo-engine/blob/master/cloud.js
 */
public class EngineDemoActivity extends DemoBaseActivity {
  public void testCallCloudFunction() throws LCException {
    Object hello = LCCloud.callFunctionInBackground("hello", null);
    log("云引擎返回的结果:" + hello);
    logThreadTips();
  }

  public void testFetchObject() throws LCException {
    Student student = getFirstStudent();
    HashMap<String, Object> params = new HashMap<>();

    Student fetchStudent = Student.createWithoutData(Student.class, student.getObjectId());
    params.put("obj", student);
    Object fetchObject = LCCloud.callFunctionInBackground("fetchObject", params);
    log("根据返回结果构造的对象:" + fetchObject);
  }

  public void testFullObject() throws LCException {
    Object object = LCCloud.callFunctionInBackground("fullObject", null);
    log("从云引擎中获取整个对象:" + object);
  }

  public void testBeforeSave() throws LCException {
    LCObject object = new LCObject("LCCloudTest");
    object.put("string", "This is too much long, too much long, too long");
    object.setFetchWhenSave(true);
    object.save();
    log("通过 beforeSave Hook 截断至 10个字符:" + object.getString("string"));
  }
}
