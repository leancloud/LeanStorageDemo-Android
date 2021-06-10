package com.example.leancloud_demo.demo;

import com.example.leancloud_demo.DemoBaseActivity;
import com.example.leancloud_demo.Student;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import cn.leancloud.LCException;
import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCSaveOption;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ObjectDemoActivity extends DemoBaseActivity {

  public void testCreateObject() throws LCException {
    Student student = new Student();
    student.setAge(12);
    student.setName("Mike");
    student.save();
    log("保存了一个学生：" + prettyJSON(student));
    logThreadTips();
  }

  public void testSaveWithOption() throws LCException {
    final LCObject LCObject1 = LCObject.createWithoutData("Student", "5a7a4ac8128fe1003768d2b1");
    LCObject1.fetchInBackground().subscribe(new Observer<LCObject>() {
      @Override
      public void onSubscribe(Disposable d) {
      }
      @Override
      public void onNext(LCObject LCObject) {
        System.out.println(LCObject.getUpdatedAt());
        LCSaveOption LCSaveOption = new LCSaveOption();
        LCSaveOption.query(new LCQuery("Student").whereLessThanOrEqualTo("updatedAt", LCObject.getUpdatedAt()));
        LCObject.put("sss","xxx");
        LCObject.saveInBackground(LCSaveOption).subscribe();
      }
      @Override
      public void onError(Throwable e) {
      }
      @Override
      public void onComplete() {
      }
    });
  }

  public void testUpdateObject() throws LCException {
    Student student = getFirstStudent();
    log("更改前学生的年龄：" + student.getAge());
    student.setAge(20);
    student.add("course", "Math");
    student.addAllUnique("course", Arrays.asList("Math", "Art"));
    student.removeAll("course", Arrays.asList("Reading"));
    student.save();
    log("更改后学生的年龄：" + student.getAge());
  }

  public void testDeleteObject() throws LCException {
    Student student = getFirstStudent();
    //删掉了第一个学生
    student.delete();
    log("删掉了学生：" + prettyJSON(student));

    try {
      LCQuery<Student> query = LCQuery.getQuery(Student.class);
      query.get(student.getObjectId());
    } catch (Exception e) {
      log("再次去获取这个学生，抛出异常：" + e.getMessage());
    }
  }

  public void testGetObject() throws LCException {
    Student first = getFirstStudent();

    Student student = LCObject.createWithoutData(Student.class, first.getObjectId());
    LCObject fetched = student.fetch();
    log("用 objectId 创建了对象，并获取了数据：" + fetched);
  }

  public void testCreateObjectWithFile() throws IOException, LCException {
    LCFile avatar = new LCFile("avatar", getAvatarBytes());

    Student student = new Student();
    student.setName(getClassName());
    student.setAvatar(avatar);
    student.save();
    log("保存了文件，并把其作为一个字段保存到了对象。student: " + prettyJSON(student));
  }

  public void testObjectParse() throws Exception {
    Student student = getFirstStudent();
    String s = student.toString();
    log("将对象序列化成字符串：" + s);

    LCObject parseObject = LCObject.parseLCObject(s);
    log("从字符串中解析对象：" + parseObject);
  }

  public void testIncrement() throws LCException {
    Student student = getFirstStudent();
    log("生日前的年龄：%d", student.getAge());
    student.increment(Student.AGE, 1);
    student.save();
    log("生日了，年龄：%d", student.getAge());
  }

  public void testAnyType() throws LCException {
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

  public void testRemoveKey() throws LCException {
    Student student = getFirstStudent();
    log("名字：" + student.getName());

    student.remove(Student.NAME);
    student.save();
    log("将名字字段置为空后：", student.getName());
  }

  public void testArrayAddObject() throws LCException {
    Student student = getFirstStudent();
    log("添加前的爱好：" + student.getHobbies());
    List<String> hobbies = new ArrayList<>();
    hobbies.add("running");
    hobbies.add("fly");
    student.addAll(Student.HOBBIES, hobbies);
    student.save();
    log("添加了两个爱好, hobbies : " + student.getHobbies());
  }

  public void testArrayAddMutipleObjects() throws LCException {
    Student student = getFirstStudent();
    student.add(Student.HOBBIES, "swimming");
    student.save();
    log("添加了游泳爱好, hobbies : " + student.getHobbies());
  }

  public void testArrayRemoveObject() throws LCException {
    Student student = getFirstStudent();
    log("移除爱好前，hobbies = " + student.getHobbies());
    List<String> removeHobbies = new ArrayList<>();
    removeHobbies.add("swimming");
    student.removeAll(Student.HOBBIES, removeHobbies);
    student.save();
    log("移除爱好后, hobbies = " + student.getHobbies());
  }

  public void testArrayAddUnique() throws LCException {
    Student student = getFirstStudent();
    student.addUnique(Student.HOBBIES, "swimming");
    student.save();
    log("添加了游泳的爱好之后, hobbies: " + student.getHobbies());

    student.addUnique(Student.HOBBIES, "swimming");
    student.save();
    log("再次 addUnique 游泳爱好, hobbies:" + student.getHobbies());
  }

  public void testSaveAll() throws LCException {
    List<Student> students = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Student student = new Student();
      student.setName(i + "");
      student.setAge(i + 10);
      students.add(student);
    }
    LCObject.saveAll(students);

    log("保存了五个学生: " + prettyJSON(students));
  }

  public void testSaveAllWithFile() throws LCException {
    List<Student> students = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Student student = new Student();
      student.setName(i + "");
      LCFile avatar = new LCFile("avatar" + i, getAvatarBytes());
      student.setAvatar(avatar);
      students.add(student);
    }
    LCObject.saveAll(students);
    log("批量保存了一批学生及其头像，students:" + prettyJSON(students));
  }

  public void testBatchUpdate() throws LCException {
    List<Student> students = findStudents();
    for (Student student : students) {
      student.setName("testBatchUpdate");
    }
    LCObject.saveAll(students);
    log("批量更改了一批学生的名字，students:" + prettyJSON(students));
  }

  public void testDeleteAll() throws LCException {
    List<Student> students = findStudents();
    LCObject.deleteAll(students);

    log("删除掉了一批学生 " + prettyJSON(students));
  }

  // create an object and query it.
  public void testObjectSaveAndQuery() throws LCException {
    final String key = "array";
    final String objectTable = "ObjectDemoTableRead";
    final LCObject myObject = new LCObject(objectTable);
    for (int i = 0; i < 5; ++i) {
      myObject.add(key, i);
    }
    myObject.save();

    LCQuery<LCObject> query = LCQuery.getQuery(objectTable);
    LCObject result = query.get(myObject.getObjectId());
    List<Number> array = result.getList(key);
    if (array.size() != 5) {
      showMessage("", new LCException(LCException.OTHER_CAUSE, "incorrect result"), false);
    } else {
      showMessage("", null, false);
    }
  }

  public void testObjectCreateAndQuery() throws LCException {
    final String objectTable = "ObjectDemoTableCreate";
    final String key = "score";
    LCObject gameScore = new LCObject(objectTable);
    final int targetValue = new Random().nextInt();
    gameScore.put(key, targetValue);
    int value = gameScore.getInt(key);

    final String targetString = "Sean Plott";
    gameScore.put("playerName", targetString);
    String stringValue = gameScore.getString("playerName");
    gameScore.save();
  }

  // update an object
  public void testObjectUpdateAndQuery() throws LCException {
    final String key = "update";
    final String objectTable = "ObjectDemoTableUpdate";
    final LCObject myObject = new LCObject(objectTable);
    final String value = "anotherValue";
    myObject.put(key, "myValue");
    myObject.save();

    myObject.put(key, value);
    myObject.save();
    LCQuery<LCObject> query = LCQuery.getQuery(objectTable);
    LCObject result = query.get(myObject.getObjectId());
    String stringValue = (String) result.get(key);
    if (!value.equals(stringValue)) {
      showMessage("", new LCException(LCException.OTHER_CAUSE, "incorrect result"), false);
    } else {
      showMessage("", null, false);
    }
  }

  public void testObjectDeleteAndQuery() throws LCException {
    final String objectTable = "ObjectDemoTableDelete";
    final LCObject myObject = new LCObject(objectTable);
    myObject.save();
    myObject.delete();
    LCQuery<LCObject> query = LCQuery.getQuery(objectTable);
    LCObject result = query.get(myObject.getObjectId());
    if (result != null) {
      showMessage("", new LCException(LCException.OTHER_CAUSE, "delete failed"), false);
    } else {
      showMessage("", null, false);
    }
  }
}
