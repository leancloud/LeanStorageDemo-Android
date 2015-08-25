package com.example.avoscloud_demo.demo;

import android.app.Dialog;
import android.widget.Toast;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import com.avos.avoscloud.*;
import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.DemoUtils;

import java.io.File;
import java.io.RandomAccessFile;

public class FileDemoActivity extends DemoBaseActivity {

  private String fileUrl = null;
  private String objectId = null;

  private byte[] readFile(File file) {
    RandomAccessFile rf = null;
    byte[] data = null;
    try {
      rf = new RandomAccessFile(file, "r");
      int length = (int) rf.length();
      if (length >= 5 * 1024 * 1024) {
        return null;
      }
      data = new byte[length];
      rf.readFully(data);
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      DemoUtils.closeQuietly(rf);
    }
    return data;
  }

  public void testFileUpload() throws AVException {
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.show();
    dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
      public void onFileSelected(Dialog source, File file) {
        source.hide();
        byte[] data = readFile(file);
        if (data == null) {
          Toast.makeText(FileDemoActivity.this, "File is too big to upload.", Toast.LENGTH_LONG).show();
          return;
        }
        final AVFile avFile = new AVFile(file.getName(), data);
        avFile.saveInBackground(new SaveCallback() {
          @Override
          public void done(AVException e) {
            FileDemoActivity.this.showMessage("", e, false);
            if (e == null) {
              fileUrl = avFile.getUrl();
              objectId = avFile.getObjectId();
            }
            setProgressBarIndeterminateVisibility(false);
          }
        }, new ProgressCallback() {
          @Override
          public void done(Integer percentDone) {
            LogUtil.log.d("uploading: " + percentDone);
          }
        });
      }

      public void onFileSelected(Dialog source, File folder, String name) {
      }
    });
  }

  // create an object and query it.
  public void testFileDownload() throws AVException {
    if (DemoUtils.isBlankString(fileUrl)) {
      showMessage("Please upload file at first.", null, false);
      return;
    }
    AVFile avFile = new AVFile("my_download_file", fileUrl, null);
    byte[] bytes = avFile.getData();
  }

  // update an object
  public void testFileDelete() throws Exception {
    if (DemoUtils.isBlankString(objectId)) {
      showMessage("Please upload file at first.", null, false);
      return;
    }
    AVFile avFile = AVFile.withObjectId(objectId);
    avFile.delete();
  }
}
