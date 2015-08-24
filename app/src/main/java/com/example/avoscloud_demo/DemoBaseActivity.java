package com.example.avoscloud_demo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DemoBaseActivity extends ListActivity {

  static public final String CONTENT_TAG = "content";
  public static final String METHOD_TAG = "method";

  private List<String> codeSnippetList = new ArrayList<String>();

  public List<String> myTestList() {
    if (codeSnippetList.isEmpty()) {
      codeSnippetList.addAll(methodsWithPrefix("test"));
    }
    return codeSnippetList;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setContentView(R.layout.demo_base);
    setupAdapter();
    setupButtonHandlers();
  }


  private void setupButtonHandlers() {
    Button button = (Button) findViewById(R.id.btn_show_source);
    if (button != null) {
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          showSourceCode();
        }
      });
    }
  }

  private String getFileSourceCode() {
    try {
      String name = this.getClass().getSimpleName() + ".java";
      InputStream inputStream = getAssets().open(name);
      String content = DemoUtils.readTextFile(inputStream);
      return content;
    } catch (Exception e) {
      showMessage(e.getMessage());
    }
    return null;
  }

  private void showSourceCode() {
    try {
      startSourceCodeActivity(getFileSourceCode());
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private String getMethodSourceCode(String name) {
    String code = getFileSourceCode();
    String method = null;
    String patternString = String.format
        ("void\\s%s.*?\\{(.*?)\\n\\s\\s\\}\\n", name);
    Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
    Matcher matcher = pattern.matcher(code);
    if (matcher.find()) {
      method = matcher.group(1);
//      method = matcher.group(1);
    }
    return method;
  }

  public void setupAdapter() {
    List<String> array = myTestList();
    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
        android.R.layout.simple_list_item_1,
        array);
    setListAdapter(adapter);
  }

  private void startSourceCodeActivity(final String content) {
    try {
      Intent intent = new Intent(this, SourceCodeActivity.class);
      intent.putExtra(CONTENT_TAG, content);
      startActivity(intent);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public void showMessage(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  public void showMessage(final String text, Exception e, boolean busy) {
    if (e == null) {
      showMessage(text + " finished.");
    } else {
      showMessage(e.toString());
    }
    if (!busy) {
      setProgressBarIndeterminateVisibility(false);
    }
  }

  public List<String> methodsWithPrefix(final String prefix) {
    List<String> methods = new ArrayList<String>();
    try {
      Class c = this.getClass();
      Method m[] = c.getDeclaredMethods();
      for (int i = 0; i < m.length; i++) {
        if (m[i].getName().startsWith(prefix)) {
          methods.add(m[i].getName());
        }
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return methods;
  }

  protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
    Intent intent = new Intent(this, DemoRunActivity.class);
    DemoRunActivity.demoActivity = this;
    List<String> array = myTestList();
    String name = array.get(position);
    intent.putExtra(CONTENT_TAG, getMethodSourceCode(name));
    intent.putExtra(METHOD_TAG, name);
    startActivity(intent);
  }


  public boolean isBlankString(final String string) {
    if (string == null || string.trim().isEmpty()) {
      return true;
    }
    return false;
  }
}
