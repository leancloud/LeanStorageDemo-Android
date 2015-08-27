package com.example.avoscloud_demo.demo;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.Student;

import java.util.HashMap;

/**
 * 云引擎与 SDK 的交互，需要结合云引擎项目学习 https://github.com/leancloud/sdk-demo-engine/blob/master/cloud.js
 */
public class EngineDemoActivity extends DemoBaseActivity {
  public void testCallCloudFunction() throws AVException {
    Object hello = AVCloud.callFunction("hello", null);
    log("云引擎返回的结果:" + hello);
    logThreadTips();
  }

  public void testErrorCode() throws AVException {
    try {
      AVCloud.callFunction("errorCode", null);
    } catch (AVException e) {
      if (e.getCode() == 211) {
        log("云引擎返回的 Error, code：" + e.getCode() + " message:" + e.getMessage());
      } else {
        throw e;
      }
    }
  }

  public void testCustomErrorCode() throws AVException {
    try {
      AVCloud.callFunction("customErrorCode", null);
    } catch (AVException e) {
      if (e.getCode() == 123) {
        log("云引擎返回的 Error, code：" + e.getCode() + " message:" + e.getMessage());
      } else {
        throw e;
      }
    }
  }

  public void testFetchObject() throws AVException {
    Student student = getFirstStudent();
    HashMap<String, Object> params = new HashMap<>();

    Student fetchStudent = Student.createWithoutData(Student.class, student.getObjectId());
    params.put("obj", student);
    Object fetchObject = AVCloud.callFunction("fetchObject", params);
    log("根据返回结果构造的对象:" + fetchObject);
  }

  public void testFullObject() throws AVException {
    Object object = AVCloud.callFunction("fullObject", null);
    log("从云引擎中获取整个对象:" + object);
  }

  public void testBeforeSave() throws AVException {
    AVObject object = new AVObject("AVCloudTest");
    object.put("string", "This is too much long, too much long, too long");
    object.setFetchWhenSave(true);
    object.save();
    log("通过 beforeSave Hook 截断至 10个字符:" + object.getString("string"));
  }
}
