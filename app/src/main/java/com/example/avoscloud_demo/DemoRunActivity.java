package com.example.avoscloud_demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.lang.reflect.Method;

public class DemoRunActivity extends Activity {
  public static DemoBaseActivity demoActivity;
  private String methodName;
  WebView webView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_demo_run);
    webView = (WebView) findViewById(R.id.source_code_view);
    String code = getIntent().getStringExtra(DemoBaseActivity.CONTENT_TAG);
    DemoUtils.loadCodeAtWebView(this, code, webView);

    methodName = getIntent().getStringExtra(DemoBaseActivity.METHOD_TAG);
  }

  public void run() {
    Method method = DemoUtils.getMethodSafely(demoActivity.getClass(), methodName, String.class);
//    setProgressBarIndeterminateVisibility(true);
    try {
      DemoUtils.invokeMethod(demoActivity, method, method.getName());
    } catch (Exception exception) {
      /*showMessage(null, exception, false);*/
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_demo_run, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      run();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
