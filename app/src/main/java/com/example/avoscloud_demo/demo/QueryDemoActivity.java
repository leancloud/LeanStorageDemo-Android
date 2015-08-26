package com.example.avoscloud_demo.demo;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.DemoUtils;
import com.example.avoscloud_demo.Student;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryDemoActivity extends DemoBaseActivity {

  public void testBasicQuery() throws AVException {
    AVQuery<AVObject> query = new AVQuery<>("Person");
    List<AVObject> persons = query.find();
    log("找回了一组 Person:" + persons);
  }

  public void testGetFirstObject() throws AVException {
    AVQuery<AVObject> query = new AVQuery<>("Person");
    AVObject first = query.getFirst();
    log("找回了最近更新的第一个 Person" + first);
  }

  public void testLimit() throws AVException {
    AVQuery<AVObject> query = new AVQuery("Person");
    query.limit(2);
    List<AVObject> persons = query.find();
    log("找回了两个 Person:" + persons);
  }

  public void testSkip() throws AVException {
    AVQuery<AVObject> query = new AVQuery<>("Person");
    query.orderByDescending("createdAt");
    query.skip(3);
    AVObject first = query.getFirst();
    log("找回了倒数第四个创建的 Person:" + first);
  }

  public void testAndQuery() throws AVException {
    AVQuery<Student> query = AVQuery.getQuery(Student.class);
    query.whereNotEqualTo(Student.NAME, "Mike");

    // 默认就是 And
    query.whereStartsWith(Student.NAME, "M");

    List<Student> students = query.find();
    log("名字不是 Mike 但 M 开头的学生：" + students);
  }

  public void testOrQuery() throws AVException {
    AVQuery<Student> query1 = AVQuery.getQuery(Student.class);
    query1.whereEqualTo(Student.NAME, "Mike");

    AVQuery<Student> query2 = AVQuery.getQuery(Student.class);
    query2.whereStartsWith(Student.NAME, "J");

    List<AVQuery<Student>> queries = new ArrayList<>();
    queries.add(query1);
    queries.add(query2);

    AVQuery<Student> query = AVQuery.or(queries);
    List<Student> students = query.find();
    log("名字是 Mike 且 J 开头的学生：" + students);
  }

  public void testAscending() throws AVException {
    AVQuery<Student> query = AVQuery.getQuery(Student.class);
    query.orderByAscending(Student.CREATED_AT)
        .limit(5);
    List<Student> students = query.find();
    log("找出了5个最早创建的学生");
    logValues(students, Student.CREATED_AT);
  }

  public void testSecondOrder() throws AVException {
    AVQuery<Student> query = AVQuery.getQuery(Student.class);
    query.orderByDescending(Student.NAME)
        .addDescendingOrder(Student.AGE)
        .limit(5);
    List<Student> students = query.find();
    log("找回了名字排序靠后，年龄最大的五个学生 ");
    logValues(students, Student.NAME);
    logValues(students, Student.AGE);
  }

  public void testArraySize() throws AVException {
    AVQuery<Student> query = AVQuery.getQuery(Student.class);
    query.whereSizeEqual(Student.HOBBIES, 2)
        .limit(10);
    List<Student> students = query.find();
    log("找回了爱好有两个的学生：");
    logValues(students, Student.HOBBIES);
  }

  public void testContainedIn() throws AVException {
    AVQuery<Student> query = AVQuery.getQuery(Student.class);
    query.whereContainedIn(Student.NAME, Arrays.asList("Mike", "Jane"));
    List<Student> students = query.find();
    log("找回了名字是 Mike 或 Jane 的学生");
    logValues(students, Student.NAME);
  }

  public void testContainAll() throws AVException {
    AVQuery<Student> query = AVQuery.getQuery(Student.class);
    query.whereContainsAll(Student.HOBBIES, Arrays.asList("swimming", "running"));
    List<Student> students = query.find();
    log("找回了爱好至少有 swimming 和 running 的学生：");
    logValues(students, Student.HOBBIES);
  }

  public void testLimitSize() throws AVException {
    AVQuery<Student> query = AVQuery.getQuery(Student.class);
    // 最大 1000，默认 100
    query.limit(1000);
    List<Student> students = query.find();
    log("找回了最多 1000 个学生，实际上有 %d 个", students.size());
  }

  public void testRegex() throws AVException {
    AVQuery<Student> query = AVQuery.getQuery(Student.class);
    query.whereMatches(Student.NAME, "^M.*");
    List<Student> students = query.find();
    log("名字满足正则表达式 ^M.* 的学生：");
    logValues(students, Student.NAME);
  }

  public void testOneKeyMultipleCondition() throws AVException {
    AVQuery<Student> query = AVQuery.getQuery(Student.class);
    query.whereStartsWith(Student.NAME, "M")
        .whereEndsWith(Student.NAME, "e")
        .whereContains(Student.NAME, "i");
    List<Student> students = query.find();
    log("名字以 M 开头、e 结尾、含有 i 的学生：");
    logValues(students, Student.NAME);
  }

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
