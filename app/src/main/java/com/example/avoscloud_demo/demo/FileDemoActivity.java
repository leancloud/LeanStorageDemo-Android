package com.example.avoscloud_demo.demo;

import android.app.Dialog;
import android.database.Observable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import cn.leancloud.LCException;
import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.callback.ProgressCallback;
import cn.leancloud.callback.SaveCallback;

import com.example.avoscloud_demo.DemoBaseActivity;
import com.example.avoscloud_demo.DemoUtils;
import com.example.avoscloud_demo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

  public void testFileUpload() throws LCException {
    selectFile(new SelectFileCallback() {
      @Override
      public void onFileSelect(File file) {
        byte[] data = DemoUtils.readFile(file);
        final LCFile LCFile = new LCFile(file.getName(), data);
        LCFile.saveInBackground(new ProgressCallback() {
          @Override
          public void done(Integer percent) {
            log("uploading: " + percent);          }
        });
      }
    });
  }

  // create an object and query it.
  public void testFileDownload() throws LCException {
    if (DemoUtils.isBlankString(fileUrl)) {
      log("Please upload file at first.");
      return;
    }
    LCFile LCFile = new LCFile("my_download_file", fileUrl, null);
    byte[] bytes = LCFile.getData();
    log("下载文件完毕，总字节数：" + bytes.length);
  }


  public void testCreateFileFromBytes() throws LCException {
    LCFile file = new LCFile("testCreateFileFromBytes", getAvatarBytes());
    file.save();
    log("从 bytes 中创建了文件 file:" + toString(file));
    logThreadTips();
  }

  private File createCacheFile(String name) throws IOException {
    File tmpFile = new File(getCacheDir(), name);
    byte[] bytes = "hello world".getBytes();
    FileOutputStream outputStream = new FileOutputStream(tmpFile);
    outputStream.write(bytes, 0, bytes.length);
    outputStream.close();
    return tmpFile;
  }

  public void testCreateFileFromPath() throws IOException, LCException {
    File tmpFile = createCacheFile("testCreateFileFromPath");

    LCFile file = LCFile.withAbsoluteLocalPath("testCreateFileFromPath", tmpFile.getAbsolutePath());
    file.save();
    log("从文件的路径中构造了 LCFile，并保存成功。file:" + toString(file));
  }

  public void testCreateLCFileFromFile() throws IOException, LCException {
    File tmpFile = createCacheFile("testCreateLCFileFromFile");

    LCFile file = LCFile.withFile("testCreateLCFileFromFile", tmpFile);
    file.save();
    log("用文件构造了 LCFile，并保存成功。file:" + toString(file));
  }

  String toString(LCFile file) {
    return "LCFile, url: " + file.getUrl() + " objectId:" + file.getObjectId() + " metaData" + file.getMetaData() +
            "name:" + file.getName();
  }
  public void testFileMetaData() throws LCException {
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, output);
    byte[] bytes = output.toByteArray();

    LCFile file = new LCFile("avatar", bytes);
    file.addMetaData("width", bitmap.getWidth());
    file.addMetaData("height", bitmap.getHeight());
    file.save();

    log("保存了文件及其 MetaData, file:" + toString(file));
  }

  LCFile saveAvatar() throws LCException {
    byte[] bytes = getAvatarBytes();
    LCFile file = new LCFile("avatar", bytes);
    file.save();
    return file;
  }

  public void testThumbnail() throws LCException {
    LCFile avatar = saveAvatar();
    String url = avatar.getThumbnailUrl(true, 200, 200);
    log("最大宽度为200 、最大高度为200的缩略图 url:" + url);
    // http://docs.qiniu.com/api/v6/image-process.html
    log("其它图片处理见七牛文档");
  }
}
