package com.example.avoscloud_demo.demo;


import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.Student;
import org.apache.http.conn.ConnectTimeoutException;

import java.util.Date;

import cn.leancloud.AVException;
import cn.leancloud.AVOSCloud;
import cn.leancloud.types.AVDate;
import io.reactivex.Observable;

/**
 * Created by lzw on 15/8/28.
 */
public class OtherDemoActivity extends DemoBaseActivity {
  public void testGetSereverDate() throws AVException {
    Observable<AVDate>  date= AVOSCloud.getServerDateInBackground();
    log("服务器时间：" + date);
  }

  public void testConfigNetworkTimeout() throws AVException {
    // 得放到 Application 里
    AVOSCloud.setNetworkTimeout(10);
    try {
      Student student = getFirstStudent();
      log("student:" + prettyJSON(student));
    } catch (AVException e) {
      log("因为设置了网络超时为 10 毫秒，所以超时了，e:" + e.getMessage());
    }
//    AVOSCloud.setNetworkTimeout(AVOSCloud.DEFAULT_NETWORK_TIMEOUT);
  }

}
