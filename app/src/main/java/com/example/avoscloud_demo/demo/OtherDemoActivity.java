package com.example.avoscloud_demo.demo;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.Student;

import java.util.Date;

/**
 * Created by lzw on 15/8/28.
 */
public class OtherDemoActivity extends DemoBaseActivity {
  public void testGetSereverDate() throws AVException {
    Date date = AVOSCloud.getServerDate();
    log("服务器时间：" + date);
  }

  public void testConfigNetworkTimeout() throws AVException {
    // 得放到 Application 里
    AVOSCloud.setNetworkTimeout(10);
    try {
      Student student = getFirstStudent();
      log("student:" + prettyJSON(student));
    } catch (Exception e) {
      log("因为设置了网络超时为 10 毫秒，所以超时了，e:" + e.getMessage());
    }
    AVOSCloud.setNetworkTimeout(AVOSCloud.DEFAULT_NETWORK_TIMEOUT);
  }

}
