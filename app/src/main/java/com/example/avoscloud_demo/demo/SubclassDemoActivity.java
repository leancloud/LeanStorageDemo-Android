package com.example.avoscloud_demo.demo;

import android.os.Bundle;
import com.avos.avoscloud.*;
import com.example.avoscloud_demo.*;
import junit.framework.Assert;

import static junit.framework.Assert.assertFalse;

public class SubclassDemoActivity extends DemoBaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Armor.registerSubclass(Armor.class);
  }

  public void testSubUserSignup() throws AVException {
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
            Assert.assertFalse(armor.getObjectId().isEmpty());

            SubUser subUser = new SubUser();
            String nickName = "testSignupSubUser";
            subUser.setUsername(username);
            subUser.setPassword(password);
            subUser.setNickName(nickName);
            subUser.setArmor(armor);
            subUser.signUp();
            Assert.assertFalse(subUser.getObjectId().isEmpty());
            Assert.assertFalse(subUser.getSessionToken().isEmpty());

            SubUser cloudUser = AVUser.logIn(username, password, SubUser.class);
            Assert.assertTrue(cloudUser.getSessionToken() != null);
            Assert.assertEquals(cloudUser.getObjectId(), subUser.getObjectId());
            Assert.assertEquals(cloudUser.getSessionToken(), subUser.getSessionToken());
            Assert.assertEquals(username, cloudUser.getUsername());
            Assert.assertEquals(nickName, cloudUser.getNickName());
            Assert.assertNotNull(cloudUser.getArmor());
            AVUser currentUser = AVUser.getCurrentUser();
            Assert.assertTrue(currentUser instanceof SubUser);
          }

          @Override
          protected void onPost(Exception e) {

          }
        }.execute();
      }
    });
  }

  public void testLogin() {
    showInputDialog("Login", new InputDialogListener() {
      @Override
      public void onAction(String username, String password) {
        SubUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
          @Override
          public void done(AVUser avUser, AVException e) {
            AVUser currentUser = AVUser.getCurrentUser();
            Assert.assertTrue(currentUser instanceof SubUser);
          }
        });
      }
    });
  }


  public void testSubObject() throws Exception {
    Armor armor = new Armor();
    String displayName = "avos cloud subclass object.";
    armor.setDisplayName(displayName);
    armor.setBroken(false);
    armor.save();
    Assert.assertFalse(armor.getObjectId().isEmpty());

    AVQuery<Armor> query = AVObject.getQuery(Armor.class);
    Armor result = query.get(armor.getObjectId());
    Assert.assertTrue(result instanceof Armor);
    String value = result.getDisplayName();
    Assert.assertEquals(value, displayName);
  }
}
