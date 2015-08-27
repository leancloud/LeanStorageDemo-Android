package com.example.avoscloud_demo.demo;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVQuery;
import com.example.avoscloud_demo.DemoBaseActivity;

/**
 * Created by lzw on 15/8/27.
 */
public class CQLDemoActivity extends DemoBaseActivity {
  void logResult(String cql, AVCloudQueryResult result) {
    log("cql:%s\nresult:%s", cql, prettyJSON(result.getResults()));
  }

  void logCount(String cql, AVCloudQueryResult result) {
    log("cql:%s\ncount:%d", cql, result.getCount());
  }

  public void testSelect() throws Exception {
    String cql = "select * from _User";
    AVCloudQueryResult result = AVQuery.doCloudQuery(cql);
    logResult(cql, result);
  }

  public void testCount() throws Exception {
    String cql = "select count(*) from _User";
    AVCloudQueryResult result = AVQuery.doCloudQuery(cql);
    logCount(cql, result);
  }

  public void testSelectWhere() throws Exception {
    String cql = String.format("select * from _User where username=?");
    AVCloudQueryResult result = AVQuery.doCloudQuery(cql, "XiaoMing");
    logResult(cql, result);
  }

  public void testSelectWhereIn() throws Exception {
    String cql = String.format("select * from _User where username in (?,?)");
    AVCloudQueryResult result = AVQuery.doCloudQuery(cql, "XiaoMing", "lzwjava@gmail.com");
    logResult(cql, result);
  }

  public void testSelectWhereDate() throws Exception {
    String cql = String.format("select * from _User where createdAt<date(?) order by -createdAt limit ?");
    AVCloudQueryResult result = AVQuery.doCloudQuery(cql, "2015-05-01T00:00:00.0000Z", "3");
    logResult(cql, result);
  }
}
