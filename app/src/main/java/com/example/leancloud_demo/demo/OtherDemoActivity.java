package com.example.leancloud_demo.demo;


import com.example.leancloud_demo.DemoBaseActivity;
import com.example.leancloud_demo.Student;

import cn.leancloud.LCException;
import cn.leancloud.LeanCloud;
import cn.leancloud.types.LCDate;
import io.reactivex.Observable;

/**
 * Created by lzw on 15/8/28.
 */
public class OtherDemoActivity extends DemoBaseActivity {
  public void testGetSereverDate() throws LCException {
    Observable<LCDate>  date= LeanCloud.getServerDateInBackground();
    log("服务器时间：" + date);
  }

  public void testConfigNetworkTimeout() throws LCException {
    // 得放到 Application 里
    LeanCloud.setNetworkTimeout(10);
    try {
      Student student = getFirstStudent();
      log("student:" + prettyJSON(student));
    } catch (LCException e) {
      log("因为设置了网络超时为 10 毫秒，所以超时了，e:" + e.getMessage());
    }
//    AVOSCloud.setNetworkTimeout(AVOSCloud.DEFAULT_NETWORK_TIMEOUT);
  }

}
