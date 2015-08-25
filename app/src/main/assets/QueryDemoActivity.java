package com.example.avoscloud_demo.demo;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.DemoUtils;
import junit.framework.Assert;

import java.util.List;

public class QueryDemoActivity extends DemoBaseActivity {

  // create an object and query it.
  public void testObjectQuery() throws AVException {
    AVObject person1 = AVObject.create("Person");
    person1.put("gender", "Female");
    person1.put("name", "Cake");
    person1.save();

    AVObject person2 = AVObject.create("Person");
    person2.put("gender", "Male");
    person2.put("name", "Man");
    person2.save();

    AVObject something = AVObject.create("Something");
    something.put("belongTo", "Cake");
    something.put("city", "ChangDe");
    something.save();

    AVObject another = AVObject.create("Something");
    another.put("belongTo", "Man");
    another.put("city", "Beijing");
    another.save();

    AVQuery q1 = AVQuery.getQuery("Person");
    q1.whereEqualTo("gender", "Female");

    AVQuery q2 = AVQuery.getQuery("Something");
    q2.whereMatchesKeyInQuery("belongTo", "name", q1);
    List<AVObject> objects = q2.find();
    Assert.assertTrue(objects.size() > 0);
    for (AVObject obj : objects) {
      Assert.assertTrue(obj.getString("belongTo").equals("Cake"));
    }

    AVQuery q3 = AVQuery.getQuery("Something");
    q3.whereDoesNotMatchKeyInQuery("belongTo", "name", q1);
    List<AVObject> list = q3.find();
    Assert.assertTrue(list.size() > 0);
    for (AVObject obj : list) {
      Assert.assertFalse(obj.getString("belongTo").equals("Cake"));
    }
  }

  public void testUserQuery() throws AVException {
    String lastString = null;
    // signup some test user
    for (int i = 0; i < 10; ++i) {
      AVUser user = new AVUser();
      user.setUsername(DemoUtils.getRandomString(10));
      user.setPassword(DemoUtils.getRandomString(10));
      user.signUp();
      Assert.assertFalse(user.getObjectId().isEmpty());
      lastString = user.getUsername();
    }

    AVQuery currentQuery = AVUser.getQuery();
    AVQuery innerQuery = AVUser.getQuery();
    innerQuery.whereContains("username", lastString);
    currentQuery.whereMatchesKeyInQuery("username", "username", innerQuery);
    List<AVUser> users = currentQuery.find();
    Assert.assertTrue(users.size() == 1);
    for (AVUser resultUser : users) {
      Assert.assertTrue(resultUser.getUsername().equals(lastString));
    }
  }
}
