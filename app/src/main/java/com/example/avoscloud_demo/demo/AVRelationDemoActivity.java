package com.example.avoscloud_demo.demo;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVRole;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.Post;
import com.example.avoscloud_demo.Student;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lzw on 15/8/27.
 */
public class AVRelationDemoActivity extends DemoBaseActivity {
  private Student testRewardStudent;
  private Post testRewardPost;

  Post getFirstPost() throws AVException {
    AVQuery<Post> query = AVQuery.getQuery(Post.class);
    Post first = query.getFirst();
    if (first == null) {
      log("请在 PointerDemoActivity 中运行创建 Post 的例子");
    }
    return first;
  }

  public void testRoleRelation() throws Exception {
    final CountDownLatch latch = new CountDownLatch(1);
    AVQuery<AVRole> roleQuery=new AVQuery<AVRole>("_Role");
    roleQuery.whereEqualTo("name", "CTO");
    roleQuery.findInBackground(new FindCallback<AVRole>() {
      @Override
      public void done(List<AVRole> list, AVException e) {
        System.out.println("get target roles.");
        AVRole administrator = list.get(0);
        AVRelation userRelation = administrator.getUsers();
        System.out.println(JSON.toJSONString(userRelation));
        AVQuery<AVUser> query = userRelation.getQuery(AVUser.class);
        query.findInBackground(new FindCallback<AVUser>() {
          @Override
          public void done(List<AVUser> list, AVException e) {
            // list 就是拥有该角色权限的所有用户了。
            System.out.println(list);
            latch.countDown();
          }
        });
      }
    });
    latch.await();
  }

  public void testRelationAddObject() throws AVException {
    Post post = getFirstPost();
    Student student = getFirstStudent();

    AVRelation<Student> rewards = post.getRewardStudents();
    rewards.add(student);
    post.save();
    log("向 Relation 字段添加对象成功，post:" + prettyJSON(post));

    testRewardStudent = student;
    testRewardPost = post;
  }

  public void testRelationRemoveObject() throws AVException {
    if (testRewardPost == null) {
      log("请先运行 RelationAddObject");
      return;
    }
    Post post = testRewardPost;
    Student student = getFirstStudent();

    AVRelation<Student> rewardStudents = post.getRewardStudents();
    rewardStudents.remove(student);
    post.save();

    fastLog("从 Relation 中移除对象成功, post:", prettyJSON(post));
  }

  public void testGetRelationObject() throws AVException {
    if (testRewardPost == null) {
      log("请先运行 RelationAddObject");
      return;
    }

    AVRelation<Student> rewardStudents = testRewardPost.getRewardStudents();
    AVQuery<Student> relationQuery = rewardStudents.getQuery();
    List<Student> students = relationQuery.find();
    log("找回了 Relation 中的对象：" + prettyJSON(students));
  }

  public void testRelationCount() throws AVException {
    Post post = testRewardPost;
    if (post == null) {
      log("请先运行 RelationAddObject");
      return;
    }
    AVRelation<Student> rewardStudents = post.getRewardStudents();
    AVQuery<Student> relationQuery = rewardStudents.getQuery();
    int count = relationQuery.count();
    log("打赏的人数有%d人", count);
  }

  public void testRelationReverseQuery() throws AVException {
    if (testRewardStudent == null) {
      log("请先运行 RelationAddObject");
      return;
    }
    AVQuery<Post> postQuery = AVRelation.reverseQuery(Post.class, Post.REWARDS, testRewardStudent);
    List<Post> posts = postQuery.find();
    log(testRewardStudent.getName() + "打赏了这些帖子：" + prettyJSON(posts));
  }
}
