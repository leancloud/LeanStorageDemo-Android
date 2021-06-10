package com.example.leancloud_demo;

import android.app.Application;

import cn.leancloud.LCLogger;
import cn.leancloud.LeanCloud;
import cn.leancloud.LCObject;


/**
 * Created with IntelliJ IDEA.
 * User: zhuzeng
 * Date: 12/12/13
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class DemoApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    LeanCloud.setLogLevel(LCLogger.Level.DEBUG);

//    AVOSCloud.setNetworkTimeout(20 * 1000);
    LCObject.registerSubclass(Student.class);
    LCObject.registerSubclass(Post.class);
  }

}
