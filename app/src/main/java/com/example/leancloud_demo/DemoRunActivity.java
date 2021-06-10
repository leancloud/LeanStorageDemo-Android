package com.example.leancloud_demo;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class DemoRunActivity extends Activity {
  public static DemoBaseActivity demoActivity;
  private String methodName;
  WebView webView;
  TextView outputTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setContentView(R.layout.activity_demo_run);

    webView = (WebView) findViewById(R.id.source_code_view);
    outputTextView = (TextView) findViewById(R.id.output_view);
    outputTextView.setMovementMethod(new ScrollingMovementMethod());
    demoActivity.setOutputTextView(outputTextView);
    demoActivity.setDemoRunActivity(this);

    String code = getIntent().getStringExtra(DemoBaseActivity.CONTENT_TAG);
    DemoUtils.loadCodeAtWebView(this, code, webView);

    methodName = getIntent().getStringExtra(DemoBaseActivity.METHOD_TAG);

    setTitle(methodName);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    demoActivity.setOutputTextView(null);
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
    if (id == R.id.action_run) {
      demoActivity.runMethod(this, methodName);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
