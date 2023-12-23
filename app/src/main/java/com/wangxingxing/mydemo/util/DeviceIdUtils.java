package com.wangxingxing.mydemo.util;

import android.annotation.SuppressLint;
import android.provider.Settings;
import android.text.TextUtils;


import com.blankj.utilcode.util.Utils;

import java.util.UUID;

/**
 * author : 王星星
 * date : 2023/12/23 10:18
 * email : 1099420259@qq.com
 * description : 获取设备唯一标识
 */
public class DeviceIdUtils {

    public static final String SP_NAME = "DeviceIdUtils";

    private static final    String KEY_UDID = "KEY_UDID";
    private volatile static String udid;


    public static String getUniqueDeviceId() {
        return getUniqueDeviceId("", true);
    }

    public static String getUniqueDeviceId(String prefix, boolean useCache) {
        if (!useCache) {
            return getUniqueDeviceIdReal(prefix);
        }
        if (udid == null) {
            synchronized (DeviceIdUtils.class) {
                if (udid == null) {
                    final String id = SPUtils.getInstance(SP_NAME).getString(KEY_UDID, null);
                    if (id != null) {
                        udid = id;
                        return udid;
                    }
                    return getUniqueDeviceIdReal(prefix);
                }
            }
        }
        return udid;
    }

    private static String getUniqueDeviceIdReal(String prefix) {
        try {
            final String androidId = getAndroidID();
            if (!TextUtils.isEmpty(androidId)) {
                return saveUdid(prefix + 2, androidId);
            }

        } catch (Exception ignore) {/**/}
        return saveUdid(prefix + 9, "");
    }

    @SuppressLint("HardwareIds")
    public static String getAndroidID() {
        String id = Settings.Secure.getString(
                Utils.getApp().getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
        if ("9774d56d682e549c".equals(id)) return "";
        return id == null ? "" : id;
    }

    private static String saveUdid(String prefix, String id) {
        udid = getUdid(prefix, id);
        SPUtils.getInstance(SP_NAME).put(KEY_UDID, udid);
        return udid;
    }

    private static String getUdid(String prefix, String id) {
        if (id.equals("")) {
            return prefix + UUID.randomUUID().toString().replace("-", "");
        }
        return prefix + UUID.nameUUIDFromBytes(id.getBytes()).toString().replace("-", "");
    }

}
