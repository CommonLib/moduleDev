package com.thirdparty.proxy.cache;

import android.content.Context;
import android.os.Environment;

import com.thirdparty.proxy.cache.disk.DiskLruCache;
import com.thirdparty.proxy.utils.FileUtil;
import com.thirdparty.proxy.utils.WindowUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


;

/**
 * 缓存工具类
 *
 * @author
 */

public class DiskLruCacheUtil {

    private static int appVersion = WindowUtils.getVersionCode();

    private static int valueCount = 1;// 同一个key可以对应多少个缓存文件

    private static int maxSize = 10 * 1024 * 1024;// 一个缓存文件最大可以缓存10M

    /**
     * 如何sd卡可用，此目录为sd卡 cache目录下，否则为data/data/应用名/cache文件夹下
     */
    private static String CACHE_OBJECT = "diskCache";// 对象缓存目录

    public static void init(int maxCacheSize, String cacheFolder) {
        maxSize = maxCacheSize;
        CACHE_OBJECT = cacheFolder;
    }

    /**
     * 保存对象缓存
     *
     * @param context
     * @param ser
     * @param key
     */
    public static void saveObject(Context context, Serializable ser, String key) {
        ObjectOutputStream oos = null;
        try {
            DiskLruCache.Editor editor = getDiskLruCacheEditor(context,
                    CACHE_OBJECT, key);
            if (editor != null) {
                oos = new ObjectOutputStream(editor.newOutputStream(0));
                oos.writeObject(ser);
                oos.flush();
                editor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeIO(oos);
        }
    }

    /**
     * 读取对象缓存
     *
     * @param context
     * @param key
     * @return
     */
    public static Serializable readObject(Context context, String key) {
        ObjectInputStream ois = null;
        try {
            DiskLruCache.Snapshot snapshot = getDiskLruSnapshot(context,
                    CACHE_OBJECT, key);
            if (snapshot != null) {
                InputStream in = snapshot.getInputStream(0);
                ois = new ObjectInputStream(in);
                return (Serializable) ois.readObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeIO(ois);
        }
        return null;
    }

    /**
     * 获取DiskLruCache的editor
     *
     * @param context
     * @param key
     * @return
     * @throws IOException
     */
    private static DiskLruCache.Editor getDiskLruCacheEditor(
            Context context, String uniqueName, String key) throws IOException {
        DiskLruCache mDiskLruCache = DiskLruCache.open(
                getDiskCacheDir(context, uniqueName), appVersion, valueCount,
                maxSize);
        DiskLruCache.Editor editor = mDiskLruCache.edit(hashKeyForDisk(key));
        return editor;
    }

    private static DiskLruCache.Snapshot getDiskLruSnapshot(Context context, String uniqueName, String key) throws IOException {
        DiskLruCache mDiskLruCache = DiskLruCache.open(
                getDiskCacheDir(context, uniqueName), appVersion, valueCount,
                maxSize);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKeyForDisk(key));
        return snapshot;
    }

    /**
     * 获取相应的缓存目录
     *
     * @param context
     * @param uniqueName
     * @return
     */
    private static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        File cache = new File(cachePath + File.separator + uniqueName);

        if (!cache.exists()) {
            cache.mkdir();
        }
        return cache;
    }

    /**
     * 传入缓存的key值，以得到相应的MD5值
     *
     * @param key
     * @return
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
