package com.example.avoscloud_demo.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.avos.avoscloud.*;
import com.example.avoscloud_demo.*;
import com.example.avoscloud_demo.R;

public class UserDemoActivity extends DemoBaseActivity {

  private void signUpImpl(final String username, final String password) {
    AVUser.logOut();
    AVUser user = new AVUser();
    user.setUsername(username);
    user.setPassword(password);
    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(AVException e) {
        showMessage("", e, false);
      }
    });
  }

  private void loginImpl(final String username, final String password) {
    AVUser.logOut();
    AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
      @Override
      public void done(AVUser avUser, AVException e) {
        showMessage("", e, false);
      }
    });
  }

  public void testUserSignUp() throws Exception {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = LayoutInflater.from(this);
    LinearLayout layout = (LinearLayout) inflater.inflate(com.example.avoscloud_demo.R.layout.login_dialog, null);

    final EditText userNameET = (EditText) layout.findViewById(R.id.usernameInput);
    final EditText passwordET = (EditText) layout.findViewById(R.id.passwordInput);

    builder.setTitle("sign up").setPositiveButton(R.string.signup, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
        String username = userNameET.getText().toString();
        String password = passwordET.getText().toString();
        signUpImpl(username, password);
      }
    }).setNegativeButton(R.string.login, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        String username = userNameET.getText().toString();
        String password = passwordET.getText().toString();
        loginImpl(username, password);
      }
    });
    builder.setView(layout);
    AlertDialog ad = builder.create();
    ad.show();
  }


}
