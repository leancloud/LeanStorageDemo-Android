package com.example.leancloud_demo.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.example.leancloud_demo.DemoBaseActivity;
import com.example.leancloud_demo.R;

import java.util.HashMap;
import java.util.Map;

import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by fengjunwen on 2018/1/26.
 */

public class UserAuthDataDemoActivity extends DemoBaseActivity {
  public interface InputDialogListener {
    void onAction(final String username, final String password);
  }
  protected void showInputDialog(final String title, final InputDialogListener listener) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(demoRunActivity);
        LayoutInflater inflater = LayoutInflater.from(demoRunActivity);
        final LinearLayout layout = (LinearLayout) inflater.inflate(com.example.leancloud_demo.R.layout.login_dialog, null);

        final EditText userNameET = (EditText) layout.findViewById(R.id.usernameInput);
        final EditText passwordET = (EditText) layout.findViewById(R.id.passwordInput);
        builder.setView(layout);
        builder.setTitle(title).setPositiveButton(title, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            String username = userNameET.getText().toString();
            String password = passwordET.getText().toString();
            if (listener != null && username.length() > 0 && password.length() > 0) {
              listener.onAction(username, password);
            }
          }
        });
        AlertDialog ad = builder.create();
        ad.show();
      }
    });
  }
  public void testLoginWithAuthData() {
    Map<String, Object> authData = new HashMap<String, Object>();
    authData.put("expires_at", "2019-01-07T02:41:13.580Z");
    authData.put("openid", "6A83158");
    authData.put("access_token", "DCIF");
    authData.put("platform", "weixin");
    LCUser.loginWithAuthData(authData, "weixin_darenbangbang").subscribe(new Observer<LCUser>() {
      @Override
      public void onSubscribe(Disposable d) {
      }
      @Override
      public void onNext(LCUser LCUser) {
        log("成功登录，当前用户：" + LCUser);
      }
      @Override
      public void onError(Throwable e) {
        log("尝试使用第三方账号登录，发生错误。cause：" + e.getMessage());
      }
      @Override
      public void onComplete() {
      }
    });
  }
  public void testAssociateWithAuthData() {
    showInputDialog("Sign Up", new InputDialogListener(){
      public void onAction(final String username, final String password) {
        final LCUser user = new LCUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground().subscribe(new Observer<LCUser>() {
          @Override
          public void onSubscribe(Disposable d) {
          }
          @Override
          public void onNext(LCUser LCUser) {
            log("注册成功 uesr: " + LCUser.getObjectId());
            final Map<String, Object> authData = new HashMap<String, Object>();
            authData.put("expires_at", "2019-01-07T02:41:13.580Z");
            authData.put("openid", "6A83faefewfew158");
            authData.put("access_token", "DCfafewerEWDWIF");
            authData.put("platform", "weixin");
            LCUser.associateWithAuthData(authData, "weixin_darenbangbang").subscribe(new Observer<LCUser>() {
              @Override
              public void onSubscribe(Disposable d) {
              }
              @Override
              public void onNext(LCUser LCUser) {
                log("第三方信息关联成功");
              }
              @Override
              public void onError(Throwable e) {
                log("第三方信息关联失败。 cause：" + e.getMessage());
              }
              @Override
              public void onComplete() {
              }
            });
          }
          @Override
          public void onError(Throwable e) {
            log("用户注册失败。 cause：" + e.getMessage());
          }
          @Override
          public void onComplete() {
          }
        });
      }
    });
  }
  public void testAssociateWithAuthDataEx() {
    showInputDialog("Sign Up", new InputDialogListener(){
      public void onAction(final String username, final String password) {
        final LCUser user = new LCUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground().subscribe(new Observer<LCUser>() {
          @Override
          public void onSubscribe(Disposable d) {
          }
          @Override
          public void onNext(LCUser LCUser) {
            log("注册成功 uesr: " + LCUser.getObjectId());
            final Map<String, Object> authData = new HashMap<String, Object>();
            authData.put("expires_at", "2019-01-07T02:41:13.580Z");
            authData.put("openid", "6A83faefewfew158");
            authData.put("access_token", "DCfafewerEWDWIF");
            authData.put("platform", "weixin");
            LCUser.associateWithAuthData(authData, "weixin_darenbangbang",
                    "ThisisAUnionIDXXX", "weixin", false).subscribe(new Observer<LCUser>() {
              @Override
              public void onSubscribe(Disposable d) {
              }
              @Override
              public void onNext(LCUser LCUser) {
                log("第三方信息关联成功");
              }
              @Override
              public void onError(Throwable e) {
                log("第三方信息关联失败。 cause：" + e.getMessage());
              }
              @Override
              public void onComplete() {
              }
            });
          }
          @Override
          public void onError(Throwable e) {
            log("用户注册失败。 cause：" + e.getMessage());
          }
          @Override
          public void onComplete() {
          }
        });
      }
    });
  }
}
