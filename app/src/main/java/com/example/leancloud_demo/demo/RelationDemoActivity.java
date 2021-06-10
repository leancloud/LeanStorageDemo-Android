package com.example.leancloud_demo.demo;

import com.example.leancloud_demo.DemoBaseActivity;
import com.example.leancloud_demo.Post;
import com.example.leancloud_demo.Student;

import java.util.List;

import cn.leancloud.LCException;
import cn.leancloud.LCQuery;
import cn.leancloud.LCRelation;

/**
 * Created by lzw on 15/8/27.
 */
public class RelationDemoActivity extends DemoBaseActivity {
  private Student testRewardStudent;
  private Post testRewardPost;

  Post getFirstPost() throws LCException {
    LCQuery<Post> query = LCQuery.getQuery(Post.class);
    Post first = query.getFirst();
    if (first == null) {
      log("请在 PointerDemoActivity 中运行创建 Post 的例子");
    }
    return first;
  }

  public void testRelationAddObject() throws LCException {
    Post post = getFirstPost();
    Student student = getFirstStudent();

    LCRelation<Student> rewards = post.getRewardStudents();
    rewards.add(student);
    post.save();
    log("向 Relation 字段添加对象成功，post:" + prettyJSON(post));

    testRewardStudent = student;
    testRewardPost = post;
  }

  public void testRelationRemoveObject() throws LCException {
    if (testRewardPost == null) {
      log("请先运行 RelationAddObject");
      return;
    }
    Post post = testRewardPost;
    Student student = getFirstStudent();

    LCRelation<Student> rewardStudents = post.getRewardStudents();
    rewardStudents.remove(student);
    post.save();

    fastLog("从 Relation 中移除对象成功, post:", prettyJSON(post));
  }

  public void testGetRelationObject() throws LCException {
    if (testRewardPost == null) {
      log("请先运行 RelationAddObject");
      return;
    }

    LCRelation<Student> rewardStudents = testRewardPost.getRewardStudents();
    LCQuery<Student> relationQuery = rewardStudents.getQuery();
    List<Student> students = relationQuery.find();
    log("找回了 Relation 中的对象：" + prettyJSON(students));
  }

  public void testRelationCount() throws LCException {
    Post post = testRewardPost;
    if (post == null) {
      log("请先运行 RelationAddObject");
      return;
    }
    LCRelation<Student> rewardStudents = post.getRewardStudents();
    LCQuery<Student> relationQuery = rewardStudents.getQuery();
    int count = relationQuery.count();
    log("打赏的人数有%d人", count);
  }

  public void testRelationReverseQuery() throws LCException {
    if (testRewardStudent == null) {
      log("请先运行 RelationAddObject");
      return;
    }
    LCQuery<Post> postQuery = LCRelation.reverseQuery(Post.class, Post.REWARDS, testRewardStudent);
    List<Post> posts = postQuery.find();
    log(testRewardStudent.getName() + "打赏了这些帖子：" + prettyJSON(posts));
  }
}
