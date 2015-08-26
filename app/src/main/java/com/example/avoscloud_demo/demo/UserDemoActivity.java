package com.example.avoscloud_demo.demo;

import com.avos.avoscloud.AVAnonymousUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.DemoUtils;

public class UserDemoActivity extends DemoBaseActivity {
  private String demoUsername = "lzwjava";
  private String demoPassword = "123456";

  public void testCurrentUser() {
    AVUser user = AVUser.getCurrentUser();
    if (user != null) {
      log("当前已登录的用户为：" + user);
    } else {
      log("当前未有用户登录");
    }
  }

  public void testLogOut() {
    AVUser user = AVUser.getCurrentUser();
    if (user != null) {
      log("当前用户为 " + user);
      AVUser.logOut();
      log("已注销当前用户");
    } else {
      promptLogin();
    }
  }

  void promptLogin() {
    log("当前用户为空，请运行登录的例子，登录一个用户");
  }

  public void testDeleteCurrentUser() throws AVException {
    AVUser user = AVUser.getCurrentUser();
    if (user != null) {
      user.delete();
      log("已删除当前用户");
    } else {
      promptLogin();
    }
  }

  public void testWriteOtherUserData() throws AVException {
    AVQuery<AVUser> q = AVUser.getQuery();
    AVUser first = q.getFirst();
    log("获取了一个用户，但未登录该用户");
    first.put("city", "ShangHai");
    try {
      first.save();
    } catch (AVException e) {
      if (e.getCode() == AVException.SESSION_MISSING) {
        log("尝试修改未登录用户的数据，发生错误：" + e.getMessage());
      } else {
        throw e;
      }
    }
    log("结论：不能修改未登录用户的数据");
  }

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

  public void testOldPasswordUpdatePassword() throws AVException {
    AVUser user = AVUser.getCurrentUser();
    if (user == null) {
      promptLogin();
      return;
    }
    String newPassword = "1111111";
    user.updatePassword(demoPassword, newPassword);
    log("重置密码成功，新密码为 " + newPassword);
    logThreadTips();
  }

  public void testPhoneNumberRegister() {
    // 请在网站勾选 "验证注册用户手机号码" 选项，否则不会发送验证短信
    showSimpleInputDialog("请输入手机号码来注册", new SimpleInputDialogListner() {
      @Override
      public void onConfirm(String text) {
        final AVUser user = new AVUser();
        user.setUsername(DemoUtils.getRandomString(6));
        user.setPassword(demoPassword);
        user.setMobilePhoneNumber(text);
        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(AVException e) {
            if (filterException(e)) {
              showSimpleInputDialog("验证短信已发送，请输入验证码", new SimpleInputDialogListner() {
                @Override
                public void onConfirm(String code) {
                  AVUser.verifyMobilePhoneInBackground(code, new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                      if (filterException(e)) {
                        log("注册成功, user:" + user);
                      }
                    }
                  });
                }
              });
            }
          }
        });
      }
    });
  }

  public void testPhoneNumberAndPasswordLogin() {
    showSimpleInputDialog("请输入手机号码来登录", new SimpleInputDialogListner() {
      @Override
      public void onConfirm(String text) {
        AVUser.loginByMobilePhoneNumberInBackground(text, demoPassword, new LogInCallback<AVUser>() {
          @Override
          public void done(AVUser avUser, AVException e) {
            if (filterException(e)) {
              log("登录成功, user:" + avUser);
            }
          }
        });
      }
    });
  }

  public void testPhoneNumberAndCodeLogin() {
    showSimpleInputDialog("请输入手机号码来登录", new SimpleInputDialogListner() {
      @Override
      public void onConfirm(final String phone) {
        AVUser.requestLoginSmsCodeInBackground(phone, new RequestMobileCodeCallback() {
          @Override
          public void done(AVException e) {
            if (filterException(e)) {
              showSimpleInputDialog("验证码已发送，请输入验证码", new SimpleInputDialogListner() {
                @Override
                public void onConfirm(String smsCode) {
                  AVUser.loginBySMSCodeInBackground(phone, smsCode, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                      if (filterException(e)) {
                        log("登录成功, user: " + avUser);
                      }
                    }
                  });
                }
              });
            }
          }
        });
      }
    });
  }

  public void testPhoneNumberResetPassword() {
    showSimpleInputDialog("请输入需要重置密码的手机号", new SimpleInputDialogListner() {
      @Override
      public void onConfirm(final String phone) {
        AVUser.requestPasswordResetBySmsCodeInBackground(phone, new RequestMobileCodeCallback() {
          @Override
          public void done(AVException e) {
            if (filterException(e)) {
              showSimpleInputDialog("短信已发送，请输入验证码来重置密码", new SimpleInputDialogListner() {
                @Override
                public void onConfirm(String smsCode) {
                  final String newPassword = "abcdefg";
                  AVUser.resetPasswordBySmsCodeInBackground(smsCode, newPassword, new UpdatePasswordCallback() {
                    @Override
                    public void done(AVException e) {
                      if (filterException(e)) {
                        log("密码更改成功，新密码 " + newPassword);
                        log("试着用手机号和新密码登录吧");
                      }
                    }
                  });
                }
              });
            }
          }
        });
      }
    });
  }

  public void testEmailRegister() {
    log("请确认控制台已开启注册时开启邮箱验证，这样才能收到验证邮件");
    showSimpleInputDialog("请输入您的邮箱来注册", new SimpleInputDialogListner() {
      @Override
      public void onConfirm(String text) {
        final AVUser user = new AVUser();
        user.setUsername(text);
        user.setPassword(demoPassword);
        user.setEmail(text);
        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(AVException e) {
            if (filterException(e)) {
              log("注册成功，user: " + user);
            }
          }
        });
      }
    });
  }

  public void testEmailLogin() {
    showSimpleInputDialog("请输入邮箱来登录", new SimpleInputDialogListner() {
      @Override
      public void onConfirm(String text) {
        AVUser.logInInBackground(text, demoPassword, new LogInCallback<AVUser>() {
          @Override
          public void done(AVUser avUser, AVException e) {
            if (filterException(e)) {
              log("登录成功 user:" + avUser);
            }
          }
        });
      }
    });
  }

  public void testEmailResetPassword() {
    showSimpleInputDialog("请输入邮箱进行密码重置", new SimpleInputDialogListner() {
      @Override
      public void onConfirm(final String text) {
        AVUser.requestPasswordResetInBackground(text, new RequestPasswordResetCallback() {
          @Override
          public void done(AVException e) {
            if (filterException(e)) {
              log("重置密码的邮件已发送到邮箱 " + text);
            }
          }
        });
      }
    });
  }

  public void testAnonymousUserLogin() {
    AVAnonymousUtils.logIn(new LogInCallback<AVUser>() {
      @Override
      public void done(AVUser avUser, AVException e) {
        if (filterException(e)) {
          log("创建了一个匿名用户并登录，user:" + avUser);
        }
      }
    });
  }
}
