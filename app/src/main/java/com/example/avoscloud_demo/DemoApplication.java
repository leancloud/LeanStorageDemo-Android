package com.example.avoscloud_demo;

import android.app.Application;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.LogUtil;

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
//    AVOSCloud.setNetworkTimeout(20 * 1000);
    AVObject.registerSubclass(Student.class);
    AVObject.registerSubclass(Post.class);
    AVOSCloud.setDebugLogEnabled(true);
  }

}
