package com.example.avoscloud_demo.demo;

import android.app.Dialog;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import com.avos.avoscloud.*;
import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.DemoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDemoActivity extends DemoBaseActivity {

  private String fileUrl = null;
  private String objectId = null;

  interface SelectFileCallback {
    void onFileSelect(File file);
  }

  private void selectFile(final SelectFileCallback callback) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        final FileChooserDialog dialog = new FileChooserDialog(getRunningContext());
        dialog.show();
        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
          @Override
          public void onFileSelected(Dialog source, File file) {
            if (callback != null) {
              callback.onFileSelect(file);
            }
            source.dismiss();
          }

          @Override
          public void onFileSelected(Dialog source, File folder, String name) {

          }
        });
      }
    });
  }

  public void testFileUpload() throws AVException {
    selectFile(new SelectFileCallback() {
      @Override
      public void onFileSelect(File file) {
        byte[] data = DemoUtils.readFile(file);
        final AVFile avFile = new AVFile(file.getName(), data);
        avFile.saveInBackground(new SaveCallback() {
          @Override
          public void done(AVException e) {
            if (e == null) {
              fileUrl = avFile.getUrl();
              objectId = avFile.getObjectId();
              log("文件上传成功 url:" + fileUrl);
            } else {
              log(e.getMessage());
            }
          }
        }, new ProgressCallback() {
          @Override
          public void done(Integer percentDone) {
            log("uploading: " + percentDone);
          }
        });
      }
    });
  }

  // create an object and query it.
  public void testFileDownload() throws AVException {
    if (DemoUtils.isBlankString(fileUrl)) {
      log("Please upload file at first.");
      return;
    }
    AVFile avFile = new AVFile("my_download_file", fileUrl, null);
    byte[] bytes = avFile.getData();
    log("下载文件完毕，总字节数：" + bytes.length);
  }

  // 需要控制台开启权限
  public void testFileDelete() throws Exception {
    if (DemoUtils.isBlankString(objectId)) {
      log("Please upload file at first.");
      return;
    }
    AVFile avFile = AVFile.withObjectId(objectId);
    avFile.delete();
    log("删除成功，被删掉的文件的 objectId 为 " + objectId);
  }

  public void testCreateFileFromBytes() throws AVException {
    AVFile file = new AVFile("testCreateFileFromBytes", getAvatarBytes());
    file.save();
    log("从 bytes 中创建了文件 file:" + toString(file));
  }

  private File createCacheFile(String name) throws IOException {
    File tmpFile = new File(getCacheDir(), name);
    byte[] bytes = "hello world".getBytes();
    FileOutputStream outputStream = new FileOutputStream(tmpFile);
    outputStream.write(bytes, 0, bytes.length);
    outputStream.close();
    return tmpFile;
  }

  public void testCreateFileFromPath() throws IOException, AVException {
    File tmpFile = createCacheFile("testCreateFileFromPath");

    AVFile file = AVFile.withAbsoluteLocalPath("testCreateFileFromPath", tmpFile.getAbsolutePath());
    file.save();
    log("从文件的路径中构造了 AVFile，并保存成功。file:" + toString(file));
  }

  public void testCreateAVFileFromFile() throws IOException, AVException {
    File tmpFile = createCacheFile("testCreateAVFileFromFile");

    AVFile file = AVFile.withFile("testCreateAVFileFromFile", tmpFile);
    file.save();
    log("用文件构造了 AVFile，并保存成功。file:" + toString(file));
  }

  String toString(AVFile file) {
    return "AVFile, url: " + file.getUrl() + " objectId:" + file.getObjectId() + " metaData" + file.getMetaData() +
        "name:" + file.getName();
  }

  public void testCreateFileFromAVObject() throws AVException {
    AVQuery<AVObject> q = new AVQuery<>("_File");
    AVObject first = q.getFirst();
    log("获取了文件 AVObject：" + first);

    AVFile file = AVFile.withAVObject(first);
    log("从 AVObject 中创建了 AVFile，file：" + toString(file));
  }

  public void testCreateFileWithObjectId() throws AVException, FileNotFoundException {
    AVQuery<AVObject> q = new AVQuery<>("_File");
    AVObject first = q.getFirst();
    log("获取了文件 AVObject：" + first);

    AVFile file = AVFile.withObjectId(first.getObjectId());
    log("从 objectId 中创建了 AVFile，并从服务器查找回了其它字段的数据，file：" + toString(file));
  }
}
