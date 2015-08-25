package com.example.avoscloud_demo.demo;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.avoscloud_demo.DemoBaseActivity;

public class UserDemoActivity extends DemoBaseActivity {

  public void testUserSignUp() throws Exception {
    showInputDialog("Sign Up", new InputDialogListener() {
      @Override
      public void onAction(String username, String password) {
        AVUser.logOut();
        final AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(AVException e) {
            log("注册成功 uesr:" + user);
          }
        });
      }
    });
  }


  public void testLogin() {
    showInputDialog("Login", new InputDialogListener() {
      @Override
      public void onAction(String username, String password) {
        AVUser.logOut();
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
          @Override
          public void done(AVUser avUser, AVException e) {
            if (e != null) {
              log(e.getMessage());
            } else {
              log("登录成功 user：" + avUser.toString());
            }
          }
        });
      }
    });
  }
}
