package com.example.leancloud_demo.demo;
import com.example.leancloud_demo.DemoBaseActivity;
import cn.leancloud.LCException;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import cn.leancloud.types.LCNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UserDemoActivity extends DemoBaseActivity {
  private String demoPassword = "123456";

  public void testCurrentUser() {
    LCUser user = LCUser.getCurrentUser();
    if (user != null) {
      log("当前已登录的用户为：" + user);
    } else {
      log("当前未有用户登录");
    }
  }

  public void testLogOut() {
    LCUser user = LCUser.getCurrentUser();
    if (user != null) {
      log("当前用户为 " + user);
      LCUser.logOut();
      log("已注销当前用户");
    } else {
      promptLogin();
    }
  }

  void promptLogin() {
    log("当前用户为空，请运行登录的例子，登录一个用户");
  }

  public void testDeleteCurrentUser() throws LCException {
    LCUser user = LCUser.getCurrentUser();
    if (user != null) {
      user.delete();
      log("已删除当前用户");
    } else {
      promptLogin();
    }
  }

  public void testWriteOtherUserData() throws LCException {
    LCQuery<LCUser> q = LCUser.getQuery();
    LCUser first = q.getFirst();
    log("获取了一个用户，但未登录该用户");
    first.put("city", "ShangHai");
    first.saveInBackground().subscribe(new Observer<LCObject>() {
      @Override
      public void onSubscribe(Disposable d) {
      }
      @Override
      public void onNext(LCObject LCObject) {
      }
      @Override
      public void onError(Throwable e) {
        LCException LCException = new LCException(e);
        if (LCException.getCode() == LCException.SESSION_MISSING) {
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
        LCUser.logOut();
        final LCUser user = new LCUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground().subscribe(new Observer<LCUser>() {
          @Override
          public void onSubscribe(Disposable d) {
          }
          @Override
          public void onNext(LCUser LCUser) {
            log("注册成功 uesr:" + LCUser);
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
        LCUser.logOut();
        LCUser.logIn(username,password).subscribe(new Observer<LCUser>() {
          @Override
          public void onSubscribe(Disposable d) {
          }
          @Override
          public void onNext(LCUser LCUser) {
            log("登录成功 user：" + LCUser.toString());
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
        LCUser.requestLoginSmsCodeInBackground(phone).subscribe(new Observer<LCNull>() {
          @Override
          public void onSubscribe(Disposable d) {
          }
          @Override
          public void onNext(LCNull LCNull) {
            showSimpleInputDialog("验证码已发送，请输入验证码", new SimpleInputDialogListner() {
              @Override
              public void onConfirm(String smsCode) {
                LCUser.signUpOrLoginByMobilePhoneInBackground(phone, smsCode).subscribe(new Observer<LCUser>() {
                  public void onSubscribe(Disposable disposable) {}
                  public void onNext(LCUser user) {
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
        final LCUser user = new LCUser();
        user.setUsername(text);
        user.setPassword(demoPassword);
        user.setEmail(text);
        user.signUpInBackground().subscribe(new Observer<LCUser>() {
          @Override
          public void onSubscribe(Disposable d) {
          }
          @Override
          public void onNext(LCUser LCUser) {
            log("注册成功，user: " + LCUser);
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
        LCUser.loginByEmail(text, demoPassword).subscribe(new Observer<LCUser>() {
          public void onSubscribe(Disposable disposable) {
          }

          public void onNext(LCUser user) {
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
            LCUser.requestPasswordResetInBackground(text).blockingSubscribe();

          }
        });
      }

      public void testAnonymousUserLogin() {
        LCUser.logInAnonymously().subscribe(new Observer<LCUser>() {
          public void onSubscribe(Disposable disposable) {
          }

          public void onNext(LCUser user) {
            // user 是新的匿名用户
          }

          public void onError(Throwable throwable) {
          }

          public void onComplete() {
          }
        });
      }
    }