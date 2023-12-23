package com.wangxingxing.mydemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.wangxingxing.mydemo.databinding.ActivityMainBinding;
import com.wangxingxing.mydemo.sp.SPHelper;
import com.wangxingxing.mydemo.util.DeviceIdUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // 请求权限的请求码
    private static final int REQUEST_READ_PHONE_STATE = 1;

    private ActivityMainBinding binding;

    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

        String versionName = Build.VERSION.RELEASE;
        int versionCode = Build.VERSION.SDK_INT;

        Log.d("System Info", "Version Name: " + versionName);
        Log.d("System Info", "API Level: " + versionCode);

        SPHelper.init(getApplication());
        SPHelper.save("playmods_is_vip", true);
        Log.d("wxx", "保存信息");

//        getShareData();

        Log.d("wxx", "deviceId=" + DeviceUtils.getUniqueDeviceId());

        Log.d("wxx", "deviceId=" + DeviceIdUtils.getUniqueDeviceId());
    }

    private void initView() {
        binding.btnRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 检查设备信息权限
                checkPhoneStatePermission();
            }
        });


        binding.progressBar.setBackgroundColor(Color.LTGRAY);
        binding.progressBar.setProgressColor(Color.YELLOW);
        binding.progressBar.setTextColor(Color.RED);

        binding.btnDownloadProgress.setShowBorder(false);

        new Thread() {
            @Override
            public void run() {
                try {
                    while (progress < 100) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress++;
                                binding.progressBar.setProgress(progress);

                                binding.btnDownloadProgress.setState(DownloadProgressButton.STATE_DOWNLOADING);
                                binding.btnDownloadProgress.setProgress(progress);
                                binding.btnDownloadProgress.setProgressText("下载中", progress);
                            }
                        });
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();

    }

    private void getShareData() {
        ContentResolver contentResolver = getContentResolver();
        Uri sharedDataUri = Uri.parse("content://com.example.shared_data_provider/data");
        Cursor cursor = contentResolver.query(sharedDataUri, null, null, null, null);
        LogUtils.i("cursor=" + cursor);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String sharedData = cursor.getString(cursor.getColumnIndex("data"));
            cursor.close();
            // 使用共享的数据
            LogUtils.i("获取共享数据：" + sharedData);
        }

    }


    private void checkPhoneStatePermission() {
        // 检查是否已经授予了权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            // 如果已经有权限，获取设备信息
            getDeviceInformation();
        } else {
            // 如果没有权限，请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        }
    }

    private void getDeviceInformation() {
        // 获取设备信息
    }

    // 处理权限请求的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            // 检查用户是否授予了设备信息权限
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予了权限，获取设备信息
                getDeviceInformation();
            } else {
                // 用户拒绝了权限请求，可以显示一些提示信息或采取其他适当的措施
            }
        }
    }

}