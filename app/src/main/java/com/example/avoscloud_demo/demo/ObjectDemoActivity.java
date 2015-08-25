package com.example.avoscloud_demo.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.R;
import com.example.avoscloud_demo.Student;
import junit.framework.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ObjectDemoActivity extends DemoBaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AVObject.registerSubclass(Student.class);
  }

  public void testCreateObject() throws AVException {
    Student student = new Student();
    student.setAge(12);
    student.setName("Mike");
    student.save();
    log("保存了一个学生：" + student);
  }

  public Student getFirstStudent() throws AVException {
    AVQuery<Student> q = AVObject.getQuery(Student.class);
    return q.getFirst();
  }

  public void testUpdateObject() throws AVException {
    Student student = getFirstStudent();
    log("更改前学生的年龄：" + student.getAge());
    student.setAge(20);
    student.save();
    log("更改后学生的年龄：" + student.getAge());
  }

  public void testDeleteObject() throws AVException {
    Student student = getFirstStudent();
    //删掉了第一个学生
    student.delete();
    log("删掉了学生：" + student);
  }

  public void testGetObject() throws AVException {
    Student first = getFirstStudent();

    Student student = AVObject.createWithoutData(Student.class, first.getObjectId());
    AVObject fetched = student.fetch();
    log("用 objectId 创建了对象，并获取了数据：" + fetched);
  }

  public void testCreateObjectWithFile() throws IOException, AVException {
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, output);
    byte[] bytes = output.toByteArray();
    AVFile avatar = new AVFile("avatar", bytes);

    Student student = new Student();
    student.setName(getClassName());
    student.setAvatar(avatar);
    student.save();
    log("保存了文件，并把其作为一个字段保存到了对象。student: " + student);
  }

  public void testObjectParse() throws Exception {
    Student student = getFirstStudent();
    String s = student.toString();
    log("将对象序列化成字符串：" + s);

    AVObject parseObject = AVObject.parseAVObject(s);
    log("从字符串中解析对象：" + parseObject);
  }

  public void testObjectIntent() throws AVException {
    Student student = getFirstStudent();
    Intent intent = new Intent();
    intent.putExtra("student", student);

    Student intentStudent = intent.getParcelableExtra("student");
    log("通过 intent 传递了对象 " + intentStudent);
  }

  public void testOfflineSave() {
    log("请在网络关闭的时候运行本方法，然后开启网络，看是否保存上");
    Student student = new Student();
    student.setName("testOfflineSave");
    student.saveEventually();
    log("离线保存了对象：" + student);
  }

  public void testIncrement() throws AVException {
    Student student = getFirstStudent();
    log("生日前的年龄：%d", student.getAge());
    student.increment(Student.AGE, 1);
    student.save();
    log("生日了，年龄：%d", student.getAge());
  }

  public void testAnyType() throws AVException {
    Student student = getFirstStudent();
    student.setAny(1);
    student.save();
    log("Any 字段保存为了数字 " + student.getAny());

    student.setAny("hello");
    student.save();
    log("Any 字段保存为了字符串 " + student.getAny());

    HashMap<String, Object> map = new HashMap<>();
    map.put("like", "swimming");
    student.setAny(map);
    student.save();
    log("Any 字段保存为了Map " + student.getAny());
  }

  public void testRemoveKey() throws AVException {
    Student student = getFirstStudent();
    log("名字：" + student.getName());

    student.remove(Student.NAME);
    student.save();
    log("将名字字段置为空后：", student.getName());
  }

  public void testArrayAddObject() throws AVException {
    Student student = getFirstStudent();
    log("添加前的爱好：" + student.getHobbies());
    List<String> hobbies = new ArrayList<>();
    hobbies.add("running");
    hobbies.add("fly");
    student.addAll(Student.HOBBIES, hobbies);
    student.save();
    log("添加了两个爱好, hobbies : " + student.getHobbies());
  }

  public void testArrayAddMutipleObjects() throws AVException {
    Student student = getFirstStudent();
    student.add(Student.HOBBIES, "swimming");
    student.save();
    log("添加了游泳爱好, hobbies : " + student.getHobbies());
  }

  public void testArrayRemoveObject() throws AVException {
    Student student = getFirstStudent();
    log("移除爱好前，hobbies = " + student.getHobbies());
    List<String> removeHobbies = new ArrayList<>();
    removeHobbies.add("swimming");
    student.removeAll(Student.HOBBIES, removeHobbies);
    student.save();
    log("移除爱好后, hobbies = " + student.getHobbies());
  }

  public void testArrayAddUnique() throws AVException {
    Student student = getFirstStudent();
    student.addUnique(Student.HOBBIES, "swimming");
    student.save();
    log("添加了游泳的爱好之后, hobbies: " + student.getHobbies());

    student.addUnique(Student.HOBBIES, "swimming");
    student.save();
    log("再次 addUnique 游泳爱好, hobbies:" + student.getHobbies());
  }

  public void testSaveAll() throws AVException {
    List<Student> students = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Student student = new Student();
      student.setName(i + "");
      student.setAge(i + 10);
      students.add(student);
    }
    AVObject.saveAll(students);

    log("保存了五个学生: " + students);
  }

  public List<Student> findStudents() throws AVException {
    AVQuery<Student> q = AVObject.getQuery(Student.class);
    q.limit(5);
    return q.find();
  }

  public void testDeleteAll() throws AVException {
    List<Student> students = findStudents();
    AVObject.deleteAll(students);

    log("删除掉了一批学生 " + students);
  }

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
