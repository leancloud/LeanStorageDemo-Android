package com.example.avoscloud_demo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
  public static final String TAG_DEMO = "Demo";
  private TextView outputTextView;
  protected DemoRunActivity demoRunActivity;

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
  }

  public void setOutputTextView(TextView outputTextView) {
    this.outputTextView = outputTextView;
  }

  public void setDemoRunActivity(DemoRunActivity demoRunActivity) {
    this.demoRunActivity = demoRunActivity;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_demo_group, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_source) {
      showSourceCode();
    }
    return super.onOptionsItemSelected(item);
  }

  private String getFileSourceCode() {
    try {
      String name = this.getClass().getSimpleName() + ".file";
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
      if (outputTextView == null) {
        showMessage(text + " finished.");
      } else {
        log(text + " finished");
      }
    } else {
      if (outputTextView == null) {
        showMessage(e.toString());
      } else {
        log(e.toString());
      }
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


  protected void log(String format, @Nullable Object... objects) {
    final String msg = String.format(format, objects);
    Log.d(TAG_DEMO, msg);
    if (outputTextView != null) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          outputTextView.setText(outputTextView.getText() + "\n-------- RUN --------\n" + msg);
        }
      });
    }
  }

  public void run(final String methodName) {
//    setProgressBarIndeterminateVisibility(true);
    new BackgroundTask() {
      @Override
      protected void doInBack() throws Exception {
        Method method = DemoUtils.getMethodSafely(DemoBaseActivity.this.getClass(), methodName);
        DemoUtils.invokeMethod(DemoBaseActivity.this, method);
      }

      @Override
      protected void onPost(Exception e) {
        if (e != null) {
          log("Error : %s", e.toString());
        } else {
          log("%s Finished.", methodName);
        }
      }
    }.execute();
  }

  protected void showInputDialog(final String title, final InputDialogListener listener) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(demoRunActivity);
        LayoutInflater inflater = LayoutInflater.from(demoRunActivity);
        final LinearLayout layout = (LinearLayout) inflater.inflate(com.example.avoscloud_demo.R.layout.login_dialog, null);

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

  public interface InputDialogListener {
    void onAction(final String username, final String password);
  }

  protected String getClassName() {
    return this.getClass().getSimpleName();
  }
}
