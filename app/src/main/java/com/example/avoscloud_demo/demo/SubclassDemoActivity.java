package com.example.avoscloud_demo.demo;

import android.os.Bundle;
import com.example.avoscloud_demo.*;

import cn.leancloud.LCException;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;

public class SubclassDemoActivity extends DemoBaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Armor.registerSubclass(Armor.class);
  }

  public void testSubUserSignup() throws LCException {
    showInputDialog("Sign up", new InputDialogListener() {
      @Override
      public void onAction(final String username, final String password) {
        new BackgroundTask() {
          @Override
          protected void doInBack() throws Exception {
            Armor armor = new Armor();
            armor.setDisplayName("avos cloud demo object.");
            armor.setBroken(false);
            armor.save();

            SubUser subUser = new SubUser();
            String nickName = "testSignupSubUser";
            subUser.setUsername(username);
            subUser.setPassword(password);
            subUser.setNickName(nickName);
            subUser.setArmor(armor);
            subUser.signUp();
            LCUser.logIn(username, password, SubUser.class);
          }

          @Override
          protected void onPost(Exception e) {

          }
        }.execute();
      }
    });
  }

//  public void testLogin() {
//    showInputDialog("Login", new InputDialogListener() {
//      @Override
//      public void onAction(String username, String password) {
//        SubUser.logInInBackground(username, password, new LogInCallback<LCUser>() {
//          @Override
//          public void done(LCUser LCUser, LCException e) {
//            LCUser currentUser = LCUser.getCurrentUser();
//            Assert.assertTrue(currentUser instanceof SubUser);
//          }
//        });
//      }
//    });
//  }


  public void testSubObject() throws Exception {
    Armor armor = new Armor();
    String displayName = "avos cloud subclass object.";
    armor.setDisplayName(displayName);
    armor.setBroken(false);
    armor.save();

    LCQuery<Armor> query = LCObject.getQuery(Armor.class);
    Armor result = query.get(armor.getObjectId());
    String value = result.getDisplayName();
  }
}
