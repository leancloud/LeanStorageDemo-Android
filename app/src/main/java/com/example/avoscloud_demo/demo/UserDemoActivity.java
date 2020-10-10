package com.example.avoscloud_demo.demo;
import com.example.avoscloud_demo.DemoBaseActivity;
import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import cn.leancloud.types.AVNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UserDemoActivity extends DemoBaseActivity {
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
    first.saveInBackground().subscribe(new Observer<AVObject>() {
      @Override
      public void onSubscribe(Disposable d) {
      }
      @Override
      public void onNext(AVObject avObject) {
      }
      @Override
      public void onError(Throwable e) {
        AVException avException = new AVException(e);
        if (avException.getCode() == AVException.SESSION_MISSING) {
          log("尝试修改未登录用户的数据，发生错误：" + e.getMessage());
        }
      }
      @Override
      public void onComplete() {
      }
    });
  }
  public void testUserSignUp() throws Exception {
    showInputDialog("Sign Up", new InputDialogListener() {
      @Override
      public void onAction(String username, String password) {
        AVUser.logOut();
        final AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground().subscribe(new Observer<AVUser>() {
          @Override
          public void onSubscribe(Disposable d) {
          }
          @Override
          public void onNext(AVUser avUser) {
            log("注册成功 uesr:" + avUser);
          }
          @Override
          public void onError(Throwable e) {
          }
          @Override
          public void onComplete() {
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
        AVUser.logIn(username,password).subscribe(new Observer<AVUser>() {
          @Override
          public void onSubscribe(Disposable d) {
          }
          @Override
          public void onNext(AVUser avUser) {
            log("登录成功 user：" + avUser.toString());
          }
          @Override
          public void onError(Throwable e) {
            log(e.getMessage());
          }
          @Override
          public void onComplete() {
          }
        });
      }
    });
  }
  public void testPhoneNumberAndCodeLogin() {
    showSimpleInputDialog("请输入手机号码来登录", new SimpleInputDialogListner() {
      @Override
      public void onConfirm(final String phone) {
        AVUser.requestLoginSmsCodeInBackground(phone).subscribe(new Observer<AVNull>() {
          @Override
          public void onSubscribe(Disposable d) {
          }
          @Override
          public void onNext(AVNull avNull) {
            showSimpleInputDialog("验证码已发送，请输入验证码", new SimpleInputDialogListner() {
              @Override
              public void onConfirm(String smsCode) {
                AVUser.signUpOrLoginByMobilePhoneInBackground(phone, smsCode).subscribe(new Observer<AVUser>() {
                  public void onSubscribe(Disposable disposable) {}
                  public void onNext(AVUser user) {
                    log("登录成功, user: " + user);
                  }
                  public void onError(Throwable throwable) {
                  }
                  public void onComplete() {}
                });
              }
            });
          }
          @Override
          public void onError(Throwable e) {
          }
          @Override
          public void onComplete() {
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
        user.signUpInBackground().subscribe(new Observer<AVUser>() {
          @Override
          public void onSubscribe(Disposable d) {
          }
          @Override
          public void onNext(AVUser avUser) {
            log("注册成功，user: " + avUser);
          }
          @Override
          public void onError(Throwable e) {
          }
          @Override
          public void onComplete() {
          }
        });
      }
    });
  }

  public void testEmailLogin() {
    showSimpleInputDialog("请输入邮箱来登录", new SimpleInputDialogListner() {
      @Override
      public void onConfirm(String text) {
        AVUser.loginByEmail(text, demoPassword).subscribe(new Observer<AVUser>() {
          public void onSubscribe(Disposable disposable) {
          }

          public void onNext(AVUser user) {
            // 登录成功
          }

          public void onError(Throwable throwable) {
            // 登录失败（可能是密码错误）
          }

          public void onComplete() {
          }
        });
      }
    });
  }
      public void testEmailResetPassword() {
        showSimpleInputDialog("请输入邮箱进行密码重置", new SimpleInputDialogListner() {
          @Override
          public void onConfirm(final String text) {
            AVUser.requestPasswordResetInBackground(text).blockingSubscribe();

          }
        });
      }

      public void testAnonymousUserLogin() {
        AVUser.logInAnonymously().subscribe(new Observer<AVUser>() {
          public void onSubscribe(Disposable disposable) {
          }

          public void onNext(AVUser user) {
            // user 是新的匿名用户
          }

          public void onError(Throwable throwable) {
          }

          public void onComplete() {
          }
        });
      }
    }