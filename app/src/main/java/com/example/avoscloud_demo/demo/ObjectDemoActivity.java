package com.example.avoscloud_demo.demo;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.example.avoscloud_demo.DemoBaseActivity;
import junit.framework.Assert;

import java.util.List;
import java.util.Random;

public class ObjectDemoActivity extends DemoBaseActivity {

  // create an object and query it.
  public void testObjectRead() throws AVException {
    final String key = "array";
    final String objectTable = "ObjectDemoTableRead";
    final AVObject myObject = new AVObject(objectTable);
    for (int i = 0; i < 5; ++i) {
      myObject.add(key, i);
    }
    myObject.save();

    AVQuery<AVObject> query = AVQuery.getQuery(objectTable);
    AVObject result = query.get(myObject.getObjectId());
    List<Number> array = result.getList(key);
    Assert.assertTrue(array.size() == 5);
    if (array.size() != 5) {
      showMessage("", new AVException(AVException.OTHER_CAUSE, "incorrect result"), false);
    } else {
      showMessage("", null, false);
    }
    setProgressBarIndeterminateVisibility(false);
  }

  public void testObjectCreate() throws AVException {

    final String objectTable = "ObjectDemoTableCreate";
    final String key = "score";
    AVObject gameScore = new AVObject(objectTable);
    final int targetValue = new Random().nextInt();
    gameScore.put(key, targetValue);
    int value = gameScore.getInt(key);
    Assert.assertTrue(value == targetValue);

    final String targetString = "Sean Plott";
    gameScore.put("playerName", targetString);
    String stringValue = gameScore.getString("playerName");
    Assert.assertTrue(stringValue == targetString);
    gameScore.save();
  }

  // update an object
  public void testObjectUpdate() throws AVException {
    final String key = "update";
    final String objectTable = "ObjectDemoTableUpdate";
    final AVObject myObject = new AVObject(objectTable);
    final String value = "anotherValue";
    myObject.put(key, "myValue");
    myObject.save();

    myObject.put(key, value);
    myObject.save();
    AVQuery<AVObject> query = AVQuery.getQuery(objectTable);
    AVObject result = query.get(myObject.getObjectId());
    String stringValue = (String) result.get(key);
    Assert.assertEquals(stringValue, value);
    if (!value.equals(stringValue)) {
      showMessage("", new AVException(AVException.OTHER_CAUSE, "incorrect result"), false);
    } else {
      showMessage("", null, false);
    }
  }

  public void testObjectDelete() throws AVException {
    final String objectTable = "ObjectDemoTableDelete";
    final AVObject myObject = new AVObject(objectTable);
    myObject.save();
    myObject.delete();
    AVQuery<AVObject> query = AVQuery.getQuery(objectTable);
    AVObject result = query.get(myObject.getObjectId());
    Assert.assertTrue(result == null);
    if (result != null) {
      showMessage("", new AVException(AVException.OTHER_CAUSE, "delete failed"), false);
    } else {
      showMessage("", null, false);
    }
  }
}
